package tsysv.anti.ttt;

import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MailActivity extends Activity {

	Button mailnext;
    // GPSTracker class
    GPSTracker gps;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mailsend);
		
		registerReceiver(stop_receiver, new IntentFilter("stop_mail"));
		
		mailnext = (Button) findViewById(R.id.mailnext);
		mailnext.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				 // create class object
                gps = new GPSTracker(MailActivity.this);
 
                // check if GPS enabled
                if(gps.canGetLocation()){
 
                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();
 
                    // \n is for new line
                    Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                }else{
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    gps.showSettingsAlert();
                  //  finish();
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
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
