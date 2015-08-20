package com.yc.health;

import cn.jpush.android.api.JPushInterface;
import android.app.Application;

public class application extends Application {

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		JPushInterface.init(getApplicationContext());
		JPushInterface.setDebugMode(true);
	}
	
}
