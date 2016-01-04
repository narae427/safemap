package com.project.safemap;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class restartService extends BroadcastReceiver{
	final static String ACTION_RESTART_PERSISTENTSERVICE = "ACTION.Restart.persistentService";
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if(intent.getAction().equals("ACTION_RESTART_PERSISTENTSERVICE")){
			Intent i = new Intent(context, sosService.class);
			context.startService(i);
		}
		
	}

}
