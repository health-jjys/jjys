package com.yc.health;

import org.kymjs.kjframe.KJActivity;
import org.kymjs.kjframe.ui.BindView;

import com.yc.health.adapter.ConstitutionGridViewAdapter;
import com.yc.health.fragment.PersonalPopupWindow;
import com.yc.health.http.HttpUserRequest;
import com.yc.health.manager.ActivityManager;
import com.yc.health.util.Method;
import com.yc.health.widget.CustomToast;
import com.yc.health.widget.GridCommodity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.ImageView;

public class ConstitutionSelectorActivity extends KJActivity implements OnGestureListener{

	@BindView(id = R.id.constitutionselector_constitutions, click = true)
	private GridCommodity constitutions;
	@BindView(id = R.id.constitutinselector_test, click = true)
	private Button test;
	@BindView(id = R.id.constitutionselector_back, click = true)
	private ImageView backBtn;
	
	private ConstitutionGridViewAdapter adapter = null;
	private SharedPreferences testPreferences;
	private SharedPreferences userPreferences;
	private String who = null;
	
	private PersonalPopupWindow menuWindow = null;
	private GestureDetector gestureDetector;
	
	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			if ( msg.what == 1 ) {
				CustomToast.showToast(aty, "已成功存储!", 6000);
			} else if ( msg.what == 2 ) {
				CustomToast.showToast(aty, "用户已成功存储!", 6000);
			}
			super.handleMessage(msg);
		}
	};
	
	@Override
	public void setRootView() {
		setContentView(R.layout.constitutionselector);
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("WorldReadableFiles") 
	@Override
	public void initData() {
		super.initData();
		
		ActivityManager.getInstace().addActivity(aty);
		adapter = new ConstitutionGridViewAdapter(aty);
		
		testPreferences = getSharedPreferences("testInfo", MODE_WORLD_READABLE);
		userPreferences = getSharedPreferences("user", MODE_WORLD_READABLE);
		
		Bundle bundle = this.getIntent().getExtras();
		who = bundle.getString("who");
	}

	@SuppressWarnings("deprecation")
	@Override
	public void initWidget() {
		super.initWidget();
		
		gestureDetector = new GestureDetector(this); // 手势滑动
		
		constitutions = (GridCommodity) this.findViewById(R.id.constitutionselector_constitutions);
		constitutions.setAdapter(adapter);
		constitutions.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				String constitution = null;
				Bundle bundle = new Bundle();
				switch(position) {
				case 0:
					constitution = "平和质";
					break;
				case 1:
					constitution = "气虚质";
					break;
				case 2:
					constitution = "阳虚质";
					break;
				case 3:
					constitution = "阴虚质";
					break;
				case 4:
					constitution = "痰湿质";
					break;
				case 5:
					constitution = "湿热质";
					break;
				case 6:
					constitution = "血瘀质";
					break;
				case 7:
					constitution = "气郁质";
					break;
				case 8:
					constitution = "特禀质";
					break;
				}
				Editor editor = testPreferences.edit();
				editor.putString("constitution", constitution);
				editor.commit();
				
				String loginName = userPreferences.getString("loginName", null);
				String memberName = testPreferences.getString("userName", null);
				String sex = testPreferences.getString("sex", "男");
				if ( loginName != null ) {
					if ( "family".equals(who) ) {
						//存储家人到数据库
						HttpUserRequest request = new HttpUserRequest(aty,mHandler,2);
						request.addMemberInit(loginName, memberName, constitution);
						request.start();
					} else {
						//存储自己到数据库
						HttpUserRequest request = new HttpUserRequest(aty,mHandler,5);
						request.saveUserInfoInit(loginName, memberName, sex, constitution);
						request.start();
						
						Editor userEditor = userPreferences.edit();
						userEditor.putString("userName", memberName);
						userEditor.putString("sex", sex);
						userEditor.putString("constitution", constitution);
						userEditor.commit();
					}
				}  else {
					Editor userEditor = userPreferences.edit();
					userEditor.putString("userName", memberName);
					userEditor.putString("sex", sex);
					userEditor.putString("constitution", constitution);
					userEditor.commit();
				}
				
				bundle.putString("constitution", constitution);
				ConstitutionSelectorActivity.this.showActivity(aty, ConstitutionRecommendActivity.class, bundle);
			}
		});
	}

	@Override
	public void widgetClick(View v) {
		super.widgetClick(v);
		
		switch(v.getId()) {
		case R.id.constitutinselector_test:
			Bundle bundle = new Bundle();
			bundle.putString("who", who);
			this.showActivity(aty, TestQuestionsActivity.class, bundle);
			break;
		case R.id.constitutionselector_back:
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
	            menuWindow.showAtLocation(this.findViewById(R.id.constitutionselector), 
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