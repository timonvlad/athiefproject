package tsysv.anti.ttt;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


	public class BootReceiver extends BroadcastReceiver
	{
		Intent IntentService;
		
	@Override
	public void onReceive(Context context, Intent intent)
	{		
	    if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {

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

