package com.project.safemap;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import static com.project.safemap.loading.intro;

public class SOS_Tab2 extends Fragment{
	EditText mET, pnET;
	Button saveBtn;
	static String PhoneNumber="", SmsMessage="";
	String sdPath = Environment.getExternalStorageDirectory().getAbsolutePath();
	File file = new File(sdPath + "/SafeMap/SosSms.txt");
	
		
	public SOS_Tab2(){
	}
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState){
		setHasOptionsMenu(true);
		View view = inflater.inflate(R.layout.sos2, null);
		mET = (EditText)view.findViewById(R.id.message);
		pnET = (EditText)view.findViewById(R.id.phoneNumber);
		
		if(file.exists())
			setSMS();
		
		saveBtn = (Button)view.findViewById(R.id.saveBtn);
		saveBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Toast.makeText(getActivity(), "저장되었습니다.", Toast.LENGTH_SHORT).show();
				
				mET.setEnabled(false);
				pnET.setEnabled(false);
				saveBtn.setEnabled(false);
				
				PhoneNumber = pnET.getText().toString();
				SmsMessage = mET.getText().toString();
				

				//String sdPath = Environment.getExternalStorageDirectory().getAbsolutePath();
				File dir = new File(sdPath,  "SafeMap");
				dir.mkdir();
				File file = new File(dir, "SosSms.txt");
				FileOutputStream fos = null;
				String enter = "\n";
				
				try {
					fos = new FileOutputStream(file);
					fos.write(PhoneNumber.getBytes());
					fos.write(enter.getBytes());					
					fos.write(SmsMessage.getBytes());
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
		return view;
	}
	
	public void onPrepareOptionsMenu(Menu menu){
		menu.findItem(R.id.smsEdit).setVisible(true);
	}
	
	public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getItemId()){
		case android.R.id.home:
			Intent homeIntent = new Intent(getActivity(), MainActivity.class);
			homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(homeIntent);
			
			break;
		case R.id.smsEdit:
			mET.setEnabled(true);
			pnET.setEnabled(true);
			saveBtn.setEnabled(true);
			break;
		default:
			return super.onOptionsItemSelected(item);
		}
		return true;
	}
	
	public void setSMS(){
		PhoneNumber = "";
		SmsMessage = "";
		
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(sdPath + "/SafeMap/SosSms.txt");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Scanner s = new Scanner(fis);
		String str = null;
		int i=0;
		while(s.hasNext()){
			str = s.next();
			if(i==0){
				PhoneNumber = str;				
			}
			else{
				SmsMessage += (str + "\n");	
			}
			i++;
		}
		pnET.setText(PhoneNumber);
		mET.setText(SmsMessage);
	
		try {
			fis.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
	}

}
