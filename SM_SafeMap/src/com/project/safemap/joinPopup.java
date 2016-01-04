package com.project.safemap;

import static com.project.safemap.MapActivity.MapActivity_MessageHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;

import com.project.safemap.MapActivity.InnerHandler;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.Toast;


public class joinPopup extends Activity{
	PopupWindow pw;
    View pv;
    LinearLayout linear;
    EditText email, nickName;
    String sdPath = Environment.getExternalStorageDirectory().getAbsolutePath();
	File file = new File(sdPath + "/SafeMap/UserId.txt");
	static InnerHandler joinPopup_MessageHandler;
	static String strEmail, strNickName;
	
    
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND, WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
		setContentView(R.layout.joinpopup);
		
		email = (EditText)findViewById(R.id.joineditText1);
		nickName = (EditText)findViewById(R.id.joineditText2);
		Button joinBtn = (Button)findViewById(R.id.joinBtn);
		
		joinPopup_MessageHandler = new InnerHandler( joinPopup.this );
		
		joinBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				strEmail = email.getText().toString();
				strNickName = nickName.getText().toString();
				
				Message msg;
	    		msg = joinPopup_MessageHandler.obtainMessage();
	            msg.arg1 = 0;
	            joinPopup_MessageHandler.sendMessage( msg );
				
				File dir = new File(sdPath,  "SafeMap");
				dir.mkdir();
				File file = new File(dir, "UserId.txt");
				FileOutputStream fos = null;
				String enter = "\n";
				
				try {
					fos = new FileOutputStream(file);
					fos.write(strEmail.getBytes());
					fos.write(enter.getBytes());					
					fos.write(strNickName.getBytes());
					fos.close();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}
			
		});
	}
		
	public class InnerHandler extends Handler {
	      WeakReference< joinPopup > mMainAct;
	      
	      InnerHandler( joinPopup mMainActRef ) {
	          mMainAct = new WeakReference< joinPopup > ( mMainActRef );
	      }
	    
	      public void handleMessage( Message msg ) {
	    	  joinPopup theMain = mMainAct.get();
	      	//ProgressBar progress = (ProgressBar)findViewById(R.id.mapProgressbar);
	      	
	          switch( msg.arg1 ) {
	          case 0: // 다이얼로그 만들기
	        	  if (Build.VERSION.SDK_INT >= 11) {
						new ReviewAsyncTask("Join").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR );
					} else {
						new ReviewAsyncTask("Join").execute();
					}
	       	       
	              break;

	          case 1: // 다이얼로그 끝내기
	        	  startActivity(new Intent(joinPopup.this, MainActivity.class));
	              break;
	              
	          case 2:
	        	  Toast.makeText(getApplicationContext(), "중복된 별명 입니다  ! ", Toast.LENGTH_SHORT).show();
	        	  email.setText("");
	        	  nickName.setText("");
	        	  break;
	         
	          default:
	              break;
	          }
	      }
	}
	

}
