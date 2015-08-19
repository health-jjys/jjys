package com.yc.health;

import java.util.ArrayList;
import java.util.List;

import org.kymjs.kjframe.KJActivity;
import org.kymjs.kjframe.ui.BindView;

import com.yc.health.adapter.RecommendListAdapter;
import com.yc.health.fragment.PersonalPopupWindow;
import com.yc.health.manager.ActivityManager;
import com.yc.health.model.RecommendModel;
import com.yc.health.util.Method;
import com.yc.health.widget.OnRefreshListener;
import com.yc.health.widget.RefreshListView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.ImageView;

public class RecommendListActivity extends KJActivity implements OnGestureListener{

	@BindView(id = R.id.recommend_back, click = true)
	private ImageView backBtn;
	@BindView(id = R.id.recommend_home, click = true)
	private ImageView homeBtn;
	@BindView(id = R.id.recommend_list, click = true)
	private RefreshListView listView;
	
	private List<RecommendModel> recommends = new ArrayList<RecommendModel>();
	private List<RecommendModel> newRecommends = new ArrayList<RecommendModel>();
	private RecommendListAdapter adapter;
	private String title = null;
	private ArrayList<String> constitutions = null;
	
	private PersonalPopupWindow menuWindow = null;
	private GestureDetector gestureDetector;
	
	@Override
	public void setRootView() {
		setContentView(R.layout.recommend_list);
	}

	@Override
	public void initData() {
		super.initData();
		
		ActivityManager.getInstace().addActivity(aty);
		
		Bundle bundle = this.getIntent().getExtras();
		title = bundle.getString("title");
		constitutions = bundle.getStringArrayList("constitutions");
		
		adapter = new RecommendListAdapter(recommends,aty);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void initWidget() {
		super.initWidget();
		
		gestureDetector = new GestureDetector(this); // 手势滑动
		
		listView = (RefreshListView) this.findViewById(R.id.recommend_list);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				RecommendListActivity.this.showActivity(aty, DetailActivity.class);
			}
		});
		
		listView.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				// 异步查询数据
				new AsyncTask<Void, Void, Void>(){
					@Override
					protected Void doInBackground(Void... params) {
						SystemClock.sleep(2000);
						recommends.addAll(newRecommends);
						return null;
					}
					
					protected void onPostExecute(Void result) {
						adapter.notifyDataSetChanged();
						// 隐藏头布局
						listView.onRefreshFinish();
					}
				}.execute(new Void[]{});
			}

			@Override
			public void onLoadMoring() {
				new AsyncTask<Void, Void, Void>() {
					@Override
					protected Void doInBackground(Void... params) {
						SystemClock.sleep(5000);
						recommends.addAll(newRecommends);
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						super.onPostExecute(result);
						adapter.notifyDataSetChanged();
						listView.onRefreshFinish();
					}
				}.execute(new Void[]{});
			}
		});
	}

	@Override
	public void widgetClick(View v) {
		super.widgetClick(v);
		
		switch(v.getId()) {
		case R.id.recommend_back:   
			this.finish();
			break;
		case R.id.recommend_home:
			ActivityManager.getInstace().enterHome();
			this.showActivity(aty, HomeActivity.class);
			break;
		}
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
	            menuWindow.showAtLocation(this.findViewById(R.id.recommendlist), 
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
