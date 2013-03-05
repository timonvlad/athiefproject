package tsysv.anti.ttt;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;


	public class BootReceiver extends BroadcastReceiver
	{
		Intent IntentService;
		
	@Override
	public void onReceive(Context context, Intent intent)
	{		
	    if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
	    	

	    //	Toast.makeText(context, "BOOT RECEIVE", Toast.LENGTH_LONG).show();
	    //	IntentService = new Intent(context, tsysv.anti.ttt.GPSService.class).setAction("GPS_BOOT");
        //    context.startService(IntentService);
        	IntentService = new Intent(context, tsysv.anti.ttt.BootCoordService.class);
            context.startService(IntentService);
	    }
	}
	public void sleep(int sl)
	{
			try {Thread.sleep(sl);} 
			catch (InterruptedException e) {e.printStackTrace();}
	}

}

