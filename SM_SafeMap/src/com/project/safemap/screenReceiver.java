package com.project.safemap;

import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;

import com.project.safemap.sosService;

public class screenReceiver extends BroadcastReceiver {
	final static String ACTION_RESTART_PERSISTENTSERVICE = "ACTION.Restart.persistentService";
	public static KeyguardManager km = null;
	public static KeyguardManager.KeyguardLock keyLock = null;
	
	public void onReceive(Context context, Intent intent){
		if("android.intent.action.SCREEN_OFF".equals(intent.getAction())){
			
			if(km==null)
				km = (KeyguardManager)context.getSystemService(Context.KEYGUARD_SERVICE);
			
			if(keyLock==null)
				keyLock = km.newKeyguardLock(Context.KEYGUARD_SERVICE);		
			
			
			Intent pushIntent = new Intent(context, lockScreen.class);

			pushIntent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);			
			pushIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
								|Intent.FLAG_ACTIVITY_MULTIPLE_TASK
								|Intent.FLAG_ACTIVITY_NEW_TASK);
								
			disableKeyguard();
			context.startActivity(pushIntent);
			
			if(intent.getAction().equals("ACTION_RESTART_PERSISTENTSERVICE")){
				Intent i = new Intent(context, sosService.class);
				context.startService(i);
			}
			
		}
	}
	
	public static void reenableKeyguard(){
		keyLock.reenableKeyguard();
	}
	
	public static void disableKeyguard(){
		keyLock.disableKeyguard();
	}

}
