package com.yc.health.manager;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;

public class ActivityManager extends Application{

	private List<Activity> activityList = new LinkedList<Activity>();
	private static ActivityManager instance;
	
	private ActivityManager(){}
	
	public static ActivityManager getInstace()
	{
		if ( null == instance )
		{
			instance = new ActivityManager();
		}
		return instance;
	}
	
	public void addActivity(Activity aty)
	{
		activityList.add(aty);
	}
	
	public void exit()
	{
		for ( int i = 0; i < activityList.size(); i++ )
		{
			activityList.get(i).finish();
		}
		System.exit(0);
	}
	
	public void enterHome()
	{
		for ( int i = 0; i < activityList.size(); i++ )
		{
			activityList.get(i).finish();
			activityList.remove(i);
		}
	}
	
	public boolean isEmpty() {
		if ( activityList.size() <= 1 ) {
			return true;
		} else {
			return false;
		}
	}
}