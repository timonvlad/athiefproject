package tsysv.anti.ttt;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SmsActivity extends Activity {
	
	EditText hintph;
	Button smssend;
	GPSTracker gps;
	final private static int DIALOG_SENDTEST = 0;
	Intent IntentService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.smssend);
		
		hintph = (EditText) findViewById(R.id.hintsmsphone);
		
		if (getSharedPreferences("antithief", MODE_PRIVATE).getString("phone", "").equals("")){
			hintph.setText("");
		}else{
			hintph.setText(getSharedPreferences("antithief", MODE_PRIVATE).getString("phone", ""));
		}
		
		smssend = (Button) findViewById(R.id.smsnext);
		smssend.setOnClickListener(new View.OnClickListener() {
			
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				if (!hintph.getText().toString().equals("")){
						getSharedPreferences("antithief", MODE_PRIVATE).edit()
						.putString("phone", hintph.getText().toString())
						.commit();
					
					Boolean checked_mail = getSharedPreferences("antithief", MODE_PRIVATE)
							.getBoolean("checkedmail", false);
					if (checked_mail){
						Intent i = new Intent(getApplicationContext(), MailActivity.class);
						startActivity(i);
						finish();
					}else{
						 // create class object
		                gps = new GPSTracker(SmsActivity.this);
		 
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
					}
		                }
				}else{
					Toast.makeText(getApplicationContext(), getResources().getString(R.string.hintsmsiswr), Toast.LENGTH_LONG).show();
				}
			}
		});
		
	}
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
