package com.project.safemap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import static com.project.safemap.Review_Tab2.Position;

public class ReviewView extends Activity{
	static Context ReviewView_Context;
	static ReviewAsyncTask rat;
	private static final int DIALOG_1 = 1;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.review);	
		
		ReviewView_Context = this;
		ImageView Icon = (ImageView)findViewById(R.id.rvvicon);
		EditText nickName = (EditText)findViewById(R.id.rvvname);
		EditText Location = (EditText)findViewById(R.id.rvvlocation);
		EditText Comment = (EditText)findViewById(R.id.rvvcomment);
		
		//Location.setSelected(true);
		//Location.setFocusable(true);
		
		rat = new ReviewAsyncTask("ReviewView");
		nickName.setText(rat.getNickName(Position));
		Location.setText(rat.getLocation(Position));
		Comment.setText(rat.getComment(Position));
		
		int icon = rat.getIcon(Position);
		
		Icon.setImageResource(icon);
		
		Button deleteBtn = (Button)findViewById(R.id.deletebtn);
		deleteBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//showDialog(DIALOG_1);
				//alertDial();
				startActivity(new Intent(ReviewView.this,deletePopup.class));
				/*
				int reviewId = rat.getReviewId(Position);
				if (Build.VERSION.SDK_INT >= 11) {
					new ReviewAsyncTask("ReviewViewDelete",reviewId).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR );
				} else {
					new ReviewAsyncTask("ReviewViewDelete",reviewId).execute();
				}
				
				*/
			}
		});
		
		Button closeBtn = (Button)findViewById(R.id.closebtn);
		closeBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				
			}
		});
	}
	/*
	public Dialog onCreateDialog(int id){
		switch(id){
		case DIALOG_1:
			return alertDial();
		}
		return null;
		
	}
	*/
	public void alertDial(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		builder.setMessage("삭제하시겠습니까?").setCancelable(false)
			
		.setPositiveButton("예", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
				
			}
		})
		
		.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
    			
			}
		});
		
		
		AlertDialog alert = builder.create();
		alert.show();
		//return alert;
	}
		
}
