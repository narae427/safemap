package com.project.safemap;

import java.io.File;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class loading extends Activity{
	
	static int intro = 0;
	static InnerHandler loading_MessageHandler;
	String sdPath = Environment.getExternalStorageDirectory().getAbsolutePath();
	File file = new File(sdPath + "/SafeMap/UserId.txt");
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.intro);
		
		loading_MessageHandler = new InnerHandler( loading.this );
		ProgressThread progressThread = new ProgressThread();
		progressThread.start();
				
		//gomain();
		
		if (Build.VERSION.SDK_INT >= 11) {
			new ReviewAsyncTask("loading").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR );
		} else {
			new ReviewAsyncTask("loading").execute();
		}
		
	}
	
	
	public class ProgressThread extends Thread {
		 public void run() {
	            Message msg;
	            
	            try {
	                msg = loading_MessageHandler.obtainMessage();
	                msg.arg1 = 0;
	                loading_MessageHandler.sendMessage( msg );

	                //Thread.sleep( 2000 );
	            }catch( Exception e ) {
	                msg = loading_MessageHandler.obtainMessage();
	                msg.arg1 = 99;
	                loading_MessageHandler.sendMessage( msg );
	            }
		 }
	 }
	
	public class InnerHandler extends Handler {
      WeakReference< loading > mMainAct;
      
      InnerHandler( loading mMainActRef ) {
          mMainAct = new WeakReference< loading > ( mMainActRef );
      }
    
      public void handleMessage( Message msg ) {
    	  loading theMain = mMainAct.get();
      	//ProgressBar progress = (ProgressBar)findViewById(R.id.loadingprogressbar);
      	TextView loadingMessage = (TextView)findViewById(R.id.loadingmsg);
      	loadingMessage.setSelected(true);

      	
          switch( msg.arg1 ) {
          case 0: 
       	   //progress.setVisibility(View.VISIBLE);
       	   loadingMessage.setVisibility(View.VISIBLE);
       	   
              break;

          case 1: 
       	   //progress.setVisibility(View.INVISIBLE);
       	   loadingMessage.setVisibility(View.INVISIBLE);
       	   if(!file.exists())
       		   startActivity(new Intent(loading.this, joinPopup.class));
       	   else
       		   finish();
       	   //startActivity(new Intent(loading.this, MainActivity.class));
       	   //gomain();
              break;
              
          default:
              break;
          }
      }
}
	




}
