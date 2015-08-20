package com.yc.health;

import java.util.ArrayList;
import java.util.List;

import ice.videoviewExt;

import org.kymjs.kjframe.KJActivity;
import org.kymjs.kjframe.ui.BindView;

import com.yc.health.adapter.LikeHealthGridViewAdapter;
import com.yc.health.fragment.PersonalPopupWindow;
import com.yc.health.http.HttpLikeHealthRequest;
import com.yc.health.http.HttpUserRequest2;
import com.yc.health.manager.ActivityManager;
import com.yc.health.model.KnowledgeModel;
import com.yc.health.util.Logutil;
import com.yc.health.util.MediaUtils;
import com.yc.health.util.Method;
import com.yc.health.widget.CustomToast;
import com.yc.health.widget.GridCommodity;
import com.yc.health.widget.mMediaController;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class VideoDetailActivity extends KJActivity implements OnGestureListener{

	@BindView(id=R.id.likehealth_video_parent)
	private RelativeLayout likehealth_video_parent;
	private videoviewExt vve;
	@BindView(id=R.id.btnFullScreen,click=true)
	private Button btnFullButton;
	@BindView(id=R.id.btnShare,click=true)
	private Button btnshare;
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
	
	private int mPositionWhenPaused = -1;
	private int windowsWidth = 0;
	private mMediaController mMediaController;
	
	private String sVideoUrl=null;
	private String content = null;
	private int knowID = -1;
	private boolean isCollected = false;
	private int collectionID = -1;
	
	private SharedPreferences userPreferences;
	private int userID = -1;
	
	private PersonalPopupWindow menuWindow = null;
	private GestureDetector gestureDetector;
	public static Handler mHandler=null;
	
	private List<KnowledgeModel> list = new ArrayList<KnowledgeModel>();
	private LikeHealthGridViewAdapter adapter = null;
	private Handler rceiveHandler = new Handler(){
		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			if ( msg.what == 1 ) {
				list = (List<KnowledgeModel>) msg.obj;
				adapter.setList(list);
				adapter.notifyDataSetChanged();
			} else if ( msg.what == 2 ) {
				isCollected = true;
				collectionID =  (Integer) msg.obj;
				if ( isCollected ) {
					collectBtn.setBackgroundResource(R.drawable.btn_sc_pre);
				} else {
					collectBtn.setBackgroundResource(R.drawable.btn_sc_nor);
				}
			} else if ( msg.what == 3) {
				isCollected = false;
			}
			super.handleMessage(msg);
		}
	};
	
	@Override
	public void setRootView() {
		setContentView(R.layout.likehealth_video);
	}

	@SuppressLint("WorldReadableFiles") 
	@SuppressWarnings("deprecation")
	@Override
	public void initData() {
		super.initData();
		
		ActivityManager.getInstace().addActivity(aty);
		
		adapter = new LikeHealthGridViewAdapter(aty);
		windowsWidth = this.getWindowManager().getDefaultDisplay().getWidth();
		
		Bundle bundle = this.getIntent().getExtras();
		knowID = bundle.getInt("id");
		sVideoUrl = bundle.getString("path");
		content = bundle.getString("content");
		
		userPreferences = getSharedPreferences("user", MODE_WORLD_READABLE);
		userID = userPreferences.getInt("userId", -1);
		
		HttpLikeHealthRequest request = new HttpLikeHealthRequest(aty,rceiveHandler,1);
		request.getKnowledgeInit("video");
		request.start();
		
		HttpUserRequest2 request2 = new HttpUserRequest2(aty,rceiveHandler,3);
		request2.isCollectionInit(userID, knowID, "HealthKnowledge");
		request2.start();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void initWidget() {
		super.initWidget();
		
		gestureDetector = new GestureDetector(this); // 手势滑动
		
		videoDes.setText(content);
		
        mMediaController = new com.yc.health.widget.mMediaController(this);
        vve=new videoviewExt(this);
        likehealth_video_parent.addView(vve, -1, -1);
        vve.setMediaController(mMediaController);
        vve.setBackgroundResource(R.color.transparent);
        vve.setVideoPath(sVideoUrl);
        vve.start();
        
		mHandler=new Handler(){
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case 1:
					btnFullButton.setVisibility(View.VISIBLE);
					btnshare.setVisibility(View.VISIBLE);
					btnshare.bringToFront();
					btnFullButton.bringToFront();
					break;
				case 0:
					btnFullButton.setVisibility(View.GONE);
					btnshare.setVisibility(View.GONE);
					break;
				default:
					break;
				}
			}
		};
		
		MediaUtils.mHandler=mHandler;
	    mHandler.sendEmptyMessage(0);
	    
	    likehealth_video_parent.setOnTouchListener(new OnTouchListener(){
			@Override
			public boolean onTouch(View arg0, MotionEvent ev) {
				 if ( ev.getY() < 100 ){
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
        
        vve.setOnCompletionListener(new OnCompletionListener(){
			@Override
			public void onCompletion(MediaPlayer arg0) {
				vve.setBackgroundResource(R.drawable.knowledge_title);
			}
        });
		
		//为文本画下划线
		videoDesTitle.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		videoDesTitle.getPaint().setAntiAlias(true);
		
		aboutRecommendTitle.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		aboutRecommendTitle.getPaint().setAntiAlias(true);
		
		//健康视频推荐
		videoGrid = (GridCommodity) this.findViewById(R.id.likehealth_videos);
		videoGrid.setAdapter(adapter);
		videoGrid.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				//播放不同的视频
				Bundle bundle = new Bundle();
				bundle.putInt("id", list.get(position).getKnowledgeID());
				bundle.putString("content", list.get(position).getContent());
				bundle.putString("path", list.get(position).getVideoUrlPath());
				VideoDetailActivity.this.showActivity(aty, VideoDetailActivity.class,bundle);
			}
		});
	}

	@Override
	public void widgetClick(View v) {
		switch (v.getId()) {
		case R.id.btnShare:
			Logutil.Log("share clicked");
			break;
		case R.id.btnFullScreen:
			Logutil.Log("FullScreen");
			MediaUtils.sVideoPath=sVideoUrl;
			this.showActivity(aty, VideoPlayer.class);
			break;
		case R.id.likehealth_video_collectbtn:
			if ( userID == -1 ) {
				Method method = new Method(aty);
				method.loginHint();
			} else {
				if ( isCollected ) {
					isCollected = false;
					collectBtn.setBackgroundResource(R.drawable.btn_sc_nor);
					CustomToast.showToast(aty, "已经移出收藏了哦！", 6000);
				} else {
					isCollected = true;
					collectBtn.setBackgroundResource(R.drawable.btn_sc_pre);
					CustomToast.showToast(aty, "已经加入收藏了哦！", 6000);
				}
			}
			break;
		default:
			break;
		}
		super.widgetClick(v);
	}
	
	@Override
	protected void onPause() {
		mPositionWhenPaused = vve.getCurrentPosition();
		vve.stopPlayback();
		
		if ( isCollected ) {
			HttpUserRequest2 request = new HttpUserRequest2(aty,rceiveHandler,1);
			request.addCollectionInit(userID, knowID, "HealthKnowledge");
			request.start();
		} else {
			if ( collectionID != -1 ) {
				HttpUserRequest2 request = new HttpUserRequest2(aty,rceiveHandler,4);
				request.deleteCollectionInit(collectionID);
				request.start();
			}
		}
		super.onPause();
	}

	@Override
	protected void onResume() {
		if(mPositionWhenPaused >= 0) {
			vve.seekTo(mPositionWhenPaused);
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
