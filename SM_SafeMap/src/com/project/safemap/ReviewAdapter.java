package com.project.safemap;

import java.util.ArrayList;



import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import static com.project.safemap.MapActivity.MapActivity_Context;
import static com.project.safemap.MapActivity.mapPosition;

public class ReviewAdapter extends BaseAdapter{
	static String activity = "";
	LayoutInflater mInflater;
	ArrayList<Review> reviewArr;
	Context mContext;
	static int raPosition;
	
	
	public ReviewAdapter(Context context, ArrayList<Review> _reviewArr){
		try{
		mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		}catch(Exception e){}
		
		reviewArr = _reviewArr;
		//Log.i("mytag", reviewArr.get(9).comment);
		mContext = context;
	}
	

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return reviewArr.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return reviewArr.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	
	public class ViewHolder{
		ImageView icon;
		TextView txt1;
		TextView txt2;
		TextView txt3;
		TextView txt4;
		Button reviewBtn;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder holder;
		if(convertView == null){
			holder = new ViewHolder();
			int res = R.layout.review_list;
			convertView = mInflater.inflate(res, parent, false);				
			
			holder.icon = (ImageView)convertView.findViewById(R.id.icon);
			holder.icon.setImageResource(reviewArr.get(position).icon);
			
			holder.txt1 = (TextView)convertView.findViewById(R.id.nickname);
			holder.txt1.setText(reviewArr.get(position).nickName);
			
			holder.txt2 = (TextView)convertView.findViewById(R.id.location);
			holder.txt2.setText(reviewArr.get(position).location);
			
			holder.txt3 = (TextView)convertView.findViewById(R.id.comment);
			holder.txt3.setText(reviewArr.get(position).comment);
			holder.txt3.setSelected(true);
			
			holder.txt4 = (TextView)convertView.findViewById(R.id.date);
			holder.txt4.setText(reviewArr.get(position).date);
			
			holder.reviewBtn = (Button)convertView.findViewById(R.id.reviewbutton);
			holder.reviewBtn.setFocusable(false);
			holder.reviewBtn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					//Toast.makeText(mContext, "reviewBtn", Toast.LENGTH_LONG).show();
					raPosition = (Integer)v.getTag();
					//raPosition = 0;
					//Toast.makeText(mContext, String.valueOf(v.getTag()), Toast.LENGTH_LONG).show();
					mContext.startActivity(new Intent(MapActivity_Context, MapReviewView.class));
				
				}
			});

			if(activity.equals("Review_Tab2")){
				holder.reviewBtn.setVisibility(View.GONE);
			}
			convertView.setTag(holder);
			convertView.setTag(R.id.reviewbutton,holder.reviewBtn);
		}else{
        	holder = (ViewHolder)convertView.getTag();
        }
		if(reviewArr.get(position) != null){
			holder.icon.setImageResource(reviewArr.get(position).icon);
			
			holder.txt1.setText(reviewArr.get(position).nickName);
			
			holder.txt2.setText(reviewArr.get(position).location);
			
			holder.txt3.setText(reviewArr.get(position).comment);
			holder.txt3.setSelected(true);
			
			holder.txt4.setText(reviewArr.get(position).date);
			
			holder.reviewBtn.setTag(position);
		}
		
		
		
		return convertView;
	}
	

}
