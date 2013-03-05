package tsysv.anti.ttt;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MailActivity extends Activity {

	Intent IntentService;
	Button mailnext;
    // GPSTracker class
    GPSTracker gps;
    EditText emailhint;
	final private static int DIALOG_SENDTEST = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mailsend);
		
		registerReceiver(stop_receiver, new IntentFilter("stop_mail"));
		
		emailhint = (EditText) findViewById(R.id.emailhint);
		
		if (getSharedPreferences("antithief", MODE_PRIVATE).getString("email", "").equals("")){
			emailhint.setText("");
		}else{
			emailhint.setText(getSharedPreferences("antithief", MODE_PRIVATE).getString("email", ""));
		}
		
		mailnext = (Button) findViewById(R.id.mailnext);
		mailnext.setOnClickListener(new View.OnClickListener() {
			
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				
				if (isEmailValid(emailhint.getText().toString())){
					
					  getSharedPreferences("antithief", MODE_PRIVATE).edit()
						.putString("email", emailhint.getText().toString())
						.commit();
					
					 // create class object
	                gps = new GPSTracker(MailActivity.this);
	 
	                // check if GPS enabled
	                if(gps.canGetLocation()){
	 
	                    double latitude = gps.getLatitude();
	                    double longitude = gps.getLongitude();
	 
	                    // \n is for new line
	             //       Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " 
	               //     + longitude, Toast.LENGTH_LONG).show();
	                    
	                    getSharedPreferences("antithief", MODE_PRIVATE).edit()
						.putString("gpscoord", "http://maps.google.ru/maps?q=" + latitude + "," + longitude)
						.commit();
	                   
	                    showDialog(DIALOG_SENDTEST);
	                   

	                    
	                 }else{
	                    // can't get location
	                    // GPS or Network is not enabled
	                    // Ask user to enable GPS/network in settings
	                    gps.showSettingsAlert();
	                  //  finish();
	                }
					
				}
				else{
					Toast.makeText(getApplicationContext(), getResources().getString(R.string.hintemailiswr), Toast.LENGTH_LONG).show();
				}
				
			}
		});
		
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		unregisterReceiver(stop_receiver);
		
	}
	
	
	private final BroadcastReceiver stop_receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
              finish();                                   
        }
	};

	
	  @Override
	    protected Dialog onCreateDialog(int id) {
	    
	     AlertDialog dialogDetails = null;
	    
	     switch (id) {
	   
	     case DIALOG_SENDTEST:
		      LayoutInflater iinflater = LayoutInflater.from(this);
		      View ddialogview = iinflater.inflate(R.layout.sendtest, null);
		      AlertDialog.Builder ddialogbuilder = new AlertDialog.Builder(this);
		      ddialogbuilder.setTitle(getResources().getString(R.string.gpsalerttest));
		      ddialogbuilder.setView(ddialogview);
		      dialogDetails = ddialogbuilder.create();
	   
	     break;

	     }
	    
	     return dialogDetails;
	    }
	    ////
	    @Override
	    protected void onPrepareDialog(int id, Dialog dialog) {
	    
	     switch (id) {
	     	
	     case DIALOG_SENDTEST:
	    	 final AlertDialog aelertDialog = (AlertDialog) dialog;
	    	 Button yes = (Button) aelertDialog.findViewById(R.id.yes);
	    	 Button no = (Button) aelertDialog.findViewById(R.id.no);
	    	 yes.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if (isNetworkAvailable()){
	                    IntentService = new Intent(getApplicationContext(), tsysv.anti.ttt.GPSService.class).setAction("GPS_send");
		                getApplicationContext().startService(IntentService);
					}
					else{
						Toast.makeText(getApplicationContext(), getResources().getString(R.string.nointernet), Toast.LENGTH_LONG).show();
					}
					finish();
					
				}
			});
	    	 no.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					aelertDialog.dismiss();
					finish();
				}
			});
	    	 break;

	     
	     }
	    }

	    private boolean isNetworkAvailable() {
		    ConnectivityManager connectivityManager 
		          = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		    return activeNetworkInfo != null;
		}

	    public static boolean isEmailValid(String email) {
	        boolean isValid = false;

	        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
	        CharSequence inputStr = email;

	        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
	        Matcher matcher = pattern.matcher(inputStr);
	        if (matcher.matches()) {
	            isValid = true;
	        }
	        return isValid;
	    }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
