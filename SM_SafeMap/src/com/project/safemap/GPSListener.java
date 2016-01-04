package com.project.safemap;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.Toast;


public class GPSListener implements LocationListener {
		static Double lat=0.00;
		static Double lng=0.00;

	   @Override
	   public void onLocationChanged(Location location) {
	    // TODO Auto-generated method stub
	  
	    lat = location.getLatitude();
	    lng = location.getLongitude();
	   
	    String msg = "Latitude : " + lat + "\nLongitude : " + lng;
	  //  Log.i("GPSListener", msg);
	   
	   // Toast.makeText(getActivity(), msg, 2000).show();
	   
	   }

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}



	 
	 }
