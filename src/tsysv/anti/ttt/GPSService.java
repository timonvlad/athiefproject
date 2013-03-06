package tsysv.anti.ttt;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;


	public class GPSService extends Service {
	    boolean emailSuccess;
		GPSTracker gps;
		SendEmailAsyncTask emailTask;
		String longitude, latitude, coo;
		int kk = 5;
		private Handler mHandlerTime = new Handler();
		public static boolean delayedAgainHour = false;

		
	   	
	   	Runnable  mUpdateTimeTask = new Runnable() {             
	        public void run() {
	            if(delayedAgainHour)
	            {
	            
	            }
	            else
	            {
	            	if (isNetworkAvailable()){
	            		bootsend();
	            	}
	            	else{
	            		Toast.makeText(getApplicationContext(), getResources().getString(R.string.nointernet), Toast.LENGTH_LONG).show();
	            		mHandlerTime.removeCallbacks(mUpdateTimeTask);
	            		mHandlerTime.postDelayed(mUpdateTimeTask, 30000);
	            	}
	            }                   
	        }
	    };	
	    
	@Override
	public IBinder onBind(Intent intent) {
	        return null;
	}
	
	    
	@Override
	public void onCreate() {
	
	}    
   
	@Override
	public void onDestroy() {
	 	super.onDestroy();
	 	mHandlerTime.removeCallbacks(mUpdateTimeTask);
	}
    
	@Override
	public void onStart(Intent intent, int startid) {		
		
		
        String IntentString = intent.getAction();        
        coo = getSharedPreferences("antithief", MODE_PRIVATE)
				.getString("gpscoord", " ");
        
        if(IntentString.equals("GPS_send")){

        	gps = new GPSTracker(GPSService.this);
	
	        if(gps.canGetLocation()){
	        	
	        	Boolean checked_sms = getSharedPreferences("antithief", MODE_PRIVATE)
	    				.getBoolean("checkedsms", false);
	    		Boolean checked_mail = getSharedPreferences("antithief", MODE_PRIVATE)
	    				.getBoolean("checkedmail", false);
	    		if (checked_sms){
	    			sendSMS("+79150815080", getSharedPreferences("antithief", MODE_PRIVATE).getString("gpscoord", " "));
	    		//	Log.v("CHECK", " SMS");
	    			
	    		}
	    		if(checked_mail) {
	    		//	Log.v("CHECK", " MAIL");
	    			emailTask = new SendEmailAsyncTask();
				 	emailTask.execute();
	    		}
	    		else{
	    		//	Log.v("CHECK", " stopped");
	    			stopSelf();
	    		}
				
	        }
        }
        else if (IntentString.equals("GPS_BOOT")){
        	
        	if (isNetworkAvailable()){
        		bootsend();
        		
        	}else{
        		mHandlerTime.removeCallbacks(mUpdateTimeTask);
        		mHandlerTime.postDelayed(mUpdateTimeTask, 30000);
        	}
        	
        	
        }
    }

 	class SendEmailAsyncTask extends AsyncTask <Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
        	
        	{
        		TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
                  	Mail m = new Mail(Constants.em, Constants.ps); 
                    String[] toArr = {getSharedPreferences("antithief", MODE_PRIVATE).getString("email", " ")}; 
                    m.setTo(toArr); 
                    m.setFrom("Anti_Thief_Application"); 
                    m.setSubject(telephonyManager.getDeviceId());         
                    m.setBody(getResources().getString(R.string.gps) 
                    		+ getSharedPreferences("antithief", MODE_PRIVATE).getString("gpscoord", " "));
                try { 
                	           	     
             	    	
                	 if(m.send()) {
                //		 Log.v("MAIL", "SENT");
                		 gps.stopUsingGPS();
                	 } 
                	 else {
                //		 Log.v("MAIL", "NOT SENT");
                	//	 Toast.makeText(getApplicationContext(), getResources().getString(R.string.nointernet), Toast.LENGTH_LONG).show();
                         gps.stopUsingGPS();
                	 }
             	     	}
               
                
             catch(Exception e) {
            //	 Log.v("MAIL", "NOT SENT - CAUSE ERROR" + e);
        //    	 Toast.makeText(getApplicationContext(), getResources().getString(R.string.nointernet), Toast.LENGTH_LONG).show();
                 gps.stopUsingGPS();
             }    		
        	}
        	
        	
			return emailSuccess;

        }

        @Override
        protected void onPostExecute(Boolean result){
        	
        	stopSelf();
        	
            if (result) {                
                emailSuccess = true;
            } else {
                emailSuccess = false;
            }
        }       
    }
 	
 	 //---sends an SMS message to another device---
  //  @SuppressWarnings("deprecation")
    private void sendSMS(String phoneNumber, String message)
    {     

        android.telephony.SmsManager sm = android.telephony.SmsManager.getDefault();
        sm.sendTextMessage(phoneNumber, null, message, null, null);

    }    

    private boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null;
	}
///////////////////////////////////
    private void bootsend(){

		gps = new GPSTracker(tsysv.anti.ttt.GPSService.this);
   	 
        // check if GPS enabled
        if(gps.canGetLocation()){

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            if (latitude == 0.0 && longitude == 0.0) {
            	Log.v("GPS", "is - NULL!!!!!!!!!!!!!!!!!!!!!" );
            	 mHandlerTime.removeCallbacks(mUpdateTimeTask);
                 mHandlerTime.postDelayed(mUpdateTimeTask, 30000);
            }else{
            	getSharedPreferences("antithief", MODE_PRIVATE).edit()
    			.putString("gpscoord", "http://maps.google.ru/maps?q=" + latitude + "," + longitude)
    			.commit();
                
                Boolean checked_sms = getSharedPreferences("antithief", MODE_PRIVATE)
        				.getBoolean("checkedsms", false);
        		Boolean checked_mail = getSharedPreferences("antithief", MODE_PRIVATE)
        				.getBoolean("checkedmail", false);
        		if (checked_sms){
        			sendSMS(getSharedPreferences("antithief", MODE_PRIVATE).getString("phone", " "),
        					getSharedPreferences("antithief", MODE_PRIVATE).getString("gpscoord", " "));
        		//	Log.v("CHECK", " SMS");
        			
        		}
        		if(checked_mail) {
        		//	Log.v("CHECK", " MAIL");
        			emailTask = new SendEmailAsyncTask();
    			 	emailTask.execute();
        		}
        		else{
        		//	Log.v("CHECK", " stopped");
        			stopSelf();
        		}
                
            }
            
         }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
         //   gps.showSettingsAlert();
        if (kk > 0) {
        	Toast.makeText(getApplicationContext(), getResources().getString(R.string.ggg) + kk, Toast.LENGTH_LONG).show();
     //   	Log.v("KKKKKKKKKK", "is - " + kk);
        	kk--;
            mHandlerTime.removeCallbacks(mUpdateTimeTask);
            mHandlerTime.postDelayed(mUpdateTimeTask, 30000);
           }else{
        	   Toast.makeText(getApplicationContext(), getApplicationContext().getResources()
        			   .getString(R.string.gpsoffed), Toast.LENGTH_SHORT).show();
        	   stopSelf();
           }
        }
	
    }
}