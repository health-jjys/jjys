package com.yc.health;

import java.util.ArrayList;
import java.util.List;

import org.kymjs.kjframe.KJActivity;
import org.kymjs.kjframe.ui.BindView;

import com.yc.health.adapter.LifeSphereListAdapter;
import com.yc.health.fragment.PersonalPopupWindow;
import com.yc.health.http.HttpMemberShoppeRequest;
import com.yc.health.manager.ActivityManager;
import com.yc.health.model.MemberShoppeModel;
import com.yc.health.util.Method;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.PopupWindow.OnDismissListener;

public class LifeSphereListActivity extends KJActivity implements OnGestureListener{

	@BindView(id = R.id.lifeSphere_list_back, click = true)
	private ImageView backBtn;
	@BindView(id = R.id.lifeSphere_list_location, click = true)
	private ImageView locationBtn;
	@BindView(id = R.id.lifeSphere_list_title)
	private TextView titleText;
	@BindView(id = R.id.lifeSphere_list_list, click = true)
	private ListView listView;
	
	private PersonalPopupWindow menuWindow = null;
	private GestureDetector gestureDetector;
	
	private String type = null;
	private List<MemberShoppeModel> list = new ArrayList<MemberShoppeModel>();
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
		setContentView(R.layout.lifesphere_list);
	}

	@Override
	public void initData() {
		super.initData();
		
		ActivityManager.getInstace().addActivity(aty);
		
		listAdapter = new LifeSphereListAdapter(list,aty);
		
		Bundle bundle = this.getIntent().getExtras();
		type = bundle.getString("type");
		HttpMemberShoppeRequest request = new HttpMemberShoppeRequest(aty,mHandler,1);
		request.getMemberShoppeInit(type,-1);
		request.start();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void initWidget() {
		super.initWidget();
		
		Method method = new Method();
		titleText.setText(method.showMemberShppeType(type));
		
		gestureDetector = new GestureDetector(this); // 手势滑动
		
		listView = (ListView) this.findViewById(R.id.lifeSphere_list_list);
		listView.setAdapter(listAdapter);
		listView.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				//跳转到详情
				Bundle bundle = new Bundle();
				bundle.putInt("id", list.get(position).getMemberShoppeId());
				bundle.putString("name", list.get(position).getName());
				bundle.putString("description", list.get(position).getName());
				bundle.putString("address", list.get(position).getName());
				bundle.putString("path", list.get(position).getImagePath());
				bundle.putString("type", list.get(position).getType());
				LifeSphereListActivity.this.showActivity(aty, TypeDetailActivity.class, bundle);
			}
		});
	}

	@Override
	public void widgetClick(View v) {
		super.widgetClick(v);
		
		switch(v.getId()) {
		case R.id.lifeSphere_list_back:
			this.finish();
			break;
		case R.id.lifeSphere_list_location:
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
	            menuWindow.showAtLocation(this.findViewById(R.id.lifesphere_list), 
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
