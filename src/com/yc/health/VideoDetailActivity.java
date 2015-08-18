package com.yc.health;

import org.kymjs.kjframe.KJActivity;
import org.kymjs.kjframe.ui.BindView;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.graphics.Paint;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.yc.health.adapter.LikeHealthGridViewAdapter;
import com.yc.health.fragment.PersonalPopupWindow;
import com.yc.health.manager.ActivityManager;
import com.yc.health.util.Logutil;
import com.yc.health.util.MediaUtils;
import com.yc.health.util.Method;
import com.yc.health.widget.GridCommodity;

public class VideoDetailActivity extends KJActivity implements OnGestureListener{

	@BindView(id = R.id.likehealth_video_video, click = true)
	private WebView video;
	@BindView(id = R.id.likehealth_video_videoDes)
	private TextView videoDesTitle;
	@BindView(id = R.id.likehealth_video_aboutrecommend)
	private TextView aboutRecommendTitle;
	@BindView(id = R.id.likehealth_video_collectbtn, click = true)
	private ImageButton collectBtn;
	@BindView(id = R.id.likehealth_video_desc)
	private TextView videoDes;
	@BindView(id = R.id.likehealth_videos, click = true)
	private GridCommodity videoGrid;
	@BindView(id=R.id.fullscreen_custom_content)
	private FrameLayout mFullscreenContainer;
	@BindView(id=R.id.main_content)
	private FrameLayout mContentView;
	private View mCustomView = null;
	private LikeHealthGridViewAdapter knowledgeAdapter = null;
	
	public static String sVideUrl="http://movie.ks.js.cn/flv/other/2014/06/20-2.mp4";
	private int mPositionWhenPaused = -1;

	private int windowsWidth = 0;
	private MediaController mMediaController;
	
	private PersonalPopupWindow menuWindow = null;
	private GestureDetector gestureDetector;
	
	@Override
	public void setRootView() {
		setContentView(R.layout.likehealth_video);
		Logutil.Log("进入竖屏");
	}
	/**
	 * 设置全屏
	 */
	private void setFullScreen() {
		// 设置全屏的相关属性，获取当前的屏幕状态，然后设置全屏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// 全屏下的状态码：1098974464
		// 窗口下的状态吗：1098973440
	}

	/**
	 * 退出全屏
	 */
	private void quitFullScreen() {
		// 声明当前屏幕状态的参数并获取
		final WindowManager.LayoutParams attrs = getWindow().getAttributes();
		attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().setAttributes(attrs);
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
	}
	@SuppressWarnings("deprecation")
	@Override
	public void initData() {
		super.initData();
		
		ActivityManager.getInstace().addActivity(aty);
		
		knowledgeAdapter = new LikeHealthGridViewAdapter(aty);
		
		windowsWidth = this.getWindowManager().getDefaultDisplay().getWidth();
	}
	public static int getPhoneAndroidSDK() {
		// TODO Auto-generated method stub
		int version = 0;
		try {
			version = Integer.valueOf(android.os.Build.VERSION.SDK);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return version;

	}
	class MyWebViewClient extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			// TODO Auto-generated method stub
			view.loadUrl(url);
			return super.shouldOverrideUrlLoading(view, url);
		}

	}
	class MyWebChromeClient extends WebChromeClient {

		private CustomViewCallback mCustomViewCallback;

		@Override
		public void onShowCustomView(View view, CustomViewCallback callback) {
			// TODO Auto-generated method stub
			onShowCustomView(view, ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE, callback);
			super.onShowCustomView(view, callback);
			
		}
		@Override
		public void onShowCustomView(View view, int requestedOrientation,
				WebChromeClient.CustomViewCallback callback) {
			MediaUtils.sVideoPath=sVideUrl;
			VideoDetailActivity.this.showActivity(aty, VideoPlayer.class);
			return;
//			if (mCustomView != null) {
//				callback.onCustomViewHidden();
//				return;
//			}
//			mFullscreenContainer.addView(view);
//			mCustomView = view;
//			mCustomViewCallback = callback;
//			mContentView.setVisibility(View.INVISIBLE);
//			mFullscreenContainer.setVisibility(View.VISIBLE);
//			mFullscreenContainer.bringToFront();
//			setFullScreen();
		}
		@Override
		public void onHideCustomView() {
			mContentView.setVisibility(View.VISIBLE);
			if (mCustomView == null) {
				return;
			}
//			quitFullScreen();
//			mCustomView.setVisibility(View.GONE);
//			mFullscreenContainer.removeView(mCustomView);
//			mCustomView = null;
//			mFullscreenContainer.setVisibility(View.GONE);
//			try {
//				mCustomViewCallback.onCustomViewHidden();
//			} catch (Exception e) {
//			}
			// Show the content view.
			//setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}

	}
	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	@Override
	public void initWidget() {
		super.initWidget();
		gestureDetector = new GestureDetector(this); // 手势滑动
        mMediaController = new MediaController(this);
        WebSettings settings = video.getSettings();
		settings.setJavaScriptEnabled(true);
		settings.setCacheMode(settings.LOAD_NO_CACHE);//禁用缓存
		settings.setJavaScriptCanOpenWindowsAutomatically(true);
		settings.setPluginState(PluginState.ON);
		// settings.setPluginsEnabled(true);
		settings.setAllowFileAccess(true);
		settings.setLoadWithOverviewMode(true);
		video.setWebChromeClient(new MyWebChromeClient());
		video.setWebViewClient(new MyWebViewClient());
		if (getPhoneAndroidSDK() >= 14) {// 4.0 需打开硬件加速
			video.setLayerType(View.LAYER_TYPE_HARDWARE, null);
			getWindow().setFlags(0x1000000, 0x1000000);
		}
		
        String Html = "<html><head><link href='http://vjs.zencdn.net/4.12/video-js.css' rel='stylesheet'><script src='http://vjs.zencdn.net/4.12/video.js'></script><title>H5VideoTest</title></head><body><video src='AAA' autoplay='true' width='100%' height='90%' controls='true' poster='BBB' >当前浏览器不支持H5Video</video></body></html>"
        		.replace("AAA", sVideUrl);
        Logutil.Log(Html);
		video.loadDataWithBaseURL("file:///", Html, "text/html", "UTF8", null);
	
		//为文本画下划线
		videoDesTitle.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		videoDesTitle.getPaint().setAntiAlias(true);
		
		aboutRecommendTitle.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		aboutRecommendTitle.getPaint().setAntiAlias(true);
		
		//健康视频推荐
		videoGrid = (GridCommodity) this.findViewById(R.id.likehealth_videos);
		videoGrid.setAdapter(knowledgeAdapter);
		videoGrid.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				//播放不同的视频
			}
		});		
	}

	@Override
	public void widgetClick(View v) {
		super.widgetClick(v);
		Logutil.Log("vC="+v.getId());
		switch (v.getId()) {
		case R.id.likehealth_video_back:
			finish();
			break;

		default:
			break;
		}
	}
	
	@Override
	protected void onPause() {
		video.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		video.onResume();
		super.onResume();
		
	}

	@SuppressLint("NewApi")
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		video.loadUrl("about:black");
		if (getPhoneAndroidSDK() >= 14) {// 4.0 需打开硬件加速
			video.setLayerType(View.LAYER_TYPE_SOFTWARE,null);
		}
		super.onDestroy();
		
		
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return gestureDetector.onTouchEvent(event);
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		gestureDetector.onTouchEvent(ev);
		super.dispatchTouchEvent(ev);
		return false;
	}
	
	@Override
	public boolean onDown(MotionEvent arg0) {
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float arg2,
			float arg3) {
		if ( (e2.getX() - e1.getX()) > 80 && Math.abs(e2.getY() - e1.getY()) < 80 ) {
			if ( menuWindow == null ) {
				menuWindow = new PersonalPopupWindow(aty);//显示窗口  
	            menuWindow.showAtLocation(this.findViewById(R.id.likehealth_video), 
	            		Gravity.LEFT | Gravity.BOTTOM, 0, 0); 
	            menuWindow.setOnDismissListener(new OnDismissListener(){
					@Override
					public void onDismiss() {
						Method method = new Method(aty);
						method.backgroundAlpha(1f);
						menuWindow = null;
					}
	            });
			}
		}
		return true;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
//		if (KeyEvent.KEYCODE_BACK==keyCode) {
//			if (getRequestedOrientation()==ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
//				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//				return true;
//			}
//			return super.onKeyDown(keyCode, event); 
//		}
		return super.onKeyDown(keyCode, event);
	}
	@Override
	public void onLongPress(MotionEvent arg0) {
	}

	@Override
	public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent arg0) {
	}

	@Override
	public boolean onSingleTapUp(MotionEvent arg0) {
		return false;
	}
}
