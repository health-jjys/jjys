package com.yc.health;

import java.util.ArrayList;
import java.util.List;

import org.kymjs.kjframe.KJActivity;
import org.kymjs.kjframe.ui.BindView;

import com.yc.health.adapter.LikeHealthGridViewAdapter;
import com.yc.health.fragment.PersonalPopupWindow;
import com.yc.health.http.HttpLikeHealthRequest;
import com.yc.health.manager.ActivityManager;
import com.yc.health.model.KnowledgeModel;
import com.yc.health.util.ListUtils;
import com.yc.health.util.Method;
import com.yc.health.widget.GridCommodity;

import android.annotation.SuppressLint;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class LikeHelathActivity extends KJActivity implements OnGestureListener{

	@BindView(id = R.id.likehealth_back, click = true)
	private ImageView backBtn;
	@BindView(id = R.id.likehealth_knowledge_btn, click = true)
	private Button knowledgeBtn;
	@BindView(id = R.id.likehealth_video_btn, click = true)
	private Button videoBtn;
	@BindView(id = R.id.likehealth_btn_selected, click = true)
	private ImageView btnSelected;
	
	@BindView(id = R.id.likehealth_knowledge_title, click = true)
	private ImageView titleImg;
	@BindView(id = R.id.likehealth_knowledge_text)
	private TextView recommend;
	@BindView(id = R.id.likehealth_knowledge, click = true)
	private GridCommodity knowledgeGrid;
	
	private int curBtn = 1;//当前状态为知识还是视频，1为知识，2为视频
	private int windowsWidth;
	
	private PersonalPopupWindow menuWindow = null;
	private GestureDetector gestureDetector;
	
	private List<KnowledgeModel> list = new ArrayList<KnowledgeModel>();
	private LikeHealthGridViewAdapter adapter = null;
	private Handler mHandler = new Handler(){
		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			if ( msg.what == 1 ) {
				list = (List<KnowledgeModel>) msg.obj;
				adapter.setList(list);
				adapter.notifyDataSetChanged();
			}
			super.handleMessage(msg);
		}
	};
	
	@Override
	public void setRootView() {
		setContentView(R.layout.likehealth);
	}

	@Override
	public void initData() {
		super.initData();
		
		ActivityManager.getInstace().addActivity(aty);
		
		adapter = new LikeHealthGridViewAdapter(aty);
		adapter.setType("richText");
		
		HttpLikeHealthRequest request = new HttpLikeHealthRequest(aty,mHandler,1);
		request.getKnowledgeInit("richText");
		request.start();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void initWidget() {
		super.initWidget();
		
		gestureDetector = new GestureDetector(this); // 手势滑动
		
		// 分类下的彩色条
		gestureDetector = new GestureDetector(this); // 手势滑动
		windowsWidth = this.getWindowManager().getDefaultDisplay().getWidth();
		int offset = ListUtils.dip2px(aty, 50);
		btnSelected.getLayoutParams().width = windowsWidth/2-offset;
		
		//为文本画下划线
		recommend.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		recommend.getPaint().setAntiAlias(true);
		
		//健康知识推荐
		knowledgeGrid = (GridCommodity) this.findViewById(R.id.likehealth_knowledge);
		knowledgeGrid.setAdapter(adapter);
		knowledgeGrid.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				if ( curBtn == 1 ) {
					Bundle bundle = new Bundle();
					bundle.putString("path1", list.get(position).getImagePath1());
					bundle.putString("path2", list.get(position).getImagePath2());
					bundle.putString("path3", list.get(position).getImagePath3());
					bundle.putString("path4", list.get(position).getImagePath4());
					LikeHelathActivity.this.showActivity(aty, KnowledgeDetailActivity.class, bundle);
				} else {
					Bundle bundle = new Bundle();
					bundle.putInt("id", list.get(position).getKnowledgeID());
					bundle.putString("title", list.get(position).getTitle());
					bundle.putString("content", list.get(position).getContent());
					bundle.putString("path", list.get(position).getVideoUrlPath());
					LikeHelathActivity.this.showActivity(aty, VideoDetailActivity.class, bundle);
				}
			}
		});
	}

	@Override
	public void widgetClick(View v) {
		super.widgetClick(v);
		
		switch(v.getId()) {
		case R.id.likehealth_back:
			this.finish();
			break;
		case R.id.likehealth_knowledge_btn:
			if ( curBtn != 1) {
				curBtn = 1;
				moveAnimation(1);
				
				if ( curBtn == 1 ) {
					knowledgeBtn.setTextColor(this.getResources().getColor(R.color.ajk_selected));
					videoBtn.setTextColor(this.getResources().getColor(R.color.ajk));
				} else if ( curBtn == 2 ) {
					knowledgeBtn.setTextColor(this.getResources().getColor(R.color.ajk));
					videoBtn.setTextColor(this.getResources().getColor(R.color.ajk_selected));
				} 
				adapter.setType("richText");
			}
			break;
		case R.id.likehealth_video_btn:
			if ( curBtn != 2) {
				curBtn = 2;
				moveAnimation(-1);
				
				if ( curBtn == 1 ) {
					knowledgeBtn.setTextColor(this.getResources().getColor(R.color.ajk_selected));
					videoBtn.setTextColor(this.getResources().getColor(R.color.ajk));
				} else if ( curBtn == 2 ) {
					knowledgeBtn.setTextColor(this.getResources().getColor(R.color.ajk));
					videoBtn.setTextColor(this.getResources().getColor(R.color.ajk_selected));
				}
				adapter.setType("video");
			}
			break;
		}
	}
	
	//分类底部彩色条移动
	private void moveAnimation(final int num) {
		final int offset = ListUtils.dip2px(aty, 30);
		TranslateAnimation animation = new TranslateAnimation(0,
				-(windowsWidth/2-offset)*num, 0, 0);
		animation.setDuration(500);
		btnSelected.startAnimation(animation);
		animation.setAnimationListener(new AnimationListener() {
			@SuppressLint("NewApi")
			@Override
			public void onAnimationEnd(Animation arg0) {
				btnSelected.clearAnimation();
				btnSelected.setX(btnSelected.getX() - (windowsWidth/2-offset)*num);
			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
			}

			@Override
			public void onAnimationStart(Animation arg0) {
			}
		});
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
		if ( (e2.getX() - e1.getX()) > 120 && Math.abs(e2.getY() - e1.getY()) < 50 ) {
			if ( curBtn != 1) {
				moveAnimation(1);
				curBtn = 1;
				if ( curBtn == 1 ) {
					knowledgeBtn.setTextColor(this.getResources().getColor(R.color.ajk_selected));
					videoBtn.setTextColor(this.getResources().getColor(R.color.ajk));
				} else if ( curBtn == 2 ) {
					knowledgeBtn.setTextColor(this.getResources().getColor(R.color.ajk));
					videoBtn.setTextColor(this.getResources().getColor(R.color.ajk_selected));
				}
				adapter.setType("richText");
			}
		} else if ( (e2.getX() - e1.getX()) < -120 && Math.abs(e2.getY() - e1.getY()) < 50 ) {
			if ( curBtn != 2) {
				moveAnimation(-1);
				curBtn = 2;
				if ( curBtn == 1 ) {
					knowledgeBtn.setTextColor(this.getResources().getColor(R.color.ajk_selected));
					videoBtn.setTextColor(this.getResources().getColor(R.color.ajk));
				} else if ( curBtn == 2 ) {
					knowledgeBtn.setTextColor(this.getResources().getColor(R.color.ajk));
					videoBtn.setTextColor(this.getResources().getColor(R.color.ajk_selected));
				}
				adapter.setType("video");
			}
		} else if ( (e2.getX() - e1.getX()) > 50 && Math.abs(e2.getY() - e1.getY()) < 120 ) {
			if ( menuWindow == null ) {
				menuWindow = new PersonalPopupWindow(aty);//显示窗口  
	            menuWindow.showAtLocation(this.findViewById(R.id.likehealth), 
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
