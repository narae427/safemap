package com.project.safemap;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.SmsManager;
import android.text.format.Time;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import static com.project.safemap.SOS_Tab2.PhoneNumber;
import static com.project.safemap.SOS_Tab2.SmsMessage;
import static com.project.safemap.lockScreen.ampmTv;
import static com.project.safemap.lockScreen.dateTv;
import static com.project.safemap.lockScreen.timeTv;
import static com.project.safemap.GPSListener.lat;
import static com.project.safemap.GPSListener.lng;

public class lockScreen extends Activity {
	
	private LocationManager locationManager;
    private String provider;
	SeekBar mUnlockBar;
	Context mContext;
	int count = 0;
	static boolean locked = false;
	//Button finishBtn;
	private static final int DIALOG_1 = 1;
	static TextView ampmTv, dateTv, timeTv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lockscreen);
		mContext = this;
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
							|WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
		TimeTic();
		
		ampmTv = (TextView)findViewById(R.id.ampm);
		ampmTv.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/jum.ttf"));
		timeTv = (TextView)findViewById(R.id.time);
		timeTv.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/number.ttf"));
		dateTv = (TextView)findViewById(R.id.date);
		dateTv.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/jum.ttf"));
		
		setDateTime();
		
		mUnlockBar = (SeekBar) findViewById(R.id.seekBar2);
		mUnlockBar.setThumbOffset(-50);
		mUnlockBar.setMax(100);
		
		mUnlockBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				if (seekBar.getProgress() >= 80) {
					seekBar.setProgress(100);
					seekBar.setEnabled(false);
					seekBar.setFocusable(false);
					
					finish();
					
				} else {
					seekBar.setProgress(0);
				}
			}
			
		});
	
	}

	
	Handler mKillHandler = new Handler(){
		public void handleMessage(Message msg){
			if(msg.what ==0){
				count = 0;
			}
		}
	};
	
	public boolean onKeyDown(int KeyCode, KeyEvent event){
		
		if(event.getAction() == KeyEvent.ACTION_DOWN){
			if(KeyCode == KeyEvent.KEYCODE_VOLUME_UP){
				if(count==3){
					count = 0;
					
					locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			        Criteria criteria = new Criteria();
			        provider = locationManager.getBestProvider(criteria, true);
			        
			        if(provider==null){  
			         	AlertDialog.Builder dia = new AlertDialog.Builder(lockScreen.this);
			         	dia.setTitle("위치서비스 동의")
				        .setNeutralButton("이동" ,new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
							}
						}).setOnCancelListener(new DialogInterface.OnCancelListener() {
							@Override
							public void onCancel(DialogInterface dialog) {
								finish();
							}
						});
				        dia.show();
				        dia.show();
			        }else{
			        	
			    		GPSListener gpsListener = new GPSListener();
						locationManager.requestLocationUpdates(provider, 5000, 0, gpsListener);
			        }
					
						 Location lastKnowLocation = locationManager.getLastKnownLocation(provider);  
						lat = lastKnowLocation.getLatitude();
						lng = lastKnowLocation.getLongitude();
						Map map = new Map();
					
					String LocationSmsMessage = SmsMessage + "\n[" + map.getAddress(getApplicationContext(), lat, lng) + "]";
					sendSMS(PhoneNumber, LocationSmsMessage);
					
					return true;
				}else{
					 
					count++;
					mKillHandler.sendEmptyMessageDelayed(0, 2000);
					return true;
				}
			}else if(KeyCode == KeyEvent.KEYCODE_HOME){
				
			}
			
		}
		
		return false;
	}
	
	private void sendSMS(String phoneNumber, String message){
		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage(phoneNumber, null, message, null, null);
		Toast.makeText(getApplicationContext(), "문자가 전송되었습니다.", Toast.LENGTH_SHORT).show();
	
	}
	
	public Dialog onCreateDialog(int id){
		switch(id){
		case DIALOG_1:
			return alertDial();
		}
		return null;
		
	}
	
	public Dialog alertDial(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("S.O.S Service 를 중단하시겠습니까?").setCancelable(false)
		.setNegativeButton("예", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
			
    			Intent intent = new Intent(lockScreen.this, sosService.class);
				stopService(intent);
				
				screenReceiver.reenableKeyguard();
				finish();
			}
		})
		
		.setPositiveButton("아니오", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.cancel();
				
			}
		});
		AlertDialog alert = builder.create();
		return alert;
	}
	
	private void setDateTime(){
		final Calendar calendar = Calendar.getInstance();
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int dayofWeek = calendar.get(Calendar.DAY_OF_WEEK);
		
		int hour = calendar.get(Calendar.HOUR);
		int minute = calendar.get(Calendar.MINUTE); 
		String stringMinute = String.valueOf(minute);
		int ampm = calendar.get(Calendar.AM_PM);
		if(hour==0){
			hour=12;
		}
		if(minute<10){
			stringMinute = "0"+stringMinute;
		}
		
		String stringdayofWeek[] = {"","일","월","화","수","목","금","토"};
		String stringampm[] = {"오전","오후"};	
		
		String stringDate = String.format("%d월 %d일 " + stringdayofWeek[dayofWeek] +"요일", month+1,day);
		String stringAmpm = String.format(stringampm[ampm]);
		String stringTime = String.format("%d : %s", hour, stringMinute);

		ampmTv.setText(stringAmpm);
		timeTv.setText(stringTime);
		dateTv.setText(stringDate);
	}
	
	private void TimeTic(){
		BroadcastReceiver receiver = new BroadcastReceiver(){
			public void onReceive(Context context, Intent intent){
				if(Intent.ACTION_TIME_TICK.equals(intent.getAction())){
					setDateTime();
					
				}
			}
			
		};
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Intent.ACTION_TIME_TICK);
		registerReceiver(receiver,intentFilter);
	}


}
