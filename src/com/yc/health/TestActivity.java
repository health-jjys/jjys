package com.yc.health;

import org.kymjs.kjframe.KJActivity;
import org.kymjs.kjframe.ui.BindView;

import com.yc.health.fragment.PersonalPopupWindow;
import com.yc.health.fragment.TestPopupWindow;
import com.yc.health.manager.ActivityManager;
import com.yc.health.util.Method;
import com.yc.health.widget.CustomToast;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

public class TestActivity extends KJActivity implements OnGestureListener{

	@BindView(id = R.id.test_back, click = true)
	private ImageView backBtn;
	@BindView(id = R.id.test_inputUserName, click = true)
	private EditText userNameEdit;
	@BindView(id = R.id.test_inputDate, click = true)
	private TextView dateText;
	@BindView(id = R.id.test_city, click = true)
	private TextView cityText;
	@BindView(id = R.id.test_selectSex, click = true)
	private View selectSexView;
	@BindView(id = R.id.test_sexhint, click = true)
	private TextView sexHintText;
	@BindView(id = R.id.test_sex, click = true)
	private TextView sexText;
	@BindView(id = R.id.test_startTest, click = true)
	private ImageView testBtn;
	
	private TestPopupWindow testWindow;
	
	private String who = null; //分为是本人测试还是家人测试，mine,family
	
	private SharedPreferences testPreferences;
	
	private PersonalPopupWindow menuWindow = null;
	private GestureDetector gestureDetector;
		
	@Override
	public void setRootView() {
		setContentView(R.layout.test);
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("WorldReadableFiles") 
	@Override
	public void initData() {
		super.initData();
		
		ActivityManager.getInstace().addActivity(aty);
		
		testPreferences = getSharedPreferences("testInfo", MODE_WORLD_READABLE);
		
		Bundle bundle = this.getIntent().getExtras();
		who = bundle.getString("who");
	}

	@SuppressWarnings("deprecation")
	@Override
	public void initWidget() {
		super.initWidget();
		
		gestureDetector = new GestureDetector(this); // 手势滑动
	}

	@Override
	public void widgetClick(View v) {
		super.widgetClick(v);
		
		switch(v.getId()) {
		case R.id.test_back:
			this.finish();
			break;
		case R.id.test_startTest:
			//存储信息
			String userName = userNameEdit.getText().toString();
			String strCity = cityText.getText().toString();
			String birthday = dateText.getText().toString();
			
			if ( (!"".equals(userName)) && (!birthday.contains("点击"))
					&& (!strCity.contains("点击"))) {
				Editor editor = testPreferences.edit();
				editor.putString("sex", sexText.getText().toString());
				editor.putString("userName", userName);
				editor.commit();
				
				Bundle bundle = new Bundle();
				bundle.putString("who", who);
				this.showActivity(aty, ConstitutionSelectorActivity.class, bundle);
			} else {
				CustomToast.showToast(aty, "请输入完整的信息！", 8000);
			}
			break;
		case R.id.test_sexhint:
		case R.id.test_sex:
			if ( testWindow == null ) {
				userNameEdit.clearFocus();
				userNameEdit.setCursorVisible(false);
				testWindow = new TestPopupWindow(aty,1);//显示窗口  
				testWindow.showAtLocation(this.findViewById(R.id.test), 
	            		Gravity.BOTTOM, 0, 0); 
				testWindow.setOnDismissListener(new OnDismissListener(){
					@Override
					public void onDismiss() {
						Method method = new Method(aty);
						method.backgroundAlpha(1f);
						testWindow = null;
					}
	            });
			}
			break;
		case R.id.test_inputDate:
			if ( testWindow == null ) {
				testWindow = new TestPopupWindow(aty,2);//显示窗口  
				testWindow.showAtLocation(this.findViewById(R.id.test), 
	            		Gravity.BOTTOM, 0, 0); 
				testWindow.setOnDismissListener(new OnDismissListener(){
					@Override
					public void onDismiss() {
						Method method = new Method(aty);
						method.backgroundAlpha(1f);
						testWindow = null;
					}
	            });
			}
			break;
		case R.id.test_city:
			if ( testWindow == null ) {
				testWindow = new TestPopupWindow(aty,3);//显示窗口  
				testWindow.showAtLocation(this.findViewById(R.id.test), 
	            		Gravity.BOTTOM, 0, 0); 
				testWindow.setOnDismissListener(new OnDismissListener(){
					@Override
					public void onDismiss() {
						Method method = new Method(aty);
						method.backgroundAlpha(1f);
						testWindow = null;
					}
	            });
			}
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
	            menuWindow.showAtLocation(this.findViewById(R.id.test), 
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
