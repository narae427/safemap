package com.project.safemap;

import java.io.IOException;
import java.util.List;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
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
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import static com.project.safemap.Review_Tab1.rvLocation;
import static com.project.safemap.GPSListener.lat;
import static com.project.safemap.GPSListener.lng;
import static com.project.safemap.Review_Tab1.Review_Tab1_lat;
import static com.project.safemap.Review_Tab1.Review_Tab1_lng;

public class Map extends FragmentActivity {

	
	private final String TAG ="Map";
	
	   private GoogleMap mmap;
	    private LocationManager locationManager;
	    private String provider;
	    String LocationInfo;
	    Button okBtn, searchBtn; 
	    EditText searchBox;
	    InputMethodManager imm;
	    ActionBar actionBar;
	    Menu staticMenu;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.map);
		
		actionBar = getActionBar();
		
		actionBar.setDisplayOptions(ActionBar.DISPLAY_USE_LOGO,ActionBar.DISPLAY_USE_LOGO);
		//actionBar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP,ActionBar.DISPLAY_HOME_AS_UP);
		//actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ff8c00")));
		actionBar.setHomeButtonEnabled(true);
		actionBar.setTitle("  ");
		actionBar.setStackedBackgroundDrawable(new ColorDrawable(Color.parseColor("#ff8c00")));	
		
		GooglePlayServicesUtil.isGooglePlayServicesAvailable(Map.this);
       	locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, true);
        
        if(provider==null){ 
         	AlertDialog.Builder dia = new AlertDialog.Builder(Map.this);
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
        }else{  
        	
    		GPSListener gpsListener = new GPSListener();
			locationManager.requestLocationUpdates(provider, 5000, 0, gpsListener);
        	setUpMapIfNeeded();
        }
        //mark();
        //////////////////////////////////////////////////////
        mmap = ((SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.fullmap)).getMap();
        mmap.getUiSettings().setRotateGesturesEnabled(false);
        mmap.getUiSettings().setTiltGesturesEnabled(false);

		mmap.setOnMapClickListener(new OnMapClickListener() {
			@Override
			public void onMapClick(LatLng latLng) {
				MarkerOptions markerOptions = new MarkerOptions();
				markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
				markerOptions.position(latLng);

				mmap.clear();
				mmap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
				mmap.addMarker(markerOptions);
			
				LocationInfo = getAddress(getApplicationContext(),latLng.latitude, latLng.longitude);
				Review_Tab1_lat = latLng.latitude;
				Review_Tab1_lng = latLng.longitude;
				rvLocation.setText(LocationInfo);
			}
		});
		
		
		mmap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        //////////////////////////////////////////////////////
        imm=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
       	
        
        okBtn = (Button)findViewById(R.id.fm_okBtn);
		okBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				
				
			}
		});
	}
	private void setUpMapIfNeeded() {
		if (mmap == null) {
			mmap = ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.fullmap)).getMap();
//			lat= location.getLatitude();
//			  lon= location.getLongitude();
			  Log.d(TAG, "GPS Position [" + String.valueOf(lat) + "," + String.valueOf(lng) + "]") ;
			  mmap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 16)) ;
			mmap.getUiSettings().setZoomControlsEnabled(false); 
			mmap.setMyLocationEnabled(true);
			mmap.getMyLocation();
			Location lastKnowLocation = locationManager.getLastKnownLocation(provider);  
			mmap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lastKnowLocation.getLatitude(),lastKnowLocation.getLongitude()), 16)) ;
		if (mmap != null) {
			setUpMap();
		}
		}
	}
	
	private void setUpMap() {
		mmap.setMyLocationEnabled(true);
		mmap.getMyLocation();
		
	}

	public String getAddress(Context context, double lat, double lng){
		String sLocationInfo="";
		try{
			Geocoder geo = new Geocoder(context);
			List<Address> addr = geo.getFromLocation(lat,lng, 1);
			if(addr!=null){
				Address address = addr.get(0);
				for(int i=0; i<=address.getMaxAddressLineIndex();i++){
					String addLine = address.getAddressLine(i);
					sLocationInfo += String.format("%s", addLine);
				}
				//Toast.makeText(context, sLocationInfo, Toast.LENGTH_SHORT).show();
			}
			
		}catch(Exception e){
			Toast.makeText(context, String.valueOf(lat), Toast.LENGTH_SHORT).show();
			Toast.makeText(context, "주소 조회 실패", Toast.LENGTH_SHORT).show();
			sLocationInfo = "주소 조회 실패";
			e.printStackTrace();
		}
		return sLocationInfo;
	}
	
	public void getLatLng(String cAddress){
		//String cAddress = searchBox.getText().toString();
		//String cAddress = "남성역";
		String result="";
		//double lat=0, lng=0;
		try{
			Geocoder geo = new Geocoder(getApplicationContext());
			List<Address> address = geo.getFromLocationName(cAddress, 1);
			if(address!=null){
				result = address.get(0).toString();
			}
			Review_Tab1_lat = address.get(0).getLatitude();
			Review_Tab1_lng = address.get(0).getLongitude();
			mmap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Review_Tab1_lat, Review_Tab1_lng), 16)) ;
			//Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
		}catch(Exception e){
			Toast.makeText(getApplicationContext(), "주소 검색 실패", Toast.LENGTH_SHORT).show();
		}
	}
	
	
	
	
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		staticMenu = menu;
		getMenuInflater().inflate(R.menu.map, menu);
		return true;
	}
	
	
	public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getItemId()){
		case android.R.id.home:
			Intent homeIntent = new Intent(Map.this, MainActivity.class);
			homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(homeIntent);	
			break;
		case R.id.search:
			View v = getLayoutInflater().inflate(R.layout.searchbar, null);
			actionBar.setCustomView(v,new ActionBar.LayoutParams(
							ActionBar.LayoutParams.WRAP_CONTENT,ActionBar.LayoutParams.MATCH_PARENT));
			actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
			actionBar.setDisplayHomeAsUpEnabled(false);
			actionBar.setDisplayShowHomeEnabled(true);
			actionBar.setDisplayUseLogoEnabled(true);
			staticMenu.findItem(R.id.search).setVisible(false);
			searchBox = (EditText)v.findViewById(R.id.searchEdit);
			searchBtn = (Button)v.findViewById(R.id.searchBtn);
			searchBtn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String cAddress = searchBox.getText().toString();
					getLatLng(cAddress);
					imm.hideSoftInputFromWindow(searchBox.getWindowToken(), 0);
					
					
				}
			});
			
			break;
		default:
			return super.onOptionsItemSelected(item);
		}
		return true;
	}
	
	
	public void onBackPressed(){
		
	}
	
}
