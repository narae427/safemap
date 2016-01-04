package com.project.safemap;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.widget.Toast;

public class persistentService extends Service implements Runnable{
	Handler handler;
	boolean mIsRunning;
	int mStartId = 0;
	public void onCreate(){
		unregisterRestartAlarm();
		super.onCreate();
		mIsRunning = false;
	}
	
	public void onDestroy(){
		registerRestartAlarm();
		super.onDestroy();
		mIsRunning = false;
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
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void unregisterRestartAlarm(){
		Intent intent = new Intent(persistentService.this, sosService.class);
		intent.setAction(restartService.ACTION_RESTART_PERSISTENTSERVICE);
		PendingIntent sender = PendingIntent.getService(persistentService.this, 0, intent, 0);
		long firstTime = SystemClock.elapsedRealtime();
		firstTime += 10*1000;
		AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
		am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,firstTime, 10*1000, sender);
		Toast.makeText(getApplicationContext(), "unregisterRestartAlarm", Toast.LENGTH_SHORT).show();
		
		
	}
	public void registerRestartAlarm(){
		
		
		Intent intent = new Intent(persistentService.this, restartService.class);
		intent.setAction(restartService.ACTION_RESTART_PERSISTENTSERVICE);
		PendingIntent sender = PendingIntent.getService(persistentService.this, 0, intent, 0);
		AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
		am.cancel(sender);
		Toast.makeText(getApplicationContext(), "registerRestartAlarm", Toast.LENGTH_SHORT).show();
	}
}
