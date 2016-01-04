package com.project.safemap;

import java.lang.ref.WeakReference;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import static com.project.safemap.Review_Tab2.Review_Tab2_MessageHandler;
import static com.project.safemap.Review_Tab2.Review_Tab2_ListView;
import static com.project.safemap.Review_Tab2.Review_Tab2_Context;
import static com.project.safemap.Review_Tab1.rvName;
import static com.project.safemap.Review_Tab1.rvLocation;
import static com.project.safemap.Review_Tab1.rvComment;
import static com.project.safemap.Review_Tab1.Review_Tab1_MessageHandler;
import static com.project.safemap.loading.loading_MessageHandler;
import static com.project.safemap.Review_Tab1.UserNickName;
import static com.project.safemap.MapActivity.MapActivity_Context;
import static com.project.safemap.MapActivity.MapActivity_ListView;
import static com.project.safemap.MapActivity.MapActivity_MessageHandler;
import static com.project.safemap.joinPopup.strEmail;
import static com.project.safemap.joinPopup.strNickName;
import static com.project.safemap.joinPopup.joinPopup_MessageHandler;


public class ReviewAsyncTask extends AsyncTask<String, Void, String>{
	static ArrayList<Emergency> policeArr = new ArrayList<Emergency>();;
	static ArrayList<Review> reviewArr;
	static Connection conn = null; 
	static Statement stmt;
	String TabName;
	int ReviewID;
	String currentDateTime;
	boolean isDuplicated = false;
	
	public ReviewAsyncTask(String tab){
		TabName = tab;
	}
	public ReviewAsyncTask(String tab, int id){
		TabName = tab;
		ReviewID = id;
	}
	protected void onPreExecute() {	 
	}
	protected String doInBackground(String... params) {

		//policeArr = new ArrayList<Emergency>();
		reviewArr = new ArrayList<Review>();		
		
		if(this.isCancelled()) {
			this.cancel(true);	 
		}
		try {
			while(conn==null){
				if(conn!=null)
					break;
				else{
					Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
					Log.i("Connection","**MSSQL driver load");
					conn = DriverManager.getConnection("jdbc:jtds:sqlserver://192.168.43.154:1433/SAFEMAP;encrypt=fasle;instance=SQLEXPRESS;","sa","74128526");
					Log.i("Connection","**MSSQL open");
				
				stmt = conn.createStatement();
			}
			}
				
		}catch (Exception e){
	        Log.w("***Error connection","" + e.getMessage());
		}
		
		if(TabName.equals("Review_Tab1")){	
			try {
				Review_Tab1 rvTab1 = new Review_Tab1();
				currentDateTime = getDateTime();
				stmt.executeUpdate("insert into review (nickname, date, location, comment, icon, lat, lng) "
						+ "values (\'"
						+rvTab1.getName()
						+"\',\'"+currentDateTime+"\',\'"
						+rvTab1.getLocation()+"\',\'"
						+rvTab1.getComment()+"\',"
						+rvTab1.getIcon()+","
						+rvTab1.getLat()+","
						+rvTab1.getLng()+")");
				
			}catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			}
			
			Message msg;
			msg = Review_Tab1_MessageHandler.obtainMessage();
			msg.arg1 = 1;
			Review_Tab1_MessageHandler.sendMessage( msg );
			
		}
		else if(TabName.equals("Review_Tab2")){	
			try {
				ResultSet reset = stmt.executeQuery("select * from review where nickname = \'"+UserNickName+"\' order by reviewID desc");
				
				while(reset.next()){
						int icon = R.drawable.ic_launcher;
						if(reset.getInt(6)==0){
						icon = R.drawable.yellow;
						}else if(reset.getInt(6)==1){
							icon = R.drawable.orange;
						}else if(reset.getInt(6)==2){
							icon = R.drawable.red;
						}    	   
						reviewArr.add(new Review(reset.getInt(1),icon,reset.getString(2),reset.getString(4),reset.getString(5),reset.getString(3),reset.getDouble(7),reset.getDouble(8)));
				}
			}catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			}
			
			Message msg;
			msg = Review_Tab2_MessageHandler.obtainMessage();
			msg.arg1 = 1;
			Review_Tab2_MessageHandler.sendMessage( msg );
		
		}
		else if(TabName.equals("MapActivity")){
			try {
				
				ResultSet reset = stmt.executeQuery("select * from review order by reviewID desc");
				
				while(reset.next()){
					int icon = R.drawable.ic_launcher;
					if(reset.getInt(6)==0){
					icon = R.drawable.yellow;
					}else if(reset.getInt(6)==1){
						icon = R.drawable.orange;
					}else if(reset.getInt(6)==2){
						icon = R.drawable.red;
					}    	   
					reviewArr.add(new Review(reset.getInt(1),icon,reset.getString(2),reset.getString(4),reset.getString(5),reset.getString(3),reset.getDouble(7),reset.getDouble(8)));
				}

				ResultSet reset2 = stmt.executeQuery("select * from police");
				
				while(reset2.next()){
					
					policeArr.add(new Emergency(reset2.getString(1), reset2.getString(2), reset2.getDouble(3), reset2.getDouble(4)));
					//Log.i("mytag", "police");
				}
				
			}catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			}
			
			Message msg;
			msg = MapActivity_MessageHandler.obtainMessage();
			msg.arg1 = 1;
			MapActivity_MessageHandler.sendMessage( msg );
		
		}
		
		else if(TabName.equals("ReviewViewDelete")){
			try {
				stmt.executeUpdate("delete from review where reviewID="+ReviewID);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(TabName.equals("loading")){
			Message msg;
			msg = loading_MessageHandler.obtainMessage();
			msg.arg1 = 1;
			loading_MessageHandler.sendMessage( msg );
		}
		else if(TabName.equals("Join")){
			try {
				
				ResultSet reset = stmt.executeQuery("select * from [user]");
				
				while(reset.next()){
					if(reset.getString(2).equals(strNickName)){
						isDuplicated = true;
					}
				}
				
				if(isDuplicated == false){
					
					stmt.executeUpdate("insert into [user] (email, nickname) "
							+ "values (\'"
							+strEmail
							+"\',\'"+strNickName+"\')");
							
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		else if(TabName.equals("Cluster")){
			Log.i("mytag", "cluster!!!");
			try {
				ResultSet reset = stmt.executeQuery("select * from review order by reviewID desc");
				
				while(reset.next()){
						int icon = R.drawable.ic_launcher;
						if(reset.getInt(6)==0){
						icon = R.drawable.yellow;
						}else if(reset.getInt(6)==1){
							icon = R.drawable.orange;
						}else if(reset.getInt(6)==2){
							icon = R.drawable.red;
						}    	   
						reviewArr.add(new Review(reset.getInt(1),icon,reset.getString(2),reset.getString(4),reset.getString(5),reset.getString(3),reset.getDouble(7),reset.getDouble(8)));
						
				}
				Log.i("mytag", String.valueOf(reviewArr.size()));
			}catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			}
		}
		
		return null;
	}
	
	public String getDateTime(){
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
		
		String currentDT = stringDate + " " + stringAmpm + " " + stringTime + " ";
		return currentDT;
	}
	
	
	protected void onProgressUpdate(Void... values){
	}
    protected void onPostExecute(String result) {
    	if(TabName.equals("Review_Tab1")){	
    		rvComment.setText("");
    	}
    	else if(TabName.equals("Review_Tab2")){	
    		ReviewAdapter reviewAdapter = new ReviewAdapter(Review_Tab2_Context,reviewArr);
    		Review_Tab2_ListView.setAdapter(reviewAdapter);
    	}
    	else if(TabName.equals("MapActivity")){

    		//Log.i("mytag", "markReview");
    		
    		ReviewAdapter reviewAdapter = new ReviewAdapter(MapActivity_Context,reviewArr);
    		MapActivity_ListView.setAdapter(reviewAdapter);
    		//MapActivity_ListView2.setAdapter(reviewAdapter);

    		//MapActivity ma = new MapActivity();
    		//ma.markReviews(reviewArr);
    	}else if(TabName.equals("ReviewViewDelete")){
    		((ReviewView)ReviewView.ReviewView_Context).finish();
    		
			Review_Tab2 rvTab2 = new Review_Tab2();
			rvTab2.refreshReview();
    	}
    	else if(TabName.equals("Join")){
    		if(isDuplicated == false){
    			Message msg;
	    		msg = joinPopup_MessageHandler.obtainMessage();
	            msg.arg1 = 1;
	            joinPopup_MessageHandler.sendMessage( msg );
    		}else if(isDuplicated == true){
    			Message msg;
	    		msg = joinPopup_MessageHandler.obtainMessage();
	            msg.arg1 = 2;
	            joinPopup_MessageHandler.sendMessage( msg );
    		}
    	}
    	else if(TabName.equals("Cluster")){
    		
    		Cluster cc = new Cluster();
    		cc.setMarkArray();
    		Log.i("mytag", "ReviewAsyncTask");
    		//MapActivity map = new MapActivity();
    		//map.setMarkClustered();
    		Message msg;
    		msg = MapActivity_MessageHandler.obtainMessage();
            msg.arg1 = 2;
            MapActivity_MessageHandler.sendMessage( msg );
    	}
    	
    }
    protected void onCancelled() {
        super.onCancelled();
    }
    public String getNickName(int position){
    	return reviewArr.get(position).nickName;
    }
    public String getLocation(int position){
    	return reviewArr.get(position).location;
    }
    public String getComment(int position){
    	return reviewArr.get(position).comment;
    }
    public int getIcon(int position){
    	return reviewArr.get(position).icon;
    }
    public int getReviewId(int position){
    	return reviewArr.get(position).reviewID;
    }
    public double getLat(int position){
    	return reviewArr.get(position).lat;
    }
    public double getLng(int position){
    	return reviewArr.get(position).lng;
    }
    
    public void deleteReview(int position){
    	try {
			stmt.executeUpdate("delete from review where reviewID="+reviewArr.get(position).reviewID);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}