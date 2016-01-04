package com.project.safemap;


import java.lang.ref.WeakReference;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;






import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import static com.project.safemap.ReviewAdapter.activity;


public class Review_Tab2 extends Fragment{
	private AsyncTask<String, String, String> mTask;
	Handler handler2;
	static Context Review_Tab2_Context;
	//ArrayList<Review> reviewArr;
	View view;
	
	static InnerHandler Review_Tab2_MessageHandler;
	View loadingLayout;
	static LayoutInflater rvInflater;
	static ViewGroup rvContainer;
	static Bundle rvBundle;
	static ListView Review_Tab2_ListView;
	static int Position;
	
	public Review_Tab2(){
	}
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState){
		setHasOptionsMenu(true);
		Review_Tab2_Context = getActivity();
		rvInflater = inflater;
		view = inflater.inflate(R.layout.review2, null);
		
		//reviewArr = new ArrayList<Review>();
		activity = "Review_Tab2";
		
		if (Build.VERSION.SDK_INT >= 11) {
			new ReviewAsyncTask("Review_Tab2").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR );
		} else {
			new ReviewAsyncTask("Review_Tab2").execute();
		}
		
	
	      
		Review_Tab2_MessageHandler = new InnerHandler( Review_Tab2.this );
		ProgressThread progressThread = new ProgressThread();
		progressThread.start();
		
		Review_Tab2_ListView = (ListView)view.findViewById(R.id.reviewListView);
		Review_Tab2_ListView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position,
					long id) {
				// TODO Auto-generated method stub
				Position = position;
				startActivity(new Intent(getActivity(),ReviewView.class));
				
			}
			
		});
		
		
		return view;
	}
	public void refreshReview(){
		if (Build.VERSION.SDK_INT >= 11) {
			new ReviewAsyncTask("Review_Tab2").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR );
		} else {
			new ReviewAsyncTask("Review_Tab2").execute();
		}
		ProgressThread progressThread = new ProgressThread();
		progressThread.start();
		
		
	}
	public class ProgressThread extends Thread {
		 public void run() {
	            Message msg;
	            
	            try {
	                msg = Review_Tab2_MessageHandler.obtainMessage();
	                msg.arg1 = 0;
	                Review_Tab2_MessageHandler.sendMessage( msg );

	                //Thread.sleep( 2000 );
	            }catch( Exception e ) {
	                msg = Review_Tab2_MessageHandler.obtainMessage();
	                msg.arg1 = 99;
	                Review_Tab2_MessageHandler.sendMessage( msg );
	            }
		 }
	 }
	
	public class InnerHandler extends Handler {
       WeakReference< Review_Tab2 > mMainAct;
       
       InnerHandler( Review_Tab2 mMainActRef ) {
           mMainAct = new WeakReference< Review_Tab2 > ( mMainActRef );
       }
     
       public void handleMessage( Message msg ) {
       	Review_Tab2 theMain = mMainAct.get();
       	ProgressBar progress = (ProgressBar)view.findViewById(R.id.progressBar1);
       	
           switch( msg.arg1 ) {
           case 0: // 다이얼로그 만들기
        	   progress.setVisibility(View.VISIBLE);
        	          	 
               break;

           case 1: // 다이얼로그 끝내기
        	   progress.setVisibility(View.INVISIBLE);
           
               break;
               
           default:
               break;
           }
       }
}
	 

	
	public void onPrepareOptionsMenu(Menu menu){
	}
	
	public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getItemId()){
		case android.R.id.home:
			Intent homeIntent = new Intent(getActivity(), MainActivity.class);
			homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(homeIntent);			
			break;
		default:
			return super.onOptionsItemSelected(item);
		}
		return true;
	}

}
