package com.yc.health;

import org.kymjs.kjframe.KJActivity;
import org.kymjs.kjframe.ui.BindView;

import com.yc.health.adapter.LikeHealthGridViewAdapter;
import com.yc.health.fragment.PersonalPopupWindow;
import com.yc.health.manager.ActivityManager;
import com.yc.health.util.Method;
import com.yc.health.widget.GridCommodity;

import android.content.Intent;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Environment;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.MediaController;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.VideoView;

public class VideoDetailActivity extends KJActivity implements OnGestureListener{

	@BindView(id = R.id.likehealth_video_video, click = true)
	private VideoView video;
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
	
	private LikeHealthGridViewAdapter knowledgeAdapter = null;
	
	private Uri mUri;
	private int mPositionWhenPaused = -1;

	private int windowsWidth = 0;
	private MediaController mMediaController;
	
	private PersonalPopupWindow menuWindow = null;
	private GestureDetector gestureDetector;
	
	@Override
	public void setRootView() {
		setContentView(R.layout.likehealth_video);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void initData() {
		super.initData();
		
		ActivityManager.getInstace().addActivity(aty);
		
		knowledgeAdapter = new LikeHealthGridViewAdapter(aty);
		
		windowsWidth = this.getWindowManager().getDefaultDisplay().getWidth();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void initWidget() {
		super.initWidget();
		
		gestureDetector = new GestureDetector(this); // 手势滑动
		
		mUri = Uri.parse(Environment.getExternalStorageDirectory() + "/3.mp4");
        mMediaController = new MediaController(this);
        mMediaController.setVisibility(View.VISIBLE);
        video = (VideoView) this.findViewById(R.id.likehealth_video_video);
        video.setMediaController(mMediaController);
        mMediaController.setAnchorView(video);
        video.setBackgroundResource(R.color.transparent);
		video.setVideoURI(mUri);
        video.start();
        video.setOnTouchListener(new OnTouchListener(){
			@Override
			public boolean onTouch(View arg0, MotionEvent ev) {
				if ( ev.getY() > 100 ) {
//					video.setBackgroundResource(R.color.transparent);
//					video.setVideoURI(mUri);
//			        video.start();
					return true;
				} else if ( ev.getY() < 100 ){
					if ( ev.getX() < 100 ) {
						VideoDetailActivity.this.finish();
					} else if ( ev.getX() > windowsWidth - 100 ) {
						Intent shareInt=new Intent(Intent.ACTION_SEND);
						shareInt.setType("text/plain");   
						shareInt.putExtra(Intent.EXTRA_SUBJECT, "分享");   
						shareInt.putExtra(Intent.EXTRA_TEXT, "快下载郡健养生吧，带给你健康的人生~~");    
						shareInt.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
						startActivity(shareInt);
					}
					return false;
				}
				return false;
			}
        });
        
        video.setOnCompletionListener(new OnCompletionListener(){
			@Override
			public void onCompletion(MediaPlayer arg0) {
				video.setBackgroundResource(R.drawable.knowledge_title);
			}
        });
		
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
	}
	
	@Override
	protected void onPause() {
		mPositionWhenPaused = video.getCurrentPosition();
		video.stopPlayback();
		super.onPause();
	}

	@Override
	protected void onResume() {
		if(mPositionWhenPaused >= 0) {
			video.seekTo(mPositionWhenPaused);
	       mPositionWhenPaused = -1;
	    }
		super.onResume();
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
