package com.project.safemap;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import static com.project.safemap.Review_Tab2.Position;
import static com.project.safemap.ReviewView.rat;

public class deletePopup extends Activity{
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND, WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
		setContentView(R.layout.deletepopup);
		
		Button yes = (Button)findViewById(R.id.yes);
		Button no = (Button)findViewById(R.id.no);
		
		yes.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				int reviewId = rat.getReviewId(Position);
				if (Build.VERSION.SDK_INT >= 11) {
					new ReviewAsyncTask("ReviewViewDelete",reviewId).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR );
				} else {
					new ReviewAsyncTask("ReviewViewDelete",reviewId).execute();
				}				
			}
		});
		
		no.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				
			}
		});
	}

}
