package com.forsuntech.secchkphone.receiver;

import com.forsuntech.secchkphone.service.ControlIOService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ControlIOReceiver extends BroadcastReceiver {

	private static String START_ACTION = "NotifyServiceStart";
	private static String STOP_ACTION = "NotifyServiceStop";

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(ControlIOService.TAG, Thread.currentThread().getName() + "---->"  
                + "ServiceBroadcastReceiver onReceive");  
  
        String action = intent.getAction();  
        if (START_ACTION.equalsIgnoreCase(action)) {  
            context.startService(new Intent(context, ControlIOService.class));  
  
            Log.d(ControlIOService.TAG, Thread.currentThread().getName() + "---->"  
                    + "ServiceBroadcastReceiver onReceive start end");  
        } else if (STOP_ACTION.equalsIgnoreCase(action)) {  
            context.stopService(new Intent(context, ControlIOService.class));  
            Log.d(ControlIOService.TAG, Thread.currentThread().getName() + "---->"  
                    + "ServiceBroadcastReceiver onReceive stop end");  
        }  
	}

}
