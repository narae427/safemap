package com.project.safemap;

public class Review{
	int reviewID;
	int icon;
	String nickName;
	String location;
	String comment;
	String date;
	double lat;
	double lng;
	
	public Review(int _reviewID, int _icon, String _nickName, String _location, String _comment, String _date, double _lat, double _lng){
		reviewID = _reviewID;
		icon = _icon;
		nickName = _nickName;
		location = _location;
		comment = _comment;
		date = _date;
		lat = _lat;
		lng = _lng;
	}
}