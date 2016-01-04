package com.project.safemap;

import static com.project.safemap.MapActivity.mapPosition;
import static com.project.safemap.ReviewAdapter.raPosition;
import static com.project.safemap.MapActivity.MapActivity_MessageHandler;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class MapReviewView extends Activity{
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mapreview);	
		
		ImageView Icon = (ImageView)findViewById(R.id.maprvicon);
		EditText nickName = (EditText)findViewById(R.id.maprvName);
		EditText Location = (EditText)findViewById(R.id.maprvLocation);
		EditText Comment = (EditText)findViewById(R.id.maprvComment);
		
		//Location.setSelected(true);
		//Location.setFocusable(true);
		
		ReviewAsyncTask rat = new ReviewAsyncTask("ReviewView");
		nickName.setText(rat.getNickName(raPosition));
		Location.setText(rat.getLocation(raPosition));
		Comment.setText(rat.getComment(raPosition));
		
		int icon = rat.getIcon(raPosition);
		
		Icon.setImageResource(icon);
		
		
		
		Button closeBtn = (Button)findViewById(R.id.maprvclosebtn);
		closeBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				
			}
		});
	}
		
}
