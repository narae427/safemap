package com.project.safemap;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.os.Vibrator;
import android.telephony.SmsManager;
import android.view.KeyEvent;
import android.widget.Toast;

public class sosService extends Service implements Runnable{
	private screenReceiver mReceiver = null;
	NotificationManager notiManager;
	Vibrator vibrator;
	final static int MyNoti=0;
	 
	Handler handler;
	boolean mIsRunning;
	int mStartId = 0;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		unregisterRestartAlarm();
		super.onCreate();
		mIsRunning = false;
	
		mReceiver = new screenReceiver();
		IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
		registerReceiver(mReceiver, filter);
		
		
        }
        
        public int onStartCommand(Intent intent, int flags, int startId){
        	super.onStartCommand(intent, flags, startId);
        	
        	if(intent!=null){
        		if(intent.getAction()==null){
        			if(mReceiver==null){
        				mReceiver = new screenReceiver();
        				IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        				registerReceiver(mReceiver, filter);        				
        			}
        		}        			
        	}
        	return START_REDELIVER_INTENT;
        }
    	
    	
        public void onDestroy(){
        	registerRestartAlarm();
    		super.onDestroy();
    		mIsRunning = false;
        	
        	if(mReceiver!=null){
        		unregisterReceiver(mReceiver);
        	}
        	
        }
        
        public void onStart(Intent intent, int startId){
    		super.onStart(intent, startId);
    		mStartId = startId;
    		
    		handler = new Handler();
    		handler.postDelayed(this, 1000);
    		mIsRunning = true;
    		
    	}
        
        public void run(){
    		if(!mIsRunning){
    			return;
    		}
    		else{
    			handler.postDelayed(this, 1000);
    			mIsRunning = true;
    		}
    	}
        
        public void unregisterRestartAlarm(){
    		Intent intent = new Intent(sosService.this, screenReceiver.class);
    		intent.setAction(screenReceiver.ACTION_RESTART_PERSISTENTSERVICE);
    		PendingIntent sender = PendingIntent.getService(sosService.this, 0, intent, 0);
    		long firstTime = SystemClock.elapsedRealtime();
    		firstTime += 10*1000;
    		AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
    		am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,firstTime, 10*1000, sender);
    		//Toast.makeText(getApplicationContext(), "unregisterRestartAlarm", Toast.LENGTH_SHORT).show();
    		
    		
    	}
    	public void registerRestartAlarm(){    		
    		Intent intent = new Intent(sosService.this, screenReceiver.class);
    		intent.setAction(screenReceiver.ACTION_RESTART_PERSISTENTSERVICE);
    		PendingIntent sender = PendingIntent.getService(sosService.this, 0, intent, 0);
    		AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
    		am.cancel(sender);
    		//Toast.makeText(getApplicationContext(), "registerRestartAlarm", Toast.LENGTH_SHORT).show();
    	}	
}
