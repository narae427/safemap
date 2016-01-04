package com.project.safemap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import static com.project.safemap.loading.intro;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends Activity {
	Button mapBtn, reviewBtn, sosBtn;
	

	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		
		Intent intent = getIntent();
		if(intent!=null)
		{
			intro = intent.getIntExtra("intro",intro);
		}
		
		if(intro<=0)
		{
			startActivity(new Intent(this, loading.class));
		}
		intro++;
		setContentView(R.layout.activity_main);
		
		mapBtn = (Button)findViewById(R.id.mapBtn);
		reviewBtn = (Button)findViewById(R.id.reviewBtn);
		sosBtn = (Button)findViewById(R.id.sosBtn);
		
		
		mapBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent mapIntent = new Intent(MainActivity.this, MapActivity.class);
				startActivity(mapIntent);
				
			}
		});
		
		reviewBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent reviewIntent = new Intent(MainActivity.this, ReviewActivity.class);
				startActivity(reviewIntent);
				
			}
		});
		
		sosBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent sosIntent = new Intent(MainActivity.this, SOSActivity.class);
				startActivity(sosIntent);
				
			}
		});
		
	}
	boolean mFlag = false;
	Handler mKillHandler = new Handler(){
		public void handleMessage(Message msg){
			if(msg.what ==0){
				mFlag = false;
			}
		}
	};

	public void onBackPressed(){
		
	}
	

}
