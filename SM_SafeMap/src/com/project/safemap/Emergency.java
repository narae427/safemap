package com.project.safemap;

public class Emergency{
	String ename;
	String elocation;
	double elat;
	double elng;
	
	public Emergency(String _ename, String _elocation, double _elat, double _elng){
		ename = _ename;
		elocation = _elocation;
		elat = _elat;
		elng = _elng;
	}
}