package com.project.safemap;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import static com.project.safemap.ReviewAsyncTask.reviewArr;
import static com.project.safemap.ReviewAsyncTask.policeArr;
import static com.project.safemap.ReviewAdapter.activity;
import static com.project.safemap.ReviewAdapter.raPosition;
import static com.project.safemap.Cluster.latlngArray;
import static com.project.safemap.Cluster.markArray2;
import static com.project.safemap.Cluster.citylatlngArray;
import static com.project.safemap.Cluster.markArray4;

public class MapActivity extends FragmentActivity{
	
	private final String TAG ="MapActivity";	
	  	private GoogleMap mmap = null;
	  	private LocationManager locationManager = null;
	  	private String provider = null;
	    Menu staticMenu;
	    ActionBar actionBar;
	    Button searchBtn, handle;
	    EditText searchBox;
	    TextView reviewTV;
	    RelativeLayout reviewTVRL, reviewRL;
	    SlidingDrawer drawer;
	    InputMethodManager imm;
	    static ListView MapActivity_ListView;
	    static Context MapActivity_Context;
	    static InnerHandler MapActivity_MessageHandler;
	    static int mapPosition;
	    boolean isItemSelected = false;
	    boolean isCameraChanged = false;
	    boolean clusterMarker = false;
	    static boolean isCity = false;
	    
	    
	   // Marker[] markers;
	    Marker mMarker;
	    ArrayList<Marker> markers = new ArrayList<Marker>();
	    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);	
		MapActivity_Context = this;
		clusterMarker = false;
		activity = "MapActivity";
		
		actionBar = getActionBar();
		
		actionBar.setDisplayOptions(ActionBar.DISPLAY_USE_LOGO,ActionBar.DISPLAY_USE_LOGO);
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ff8c00")));
		actionBar.setHomeButtonEnabled(true);
		actionBar.setTitle("  MAP");
	
		reviewTV = (TextView)findViewById(R.id.reviewTV);
		reviewTV.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/number.ttf"));
		reviewTV.setVisibility(View.VISIBLE);
		reviewTVRL = (RelativeLayout)findViewById(R.id.reviewTVRL);
		reviewTVRL.setVisibility(View.VISIBLE);
		
		reviewRL = (RelativeLayout)findViewById(R.id.selectedReview);
		MapActivity_ListView = (ListView)findViewById(R.id.mapListView);
		
		drawer = (SlidingDrawer)findViewById(R.id.slidingDrawer1);
		handle = (Button)findViewById(R.id.handle);
				
		drawer.setOnDrawerScrollListener(new SlidingDrawer.OnDrawerScrollListener() {
			
			
			@Override
			public void onScrollStarted() {
				// TODO Auto-generated method stub
				reviewRL.setVisibility(View.GONE);
				reviewTV.setVisibility(View.GONE);
				reviewTVRL.setVisibility(View.GONE);
				
			}
			
			@Override
			public void onScrollEnded() {
				// TODO Auto-generated method stub
				if(drawer.isMoving()==true){
					reviewTV.setVisibility(View.GONE);
					reviewTVRL.setVisibility(View.GONE);
					
				}
				
			}
			
			
		});
		
		drawer.setOnDrawerOpenListener(new OnDrawerOpenListener(){

			@Override
			public void onDrawerOpened() {
				// TODO Auto-generated method stub
				reviewRL.setVisibility(View.GONE);
				handle.setBackgroundResource(R.drawable.handledown);
				reviewTV.setVisibility(View.GONE);
				reviewTVRL.setVisibility(View.GONE);
				
			}
			
		});
		
		drawer.setOnDrawerCloseListener(new OnDrawerCloseListener(){

			@Override
			public void onDrawerClosed() {
				// TODO Auto-generated method stub
				if(isItemSelected == true){
					reviewRL.setVisibility(View.VISIBLE);
					reviewTV.setVisibility(View.INVISIBLE);
					reviewTVRL.setVisibility(View.INVISIBLE);
					//MapActivity_ListView2.setVisibility(View.VISIBLE);
				}else{
					reviewTV.setVisibility(View.VISIBLE);
					reviewTVRL.setVisibility(View.VISIBLE);
				}
				handle.setBackgroundResource(R.drawable.handle);
				
			}
			
		});
			
		
		MapActivity_ListView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position,
					long id) {
				isItemSelected = true;
				mapPosition = position;
				 drawer.animateClose();
				 mmap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(reviewArr.get(position).lat,reviewArr.get(position).lng), 16)) ;
	        	
	        	 int count=0;
	        	 int pos = 0;
	        	 while(true){
	        		 if(markers.get(count).getSnippet().equals(String.valueOf(reviewArr.get(position).reviewID))){
	        			 pos = count;
	        			 break;
	        		 }
	 				count++;
	 			}
	        	 
	        	 mMarker= markers.get(pos);
	        	 Handler mHandler = new Handler();
				 for (int i = 500; i < 10000 ; i+=500){
					 mHandler.postDelayed(mRunnable1, i);
					 i+=500;
					 mHandler.postDelayed(mRunnable2, i);
				 }
					
	        	 reviewTV.setVisibility(View.GONE);
        		 reviewTVRL.setVisibility(View.GONE);
             
            setReviewView(mapPosition);
			}
			
		});
		
		
		if (Build.VERSION.SDK_INT >= 11) {
			new ReviewAsyncTask("MapActivity").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR );
		} else {
			new ReviewAsyncTask("MapActivity").execute();
		}
		
		MapActivity_MessageHandler = new InnerHandler( MapActivity.this );
		ProgressThread progressThread = new ProgressThread();
		progressThread.start();
			

		imm=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		 
		GooglePlayServicesUtil.isGooglePlayServicesAvailable(MapActivity.this);
       	locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, true);
        
        if(provider==null){ 
         	AlertDialog.Builder dia = new AlertDialog.Builder(MapActivity.this);
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
        
        mmap.setOnMarkerClickListener(new OnMarkerClickListener(){
        	 @Override

             public boolean onMarkerClick(Marker marker) {
        		 if(clusterMarker==true || marker.getSnippet().equals("police")){
        			 Toast.makeText(MapActivity_Context, "return", Toast.LENGTH_SHORT).show();
        			 return false;
        		 }
        		
        		 reviewRL.setVisibility(View.VISIBLE);
        		 reviewTV.setVisibility(View.GONE);
        		 reviewTVRL.setVisibility(View.GONE);
        		        		 
        		 int reviewnumber = Integer.valueOf(marker.getSnippet());
        		 int position = 0;
        		 int i=0;
        		 while(true){
        			 if(reviewnumber==reviewArr.get(i).reviewID){
        				 position = i;
        				 break;
        			 }
        				 i++;
        		 }
        		 setReviewView(position);            
             
        		 return false;
        	 }
        });   
        
        mmap.setOnCameraChangeListener(new OnCameraChangeListener(){

			@Override
			public void onCameraChange(CameraPosition arg0) {
				// TODO Auto-generated method stub
				
				float zoom = 1.0f;
				zoom = mmap.getCameraPosition().zoom;
				
				if(zoom <= 10f){
					if(isCameraChanged==false){
						return;
					}
					isCity = true;
					isCameraChanged = false;
					clusterMarker = true;
					mmap.clear();
					if (Build.VERSION.SDK_INT >= 11) {
						new ReviewAsyncTask("Cluster").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR );
					} else {
						new ReviewAsyncTask("Cluster").execute();
					}
				
				}
				else if(zoom >10f && zoom <=13f){
					if(isCameraChanged==true){
						return;
					}
					isCity = false;
					isCameraChanged = true;
					clusterMarker = true;
					mmap.clear();
					if (Build.VERSION.SDK_INT >= 11) {
						new ReviewAsyncTask("Cluster").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR );
					} else {
						new ReviewAsyncTask("Cluster").execute();
					}
				}
			
				else if(zoom > 13f){
					if(isCameraChanged==false){
						return;
					}
					isCameraChanged = false;
					clusterMarker = false;
					mmap.clear();
					
					Message msg = MapActivity_MessageHandler.obtainMessage();
	                msg.arg1 = 1;
	                MapActivity_MessageHandler.sendMessage( msg );
	                
	              
				}
				
			}
        	
        });
        
        
        mmap.getUiSettings().setRotateGesturesEnabled(false);
        mmap.getUiSettings().setTiltGesturesEnabled(false);
	}

	
	 
	public void setReviewView(final int position){
		ImageView reviewIV = (ImageView)findViewById(R.id.sicon);        
        reviewIV.setImageResource(reviewArr.get(position).icon);
        TextView reviewt1 = (TextView)findViewById(R.id.snickname);
        reviewt1.setText(reviewArr.get(position).nickName);
        TextView reviewt2 = (TextView)findViewById(R.id.slocation);
        reviewt2.setText(reviewArr.get(position).location);
        TextView reviewt3 = (TextView)findViewById(R.id.scomment);
        reviewt3.setText(reviewArr.get(position).comment);
        TextView reviewt4 = (TextView)findViewById(R.id.sdate);
        reviewt4.setText(reviewArr.get(position).date);
        
        Button reviewButton = (Button)findViewById(R.id.sreviewbutton);
        reviewButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				raPosition = Integer.valueOf(position);
				startActivity(new Intent(MapActivity_Context, MapReviewView.class));
			}
		});
		
	}

	public void setUpMapIfNeeded() {
		if (mmap == null) {
			mmap = ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
			
			mmap.setMyLocationEnabled(true);
			mmap.getMyLocation();
			Location lastKnowLocation = locationManager.getLastKnownLocation(provider);  
			mmap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lastKnowLocation.getLatitude(),lastKnowLocation.getLongitude()), 16)) ;
			
		if (mmap != null) {
			setUpMap();
		}
		}
	}

	public void setUpMap() {
		mmap.setMyLocationEnabled(true);
		mmap.getMyLocation();
		
	}
	public void markPolice() {
		int i=0;
		while(i<policeArr.size()){
			LatLng latlng = new LatLng(policeArr.get(i).elat, policeArr.get(i).elng);
			
			MarkerOptions markerOptions = new MarkerOptions();
			markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.police));
			markerOptions.position(latlng);
			markerOptions.snippet("police");
			
			mmap.addMarker(markerOptions);
			mmap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
			
			i++;
		}

	}
	public void markReviews() {
			
		int i=0;
		markers = new ArrayList<Marker>();
		while(i<reviewArr.size()){
			LatLng latlng = new LatLng(reviewArr.get(i).lat, reviewArr.get(i).lng);
			
			MarkerOptions markerOptions = new MarkerOptions();
			if(reviewArr.get(i).icon==R.drawable.yellow)
				markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.yellow_marker));
			else if(reviewArr.get(i).icon==R.drawable.orange)
				markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.orange_marker));
			else if(reviewArr.get(i).icon==R.drawable.red)
				markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.red_marker));
			markerOptions.position(latlng);
			markerOptions.snippet(String.valueOf(reviewArr.get(i).reviewID));
			markers.add(mmap.addMarker(markerOptions));
			
			mmap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
			
			i++;
		}
		//Toast.makeText(MapActivity_Context, "markers are added"+String.valueOf(markers.size()), Toast.LENGTH_SHORT).show();
	}
	
	public void setMarkClustered(){
		Log.i("mytag", "setMarkClustered");
		View clusteredMarker = ((LayoutInflater)MapActivity_Context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.clusteredmarker,null);
		TextView numberofMarkers = (TextView)clusteredMarker.findViewById(R.id.numberofmarker);
		numberofMarkers.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/number.ttf"));
		ImageView clusteredImage = (ImageView)clusteredMarker.findViewById(R.id.clusteredimage);
		
		int i=0;
		while(i<latlngArray.size()){
			LatLng latlng = latlngArray.get(i);		
			numberofMarkers.setText(String.valueOf(markArray2.get(i).size()));
			if(markArray2.get(i).size()<=5)
				clusteredImage.setBackgroundResource(R.drawable.cmarker_yellow);
			else if(markArray2.get(i).size()<=10)
				clusteredImage.setBackgroundResource(R.drawable.cmarker_orange);
			else 
				clusteredImage.setBackgroundResource(R.drawable.cmarker_red);
			MarkerOptions markerOptions = new MarkerOptions();
			
			markerOptions.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(MapActivity_Context, clusteredMarker)));
		
			markerOptions.position(latlng);
			
			mmap.addMarker(markerOptions);
			mmap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
			
			i++;
		}

	}
	
	public void setMarkClusteredCity(){
		View clusteredMarker = ((LayoutInflater)MapActivity_Context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.clusteredmarker,null);
		TextView numberofMarkers = (TextView)clusteredMarker.findViewById(R.id.numberofmarker);
		numberofMarkers.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/number.ttf"));
		ImageView clusteredImage = (ImageView)clusteredMarker.findViewById(R.id.clusteredimage);
		
		int i=0;
		while(i<citylatlngArray.size()){
			LatLng latlng = citylatlngArray.get(i);		
			numberofMarkers.setText(String.valueOf(markArray4.get(i).size()));
			if(markArray4.get(i).size()<=5)
				clusteredImage.setBackgroundResource(R.drawable.cmarker_yellow);
			else if(markArray4.get(i).size()<=10)
				clusteredImage.setBackgroundResource(R.drawable.cmarker_orange);
			else
				clusteredImage.setBackgroundResource(R.drawable.cmarker_red);
			MarkerOptions markerOptions = new MarkerOptions();
			
			markerOptions.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(MapActivity_Context, clusteredMarker)));
		
			markerOptions.position(latlng);
			
			mmap.addMarker(markerOptions);
			mmap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
			
			i++;
		}

	}
	
	public Bitmap createDrawableFromView(Context context, View view){
		DisplayMetrics displayMetrics = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		view.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
		view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
		view.buildDrawingCache();
		Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
		
		Canvas canvas = new Canvas(bitmap);
		view.draw(canvas);
		return bitmap;
	}


	public void getLatLng(String cAddress){
		String result="";
		double lat=0, lng=0;
		try{
			Geocoder geo = new Geocoder(getApplicationContext());
			List<Address> address = geo.getFromLocationName(cAddress, 1);
			if(address!=null){
				result = address.get(0).toString();
			}
			lat = address.get(0).getLatitude();
			lng = address.get(0).getLongitude();
			mmap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 16)) ;
		}catch(Exception e){
			Toast.makeText(getApplicationContext(), "주소 검색 실패", Toast.LENGTH_SHORT).show();
		}
	}
	
	
	public boolean onCreateOptionsMenu(Menu menu) {
		staticMenu = menu;
		getMenuInflater().inflate(R.menu.map, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getItemId()){
		case android.R.id.home:
			Intent homeIntent = new Intent(MapActivity.this, MainActivity.class);
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
					if(drawer.isOpened())
						drawer.animateClose();
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
	
	public void viewMap(){
		ReviewAsyncTask rat = new ReviewAsyncTask("MapActivity");	
		mmap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(rat.getLat(mapPosition),rat.getLng(mapPosition)), 16)) ;
	}
	
	public class ProgressThread extends Thread {
		 public void run() {
	            Message msg;
	            
	            try {
	                msg = MapActivity_MessageHandler.obtainMessage();
	                msg.arg1 = 0;
	                MapActivity_MessageHandler.sendMessage( msg );

	                //Thread.sleep( 2000 );
	            }catch( Exception e ) {
	                msg = MapActivity_MessageHandler.obtainMessage();
	                msg.arg1 = 99;
	                MapActivity_MessageHandler.sendMessage( msg );
	            }
		 }
	 }
	
	public class InnerHandler extends Handler {
      WeakReference< MapActivity > mMainAct;
      
      InnerHandler( MapActivity mMainActRef ) {
          mMainAct = new WeakReference< MapActivity > ( mMainActRef );
      }
    
      public void handleMessage( Message msg ) {
    	MapActivity theMain = mMainAct.get();
      	ProgressBar progress = (ProgressBar)findViewById(R.id.mapProgressbar);
      	
          switch( msg.arg1 ) {
          case 0: // 다이얼로그 만들기
        	  progress.setVisibility(View.VISIBLE);
       	       
              break;

          case 1: // 다이얼로그 끝내기
        	  progress.setVisibility(View.INVISIBLE);
        	  markReviews();
        	  markPolice();
              break;
          case 2:
        	  if(isCity==true){
        		  setMarkClusteredCity();
        	  }
        	  else if(isCity==false){
        		  setMarkClustered();
        	  }
        	  
        	  break;
          
          default:
              break;
          }
      }
}
	private Runnable mRunnable1 = new Runnable(){

		@Override
		public void run() {
			// TODO Auto-generated method stub
			
			mMarker.setVisible(false);
		}
		
	};
	
	private Runnable mRunnable2 = new Runnable(){

		@Override
		public void run() {
			// TODO Auto-generated method stub
			mMarker.setVisible(true);
		}
		
	};
	
	public void intentReviewView(){
		startActivity(new Intent(MapActivity.this,MapReviewView.class));	
	}
	
	public void onBackPressed(){
		
	}



}
