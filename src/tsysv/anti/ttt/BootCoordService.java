package tsysv.anti.ttt;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;


	public class BootCoordService extends Service {

		GPSTracker gps;
		String longitude, latitude, coo;
		Intent IntentService;
		private Handler mHandlerTime = new Handler();
		 public static boolean delayedAgainHour = false;

		
	   	
	   	Runnable  mUpdateTimeTask = new Runnable() {             
	        public void run() {
	            if(delayedAgainHour)
	            {
	            
	            }
	            else
	            {
	            	stopSelf();
	            }                   
	        }
	    };

		
		
		
		
	@Override
	public IBinder onBind(Intent intent) {
	        return null;
	}
	
	    
	@Override
	public void onCreate() {
		gps = new GPSTracker(tsysv.anti.ttt.BootCoordService.this);
		 
        // check if GPS enabled
        if(gps.canGetLocation()){

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            // \n is for new line
            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " 
            + longitude, Toast.LENGTH_LONG).show();
            
            getSharedPreferences("antithief", MODE_PRIVATE).edit()
			.putString("gpscoord", "http://maps.google.ru/maps?q=" + latitude + "," + longitude)
			.commit();
          
         }else{

       //     gps.showSettingsAlert();
            mHandlerTime.removeCallbacks(mUpdateTimeTask);
    		mHandlerTime.postDelayed(mUpdateTimeTask, 30000);

        }
     	

	}    
   
	@Override
	public void onDestroy() {
	 	super.onDestroy();
	 	IntentService = new Intent(getApplicationContext(), tsysv.anti.ttt.GPSService.class).setAction("GPS_BOOT");
        getApplicationContext().startService(IntentService);
	}
    
	@Override
	public void onStart(Intent intent, int startid) {
		
	}
	public void sleep(int sl)
	{
			try {Thread.sleep(sl);} 
			catch (InterruptedException e) {e.printStackTrace();}
	}

 	
}