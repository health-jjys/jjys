package com.yc.health;

import com.baidu.mapapi.SDKInitializer;

import android.app.Application;

public class ApplicationBaiduMap extends Application {

	@Override
	public void onCreate() {
		SDKInitializer.initialize(this);
		super.onCreate();
	}

}
