package com.yc.health.widget;

import android.content.Context;
import android.widget.LinearLayout;

import com.yc.health.util.Logutil;
import com.yc.health.util.MediaUtils;


public class mMediaController extends android.widget.MediaController{
	LinearLayout view=null;
	public mMediaController(Context context) {
		super(context);
		// TODO Auto-generated constructor stub 
	}



	@Override
	public void hide() {
		// TODO Auto-generated method stub
		super.hide();
		//Logutil.Log("hide");
		if (MediaUtils.mHandler!=null) {
			MediaUtils.mHandler.sendEmptyMessage(0);
		}
	}

	@Override
	public boolean isShowing() {
		// TODO Auto-generated method stub
		return super.isShowing();
	}

	@Override
	public void setMediaPlayer(MediaPlayerControl player) {
		// TODO Auto-generated method stub
		super.setMediaPlayer(player);
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		super.show();
		//Log.i("tag", "show");
	}

	@Override
	public void show(int timeout) {
		// TODO Auto-generated method stub
		super.show(timeout);
		//Logutil.Log("show");
		if (MediaUtils.mHandler!=null) {
			MediaUtils.mHandler.sendEmptyMessage(1);
		}
	}
	
}