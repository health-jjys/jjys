package com.yc.health;

import java.util.ArrayList;
import java.util.List;

import org.kymjs.kjframe.KJActivity;
import org.kymjs.kjframe.ui.BindView;

import com.yc.health.adapter.MyPagerAdapter;
import com.yc.health.fragment.PersonalPopupWindow;
import com.yc.health.http.HttpUserRequest2;
import com.yc.health.manager.ActivityManager;
import com.yc.health.util.Method;
import com.yc.health.widget.CustomToast;
import com.zwq.view.effect.DepthPageTransformer;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.webkit.WebView;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.PopupWindow.OnDismissListener;

public class KnowledgeDetailActivity extends KJActivity implements OnGestureListener{

	@BindView(id = R.id.knowledgedetail_viewPager, click = true)
	private ViewPager viewPager;
	@BindView(id = R.id.knowledgedetail_backicon, click = true)
	private ImageView backIcon;
	@BindView(id = R.id.knowledgedetail_collectbtn, click = true)
	private ImageView collectBtn;
	@BindView(id = R.id.knowledgedetail_backtext, click = true)
	private Button backText;
	@BindView(id = R.id.knowledgedetail_page)
	private TextView pageText;

	private PersonalPopupWindow menuWindow = null;
	private GestureDetector gestureDetector;
	
	private List<View> listViews = null; //viewpager的所有view
	private int currentPage = 0; //当前页面
	
	private int knowID = -1;
	private String imagePath1 = null;
	private String imagePath2 = null;
	private String imagePath3 = null;
	private String imagePath4 = null;
	private boolean isCollected = false;
	private int collectionID = -1;
	
	private SharedPreferences userPreferences;
	private int userID = -1;

	private Handler rceiveHandler = new Handler(){
		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			if ( msg.what == 2 ) {
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
		setContentView(R.layout.knowledge_detail);
	}

	@SuppressLint("WorldReadableFiles") 
	@SuppressWarnings("deprecation")
	@Override
	public void initData() {
		super.initData();
		
		ActivityManager.getInstace().addActivity(aty);
		
		listViews = new ArrayList<View>();
		LayoutInflater mInflater = getLayoutInflater();
		listViews.add(mInflater.inflate(R.layout.knowledgedetail_viewpager, null));
		listViews.add(mInflater.inflate(R.layout.knowledgedetail_viewpager, null));
		listViews.add(mInflater.inflate(R.layout.knowledgedetail_viewpager, null));
		listViews.add(mInflater.inflate(R.layout.knowledgedetail_viewpager, null));
		
		Bundle bundle = this.getIntent().getExtras();
		knowID = bundle.getInt("id");
		imagePath1 = bundle.getString("path1");
		imagePath2 = bundle.getString("path2");
		imagePath3 = bundle.getString("path3");
		imagePath4 = bundle.getString("path4");
		
		userPreferences = getSharedPreferences("user", MODE_WORLD_READABLE);
		userID = userPreferences.getInt("userId", -1);
		
		HttpUserRequest2 request2 = new HttpUserRequest2(aty,rceiveHandler,3);
		request2.isCollectionInit(userID, knowID, "HealthKnowledge");
		request2.start();
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("InlinedApi") 
	@Override
	public void initWidget() {
		super.initWidget();
		
		gestureDetector = new GestureDetector(this); // 手势滑动
		
		viewPager = (ViewPager) this.findViewById(R.id.knowledgedetail_viewPager);
		
		viewPager.setCurrentItem(0);
		WebView img = (WebView) listViews.get(0).findViewById(R.id.knowledgedetail_viewpager_item);
		String path = aty.getResources().getString(R.string.localhost) + imagePath1;
		img.loadUrl(path);
		img.setBackgroundColor(aty.getResources().getColor(R.color.transparent));
		img.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		img.getSettings().setLoadWithOverviewMode(true);
		
		viewPager.setAdapter(new MyPagerAdapter(listViews));
		viewPager.setPageTransformer(true, new DepthPageTransformer());//设置动画
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageScrollStateChanged(int state) {
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageSelected(int index) {
				viewPager.setCurrentItem(index);
				pageText.setText((index+1)+"/4");
				
				WebView img = (WebView) listViews.get(index).findViewById(R.id.knowledgedetail_viewpager_item);
				String path = null;
				if ( index == 0 ) {
					path = aty.getResources().getString(R.string.localhost) + imagePath1;
				} else if ( index == 1 ) {
					path = aty.getResources().getString(R.string.localhost) + imagePath2;
				} else if ( index == 2 ) {
					path = aty.getResources().getString(R.string.localhost) + imagePath3;
				} else if ( index == 3 ) {
					path = aty.getResources().getString(R.string.localhost) + imagePath4;
				}
				img.loadUrl(path);
				img.setBackgroundColor(aty.getResources().getColor(R.color.transparent));
				img.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
				img.getSettings().setLoadWithOverviewMode(true);
			}
		});
	}

	@Override
	public void widgetClick(View v) {
		super.widgetClick(v);
		
		switch(v.getId()) {
		case R.id.knowledgedetail_backicon:
		case R.id.knowledgedetail_backtext:
			this.finish();
		case R.id.knowledgedetail_collectbtn:
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
		}
	}
	

	@Override
	protected void onPause() {
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
	public boolean onTouchEvent(MotionEvent event) {
		return gestureDetector.onTouchEvent(event);
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
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
			if ( currentPage > 0 ) {
				currentPage--;
				viewPager.setCurrentItem(currentPage);
			}
		} else if ( (e2.getX() - e1.getX()) < -120 && Math.abs(e2.getY() - e1.getY()) < 50 ) {
			if ( currentPage < (listViews.size() - 1)  ) {
				currentPage++;
				viewPager.setCurrentItem(currentPage);
			}
		} else if ( (e2.getX() - e1.getX()) > 180 && Math.abs(e2.getY() - e1.getY()) < 80 ) {
			if ( menuWindow == null ) {
				menuWindow = new PersonalPopupWindow(aty);//显示窗口  
	            menuWindow.showAtLocation(this.findViewById(R.id.knowledgedetail), 
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