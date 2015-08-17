package com.yc.health;

import java.util.ArrayList;
import java.util.List;

import org.kymjs.kjframe.KJActivity;
import org.kymjs.kjframe.ui.BindView;

import com.yc.health.adapter.LifeSphereGridViewAdapter;
import com.yc.health.adapter.LifeSphereListAdapter;
import com.yc.health.fragment.PersonalPopupWindow;
import com.yc.health.http.HttpMemberShoppeRequest;
import com.yc.health.manager.ActivityManager;
import com.yc.health.model.MemberShoppeModel;
import com.yc.health.util.ListUtils;
import com.yc.health.util.Method;
import com.yc.health.widget.GridCommodity;

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
import android.widget.ImageView;
import android.widget.ListView;

public class LifeSphereActivity extends KJActivity implements OnGestureListener{

	@BindView(id = R.id.lifeSphere_back, click = true)
	private ImageView backBtn;
	@BindView(id = R.id.lifeSphere_grid, click = true)
	private GridCommodity lifeSphere;
	@BindView(id = R.id.lifeSphere_grid, click = true)
	private ListView listView;
	
	private float x = 0f;
	private float y = 0f;
	
	private PersonalPopupWindow menuWindow = null;
	private GestureDetector gestureDetector;
	
	private String type = null;
	private List<MemberShoppeModel> list = new ArrayList<MemberShoppeModel>();
	private LifeSphereGridViewAdapter gridAdapter = null;
	private LifeSphereListAdapter listAdapter = null;
	private Handler mHandler = new Handler() {
		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			if ( msg.what == 1 ) {
				list = (ArrayList<MemberShoppeModel>) msg.obj;
				listAdapter.setList(list);
				listAdapter.notifyDataSetChanged();
			}
			super.handleMessage(msg);
		}
	};
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
		
		type = "";
		HttpMemberShoppeRequest request = new HttpMemberShoppeRequest(aty,mHandler,1);
		request.getMemberShoppeInit(type,12);
		request.start();
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
				Bundle bundle = new Bundle();
				//跳转到某一类的列表
				switch(position) {
				case 0:
					bundle.putString("type", "deliciousfood");
					break;
				case 1:
					bundle.putString("type", "beauty");
					break;
				case 2:
					bundle.putString("type", "fallow");
					break;
				case 3:
					bundle.putString("type", "examination");
					break;
				case 4:
					bundle.putString("type", "therapy");
					break;
				case 5:
					bundle.putString("type", "convenient");
					break;
				case 6:
					bundle.putString("type", "hotel");
					break;
				case 7:
					bundle.putString("type", "car");
					break;
				}
				LifeSphereActivity.this.showActivity(aty, LifeSphereListActivity.class, bundle);
			}
		});
		
		listView = (ListView) this.findViewById(R.id.lifeSphere_list);
		listView.setAdapter(listAdapter);
		new ListUtils(this).setListViewHeightBasedOnChildren(listView,180);
		listView.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				Bundle bundle = new Bundle();
				bundle.putString("name", list.get(position).getName());
				bundle.putString("description", list.get(position).getName());
				bundle.putString("address", list.get(position).getName());
				bundle.putString("path", list.get(position).getImagePath());
				bundle.putString("type", list.get(position).getType());
				LifeSphereActivity.this.showActivity(aty, TypeDetailActivity.class, bundle);
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
