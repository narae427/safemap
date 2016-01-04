package com.project.safemap;

import java.util.Locale;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class SOSActivity extends FragmentActivity implements
		ActionBar.TabListener {
	
	NotificationManager notiManager;
	 Vibrator vibrator;
	 final static int MyNoti=0;
	 



	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;
	static Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sos);
		mContext = this;
		// Set up the action bar.
/*
		notiManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        handler.sendEmptyMessageDelayed(0, 10000);
        */        
		final ActionBar actionBar = getActionBar();
		
		actionBar.setDisplayOptions(ActionBar.DISPLAY_USE_LOGO,ActionBar.DISPLAY_USE_LOGO);
		//actionBar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP,ActionBar.DISPLAY_HOME_AS_UP);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ff8c00")));
		actionBar.setHomeButtonEnabled(true);
		actionBar.setTitle("  S.O.S");
		actionBar.setStackedBackgroundDrawable(new ColorDrawable(Color.parseColor("#ff8c00")));	

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getApplicationContext(), getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.sospager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sos, menu);
		return true;
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {
		Context mContext;
		public SectionsPagerAdapter(Context context, FragmentManager fm) {
			super(fm);
			mContext = context;
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			/*
			Fragment fragment = new DummySectionFragment();
			Bundle args = new Bundle();
			args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
			fragment.setArguments(args);
			return fragment;
			*/
			switch(position){
			case 0:
				return new SOS_Tab1();
			case 1:
				return new SOS_Tab2();
				
			}
			return null;
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.sos_tab1).toUpperCase(l);
			case 1:
				return getString(R.string.sos_tab2).toUpperCase(l);
			}
			return null;
		}
	}
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
		inflater.inflate(R.menu.sos, menu);
	}
	
	public void intentService(){
		Intent serviceIntent = new Intent(getApplicationContext(), sosService.class);
		//BroadcastReceiver receiver = new screenReceiver();
		//serviceIntent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
		//serviceIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		//registerReceiver(receiver);
		startService(serviceIntent);
		
		notiManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        handler.sendEmptyMessageDelayed(0, 1000);
	}
	public void stopService(){
		Intent intent = new Intent(mContext, sosService.class);
		stopService(intent);
		
		notiManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
		notiManager.cancel(MyNoti);
		
		//screenReceiver.reenableKeyguard();
		
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	/*
	public static class DummySectionFragment extends Fragment {
		
		public static final String ARG_SECTION_NUMBER = "section_number";

		public DummySectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View v = null;
			if(getArguments().getInt(ARG_SECTION_NUMBER)==1)
				v = inflater.inflate(R.layout.sos1, container,false);
			else if(getArguments().getInt(ARG_SECTION_NUMBER)==2)
				v = inflater.inflate(R.layout.sos2, container,false);
			
			View rootView = inflater.inflate(R.layout.fragment_so_dummy,
					container, false);
			TextView dummyTextView = (TextView) rootView
					.findViewById(R.id.section_label);
			dummyTextView.setText(Integer.toString(getArguments().getInt(
					ARG_SECTION_NUMBER)));
					*/
			//return v;
		//}
	//}
	
	
	public void onBackPressed(){
		
	}
	
	public void onNewIntent(Intent i){
		
	}
	
	
	Handler handler = new Handler(){

	     public void handleMessage(android.os.Message msg) {

	      //notification 객체 생성(상단바에 보여질 아이콘, 메세지, 도착시간 정의)

	      Notification noti = new Notification(R.drawable.sossmall//알림창에 띄울 아이콘

	               , "SafeMap", //간단 메세지

	               System.currentTimeMillis()); //도착 시간

	      //기본으로 지정된 소리를 내기 위해

	      noti.defaults = Notification.DEFAULT_SOUND;

	      //알림 소리를 한번만 내도록

	      noti.flags = Notification.FLAG_ONLY_ALERT_ONCE;

	      //확인하면 자동으로 알림이 제거 되도록

	      //noti.flags = Notification.FLAG_AUTO_CANCEL;
	      noti.flags = Notification.FLAG_NO_CLEAR;

	      //사용자가 알람을 확인하고 클릭했을때 새로운 액티비티를 시작할 인텐트 객체

	      Intent intent = new Intent(SOSActivity.this, SOSActivity.class);
	      /*
	      intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
	    		  		|Intent.FLAG_ACTIVITY_MULTIPLE_TASK
	    		  		|Intent.FLAG_ACTIVITY_NEW_TASK);
	    		  		*/
	      
	      //새로운 태스크(Task) 상에서 실행되도록(보통은 태스크1에 쌓이지만 태스크2를 만들어서 전혀 다른 실행으로 관리한다)

	      //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

	      //인텐트 객체를 포장해서 전달할 인텐트 전달자 객체

	      PendingIntent pendingI = PendingIntent.getActivity(SOSActivity.this, 0, intent, Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);

	      //상단바를 드래그 했을때 보여질 내용 정의하기

	      noti.setLatestEventInfo(SOSActivity.this, "[SafeMap]", "S.O.S Service 실행중", pendingI);

	      //알림창 띄우기(알림이 여러개일수도 있으니 알림을 구별할 상수값, 여러개라면 상수값을 달리 줘야 한다.)

	      notiManager.notify(MyNoti, noti);

	      //진동주기(** 퍼미션이 필요함 **)

	      vibrator.vibrate(1000); //1초 동안 진동

	     }

	    };





}
