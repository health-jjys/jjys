package com.yc.health;

import ice.videoviewExt;

import org.kymjs.kjframe.KJActivity;

import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;

import com.yc.health.util.MediaUtils;

public class VideoPlayer extends KJActivity {
	private videoviewExt vve=null;
	private MediaController mcController=null;
	@Override
	public void setRootView() {
		// TODO Auto-generated method stub
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// 全屏下的状态码：1098974464
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		vve=new videoviewExt(this);
		setContentView(vve);
		vve.setVideoPath(MediaUtils.sVideoPath);
		mcController=new MediaController(this);
		vve.setMediaController(mcController);
		vve.start();
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return super.onKeyDown(keyCode, event);
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		vve.pause();
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		vve.start();
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		vve.stopPlayback();
	}

}
