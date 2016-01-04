package com.project.safemap;


import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class SOS_Tab1 extends Fragment{
	Button ssStartBtn, ssFinishBtn;
	 
	 final static int MyNoti=0;


	
	public SOS_Tab1(){
	}
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState){
		setHasOptionsMenu(true);


		View view = inflater.inflate(R.layout.sos1, null);
		ssStartBtn = (Button)view.findViewById(R.id.ssStartBtn);
		ssStartBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				((SOSActivity)SOSActivity.mContext).intentService();
				Toast.makeText(getActivity(), "S.O.S Service가 시작되었습니다.", Toast.LENGTH_SHORT).show();
				
			}
		});
		
		ssFinishBtn = (Button)view.findViewById(R.id.ssFinishBtn);
		ssFinishBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				((SOSActivity)SOSActivity.mContext).stopService();
				Toast.makeText(getActivity(), "S.O.S Service가 종료되었습니다.", Toast.LENGTH_SHORT).show();
			}
		});
		return view;
	}
	
	public void onPrepareOptionsMenu(Menu menu){
		menu.findItem(R.id.smsEdit).setVisible(false);
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
