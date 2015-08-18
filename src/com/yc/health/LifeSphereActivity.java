package com.yc.health;

import java.util.ArrayList;
import java.util.List;

import org.kymjs.kjframe.KJActivity;
import org.kymjs.kjframe.ui.BindView;

import com.yc.health.adapter.LifeSphereGridViewAdapter;
import com.yc.health.adapter.LifeSphereListAdapter;
import com.yc.health.fragment.PersonalPopupWindow;
import com.yc.health.manager.ActivityManager;
import com.yc.health.model.LifeSphereModel;
import com.yc.health.util.ListUtils;
import com.yc.health.util.Method;
import com.yc.health.widget.GridCommodity;

import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.ImageView;
import android.widget.ListView;

public class LifeSphereActivity extends KJActivity implements OnGestureListener{

	@BindView(id = R.id.lifeSphere_back, click = true)
	private ImageView backBtn;
	@BindView(id = R.id.lifeSphere_grid, click = true)
	private GridCommodity lifeSphere;
	@BindView(id = R.id.lifeSphere_grid, click = true)
	private ListView listView;
	
	private List<LifeSphereModel> list = new ArrayList<LifeSphereModel>();
	private LifeSphereGridViewAdapter gridAdapter = null;
	private LifeSphereListAdapter listAdapter = null;
	
	private float x = 0f;
	private float y = 0f;
	
	private PersonalPopupWindow menuWindow = null;
	private GestureDetector gestureDetector;
	
	@Override
	public void setRootView() {
		setContentView(R.layout.lifesphere);
	}

	@Override
	public void initData() {
		super.initData();
		
		ActivityManager.getInstace().addActivity(aty);
		
		gridAdapter = new LifeSphereGridViewAdapter(aty);
		listAdapter = new LifeSphereListAdapter(list,aty);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void initWidget() {
		super.initWidget();
		
		gestureDetector = new GestureDetector(this); // 手势滑动
		
		lifeSphere = (GridCommodity) this.findViewById(R.id.lifeSphere_grid);
		lifeSphere.setAdapter(gridAdapter);
		lifeSphere.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				//跳转到某一类的列表
				LifeSphereActivity.this.showActivity(aty, LifeSphereListActivity.class);
			}
		});
		
		listView = (ListView) this.findViewById(R.id.lifeSphere_list);
		listView.setAdapter(listAdapter);
		new ListUtils(this).setListViewHeightBasedOnChildren(listView,105);
		listView.setOnTouchListener(new OnTouchListener(){
			@Override
			public boolean onTouch(View arg0, MotionEvent e) {
				if ( e.getAction() == MotionEvent.ACTION_DOWN ) {
					x = (int)e.getX();
					y = (int)e.getY();
				} 
				
				if ( e.getAction() == MotionEvent.ACTION_UP ) {
					if ( 50 > Math.abs(e.getX()-x) && 50 > Math.abs(e.getY()-y) ) {
						LifeSphereActivity.this.showActivity(aty, DetailActivity.class);
						return false;
					} else {
						return true;
					}
				}
				return false;
			}
		});
	}

	@Override
	public void widgetClick(View v) {
		super.widgetClick(v);
		
		switch(v.getId()) {
		case R.id.lifeSphere_back:
			this.finish();
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
	            menuWindow.showAtLocation(this.findViewById(R.id.lifesphere), 
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
