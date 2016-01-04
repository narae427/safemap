package com.project.safemap;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import static com.project.safemap.Review_Tab2.rvInflater;
import static com.project.safemap.Review_Tab2.rvContainer;
import static com.project.safemap.Review_Tab2.rvBundle;

public class Review_Tab1 extends Fragment {
	Spinner spinner;
	static EditText rvLocation, rvName, rvComment;
    static String UserNickName="";
	String sdPath = Environment.getExternalStorageDirectory().getAbsolutePath();
	File file = new File(sdPath + "/SafeMap/UserId.txt");
	
	Button saveBtn;
	
	static double Review_Tab1_lat=0;
	static double Review_Tab1_lng=0;
	InputMethodManager imm;
	Handler handler;
	String currLocation;
	static InnerHandler Review_Tab1_MessageHandler;
	View loadingLayout;
	LayoutInflater inflater;
	View view;
	Location lastKnowLocation;
	
	static String nickName, location, comment, date;
	static int icon;
    
	public Review_Tab1(){
	}
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState){
		setHasOptionsMenu(true);
		view = inflater.inflate(R.layout.review1, null);
		
		spinner = (Spinner)view.findViewById(R.id.spinner1);
		
		List<HashMap<String,Object>> data = new ArrayList<HashMap<String,Object>>();
		HashMap<String, Object> hashmap = new HashMap<String, Object>();
		
		hashmap.put("logo", R.drawable.yellow);
		hashmap.put("text", "양호");
		
		HashMap<String, Object> hashmap2 = new HashMap<String, Object>();
		
		hashmap2.put("logo", R.drawable.orange);
		hashmap2.put("text", "위험");
		
		HashMap<String, Object> hashmap3 = new HashMap<String, Object>();
		
		hashmap3.put("logo", R.drawable.red);
		hashmap3.put("text", "사망");
		
		data.add(hashmap);
		data.add(hashmap2);
		data.add(hashmap3);

		
		SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(), data, 
				R.layout.spinner, new String[]{"logo"}, 
				new int[]{R.id.warningIcon});

		
		spinner.setAdapter(simpleAdapter);
		
		imm=(InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		
		
		rvName = (EditText)view.findViewById(R.id.rvName);		
		rvLocation = (EditText)view.findViewById(R.id.rvLocation);
		rvComment = (EditText)view.findViewById(R.id.rvComment);
		
		setName();
		
		saveBtn = (Button)view.findViewById(R.id.saveBtn);
		saveBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				nickName = rvName.getText().toString();
				location = rvLocation.getText().toString();
				comment = rvComment.getText().toString();
				icon = spinner.getSelectedItemPosition();
				//icon date
				
				if (Build.VERSION.SDK_INT >= 11) {
					new ReviewAsyncTask("Review_Tab1").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR );
				} else {
					new ReviewAsyncTask("Review_Tab1").execute();
				}
			      
				Review_Tab1_MessageHandler = new InnerHandler( Review_Tab1.this );
				ProgressThread progressThread = new ProgressThread();
				progressThread.start();
				
				
				
			}
		});
		
		
		Review_Tab1_MessageHandler = new InnerHandler( Review_Tab1.this );
		
		String locationProvider=null;
		LocationListener locationListener = null;
		LocationManager lm = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
		if (lm.isProviderEnabled(lm.NETWORK_PROVIDER) == true) {
			locationProvider = LocationManager.NETWORK_PROVIDER; 
		} else{        
			locationProvider = LocationManager.GPS_PROVIDER;
		}
		try{
			GPSListener gpsListener = new GPSListener();
			lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, gpsListener);

			 Location lastKnowLocation = lm.getLastKnownLocation(locationProvider);  
			 Review_Tab1_lat = lastKnowLocation.getLatitude();
			Review_Tab1_lng = lastKnowLocation.getLongitude();
			Map map = new Map();
			rvLocation.setText(map.getAddress(getActivity(),Review_Tab1_lat, Review_Tab1_lng));
		}catch(Exception e){
		}
		
		////////////////////////////////////////////////////////////////현재 위치를 못가져옴
		
		
		rvLocation.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent mapIntent = new Intent(getActivity(),Map.class);
				startActivity(mapIntent);
				
			}
		});
		return view;
	}
	public String getName(){
		return nickName;
	}
	public String getLocation(){
		return location;
	}
	public String getComment(){
		return comment;
	}
	public int getIcon(){
		return icon;
	}
	public double getLat(){
		return Review_Tab1_lat;
	}
	public double getLng(){
		return Review_Tab1_lng;
	}
	
	public void setName(){
		String email="";
		String nickName="";
		
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(sdPath + "/SafeMap/UserId.txt");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Scanner s = new Scanner(fis);
		String str = null;
		int i=0;
		while(s.hasNext()){
			str = s.next();
			if(i==0){
				email = str;				
			}
			else{
				nickName = str;
			}
			i++;
		}
		rvName.setText(nickName);
		UserNickName = nickName;
	
		try {
			fis.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
	}
	
	
	public void onPrepareOptionsMenu(Menu menu){
	}
	
	public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getItemId()){
		case android.R.id.home:
			getActivity().finish();
			Intent homeIntent = new Intent(getActivity(), MainActivity.class);
			homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(homeIntent);			
			break;
		default:
			return super.onOptionsItemSelected(item);
		}
		return true;
	}
	
	
	public class LocationThread extends Thread{
		StringBuilder output = new StringBuilder();
		public LocationThread(){
			output = new StringBuilder();
			
		}
		public void run(){
			String locationProvider=null;
			LocationManager lm = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
			if (lm.isProviderEnabled(lm.NETWORK_PROVIDER) == true) {
				locationProvider = LocationManager.NETWORK_PROVIDER; 
			} else{        
				locationProvider = LocationManager.GPS_PROVIDER;
			}
			try{
				GPSListener gpsListener = new GPSListener();
				lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, gpsListener);

				lastKnowLocation = lm.getLastKnownLocation(locationProvider);  
				Review_Tab1_lat = lastKnowLocation.getLatitude();
				Review_Tab1_lng = lastKnowLocation.getLongitude();
				Map map = new Map();
				currLocation = map.getAddress(getActivity(),Review_Tab1_lat, Review_Tab1_lng);
				
				output.append(currLocation);
				
			}catch(Exception e){
			}
		
	        
		}
		public String getResult(){
			return output.toString();
		}
	}
	public class ProgressThread extends Thread {
		 public void run() {
	            Message msg;
	            
	            try {
	                msg = Review_Tab1_MessageHandler.obtainMessage();
	                msg.arg1 = 0;
	                Review_Tab1_MessageHandler.sendMessage( msg );

	                Thread.sleep( 2000 );
	            }catch( Exception e ) {
	                msg = Review_Tab1_MessageHandler.obtainMessage();
	                msg.arg1 = 99;
	                Review_Tab1_MessageHandler.sendMessage( msg );
	            }
		 }
	 }
	
	public class InnerHandler extends Handler {
	       WeakReference< Review_Tab1 > mMainAct;
	       
	       InnerHandler( Review_Tab1 mMainActRef ) {
	           mMainAct = new WeakReference< Review_Tab1 > ( mMainActRef );
	       }
	     
	       public void handleMessage( Message msg ) {
	       	Review_Tab1 theMain = mMainAct.get();
	       	ProgressBar progress = (ProgressBar)view.findViewById(R.id.tab1progressbar);
	       	
	           switch( msg.arg1 ) {
	           case 0: // 다이얼로그 만들기
	        	   progress.setVisibility(View.VISIBLE);
	        	   rvName.setEnabled(false);
	        	   rvLocation.setEnabled(false);
	        	   rvComment.setEnabled(false);
	        	   spinner.setEnabled(false);
	        	          	 
	               break;

	           case 1: // 다이얼로그 끝내기
	        	   progress.setVisibility(View.INVISIBLE);
	        	   rvName.setEnabled(true);
	        	   rvLocation.setEnabled(true);
	        	   rvComment.setEnabled(true);
	        	   spinner.setEnabled(true);
	           
	        	   Review_Tab2 rvTab2 = new Review_Tab2();
	        	   rvTab2.refreshReview();
	               break;
	               
	           default:
	               break;
	           }
	       }
	}
		 
	
	
	
}
