package com.yc.health.util;

import android.content.Context;
import android.widget.Toast;

public class Logutil {
	public static void Log(Object msg){
		android.util.Log.i("health", msg.toString());
	}
	public static void ShowToast(Context con,Object msg,boolean islong) {
		if (islong) {
			Toast.makeText(con, msg.toString(), Toast.LENGTH_LONG).show();	
		}else {
			Toast.makeText(con, msg.toString(), Toast.LENGTH_SHORT).show();
		}
		
	}
}
