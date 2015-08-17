package com.yc.health;

import java.util.ArrayList;
import java.util.List;

import org.kymjs.kjframe.KJActivity;
import org.kymjs.kjframe.ui.BindView;

import com.yc.health.adapter.FamilyListAdapter;
import com.yc.health.fragment.PersonalPopupWindow;
import com.yc.health.http.HttpUserRequest;
import com.yc.health.manager.ActivityManager;
import com.yc.health.model.MemberFamilyModel;
import com.yc.health.util.Method;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow.OnDismissListener;

public class FamilyActivity extends KJActivity implements OnGestureListener{

	@BindView(id = R.id.family_list, click = true)
	private ListView listView;
	@BindView(id = R.id.family_ok, click = true)
	private Button okBtn;
	@BindView(id = R.id.family_edit, click = true)
	private Button editBtn;
	
	private FamilyListAdapter adapter;
	private SharedPreferences userPreferences;
	public static boolean editStatus = false;
	private List<MemberFamilyModel> family = new ArrayList<MemberFamilyModel>();
	
	private PersonalPopupWindow menuWindow = null;
	private GestureDetector gestureDetector;
	
	private Handler mHandler = new Handler(){
		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			if ( msg.what == 1 ) {
				family = (List<MemberFamilyModel>) msg.obj;
				adapter.setFamily(family);
				adapter.notifyDataSetChanged();
			} else if ( msg.what == 2 ) {
				adapter.notifyDataSetChanged();
			}
			super.handleMessage(msg);
		}
	};
	
	@Override
	public void setRootView() {
		setContentView(R.layout.family);
	}

	@SuppressLint("WorldReadableFiles") 
	@SuppressWarnings("deprecation")
	@Override
	public void initData() {
		super.initData();
		
		ActivityManager.getInstace().addActivity(aty);
		
		userPreferences = getSharedPreferences("user", MODE_WORLD_READABLE);
		int userId = userPreferences.getInt("userId", -1);
		
		adapter = new FamilyListAdapter(family,aty,mHandler);
		
		HttpUserRequest request = new HttpUserRequest(aty, mHandler, 3);
		request.getMembersInit(userId);
		request.start();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void initWidget() {
		super.initWidget();
		
		gestureDetector = new GestureDetector(this); // 手势滑动
		
		okBtn.setVisibility(View.GONE);
		
		listView = (ListView) this.findViewById(R.id.family_list);
		listView.setAdapter(adapter);
	}

	@Override
	public void widgetClick(View v) {
		super.widgetClick(v);
		
		switch(v.getId()) {
		case R.id.family_ok:
			if ( editStatus ) {
				editStatus = false;
				editBtn.setText("编辑");
				okBtn.setVisibility(View.GONE);
				adapter.notifyDataSetChanged();
			}
			break;
		case R.id.family_edit:
			if ( editStatus ) {
				Bundle bundle = new Bundle();
				bundle.putString("who", "family");
				showActivity(aty, TestActivity.class, bundle);
			} else {
				editStatus = true;
				editBtn.setText("添加");
				okBtn.setText("完成");
				okBtn.setVisibility(View.VISIBLE);
			}
			adapter.notifyDataSetChanged();
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
	            menuWindow.showAtLocation(this.findViewById(R.id.family), 
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
