package com.project.safemap;

import static com.project.safemap.ReviewAsyncTask.reviewArr;
//import static com.project.safemap.MapActivity.isClustered;
import java.util.ArrayList;

import com.google.android.gms.maps.model.LatLng;

import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

public class Cluster {
	static ArrayList<Review> markArray1;
	static ArrayList<ArrayList<Review>> markArray2;// = new ArrayList<ArrayList<Review>>();
	static ArrayList<Review> markArray3;
	static ArrayList<ArrayList<Review>> markArray4;// = new ArrayList<ArrayList<Review>>();
	
	static ArrayList<LatLng> latlngArray;
	static ArrayList<LatLng> citylatlngArray;
	

	public Cluster(){
		
	}
	public void getReviewArr(){
		if (Build.VERSION.SDK_INT >= 11) {
			new ReviewAsyncTask("Cluster").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR );
		} else {
			new ReviewAsyncTask("Cluster").execute();
		}
	}
	
	public void setMarkArray(){
		Log.i("mytag", "setMarkArray()");
		/*
		if(isClustered){
			Log.i("mytag", "isClustered");
			return;
		}
		*/
		markArray2 = new ArrayList<ArrayList<Review>>();
		markArray4 = new ArrayList<ArrayList<Review>>();
		//isClustered = true;
		latlngArray = new ArrayList<LatLng>();
		citylatlngArray = new ArrayList<LatLng>();
		String address = "";
		Review review;
		for(int reviewCount = 0; reviewCount < reviewArr.size(); reviewCount++){
			boolean isAdded = false;
			boolean isAddedCity = false;
			review = reviewArr.get(reviewCount);
			address = reviewArr.get(reviewCount).location;
			String[] str = address.split(" ");
			String strAddress = str[0]+str[1]+str[2];
			String strAddressCity = str[0]+str[1];
			
			for(int row = 0; row < 70; row++){
				
				try{
					String mAddress = markArray2.get(row).get(0).location;
					String[] mStr = mAddress.split(" ");
					String mStrAddress = mStr[0]+mStr[1]+mStr[2];
					String mStrAddressCity = mStr[0]+mStr[1];
										
					if(mStrAddressCity.equals(strAddressCity)){
						
						if(isAdded==false && mStrAddress.equals(strAddress)){
							markArray2.get(row).add(review);
							isAdded=true;
							//break;
						}
						//else{
						if(isAddedCity==false){
							markArray4.get(row).add(review);
							isAddedCity=true;
						}
						
						//break;
					}
					
					
					
				}catch(Exception e){
									
				}	
				
				
			}
			if(isAdded==false){
				markArray1 = new ArrayList<Review>();
				markArray1.add(review);
				markArray2.add(markArray1);
			}
			if(isAddedCity==false){
				markArray3 = new ArrayList<Review>();
				markArray3.add(review);
				markArray4.add(markArray3);
			}
			
			
		}
		Log.i("mytag", "reiewarray : "+String.valueOf(reviewArr.size()));
		//Log.i("mytag", markArray2.get(2).get(0).location);
		//Log.i("mytag", markArray2.get(0).get(1).location);
		//Log.i("mytag", markArray2.get(0).get(2).location);
		
		int ma2;
		int ma1;
				
		for(ma2 = 0; ma2 < markArray2.size(); ma2++)
		{
			double lat = 0;
			double lng = 0;
			double avLat = 0;
			double avLng = 0;
			
			for(ma1 = 0; ma1 < markArray2.get(ma2).size(); ma1++)
			{				
				lat += markArray2.get(ma2).get(ma1).lat;
				lng += markArray2.get(ma2).get(ma1).lng;
			}
			avLat = lat / markArray2.get(ma2).size();
			avLng = lng / markArray2.get(ma2).size();
			LatLng ll = new LatLng(avLat, avLng);
			latlngArray.add(ll);
			
			//Log.i("mytag", String.valueOf(avLat)+"   "+String.valueOf(avLng));
		}
		
		int ma4;
		int ma3;
				
		for(ma4 = 0; ma4 < markArray4.size(); ma4++)
		{
			double lat = 0;
			double lng = 0;
			double avLat = 0;
			double avLng = 0;
			
			for(ma3 = 0; ma3 < markArray4.get(ma4).size(); ma3++)
			{				
				lat += markArray4.get(ma4).get(ma3).lat;
				lng += markArray4.get(ma4).get(ma3).lng;
			}
			avLat = lat / markArray4.get(ma4).size();
			avLng = lng / markArray4.get(ma4).size();
			LatLng ll = new LatLng(avLat, avLng);
			citylatlngArray.add(ll);
			//Log.i("mytag", String.valueOf(avLat)+"   "+String.valueOf(avLng));
		}
		
		
	}
	

}
