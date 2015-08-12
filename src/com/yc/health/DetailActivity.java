package com.yc.health;

import org.kymjs.kjframe.KJActivity;
import org.kymjs.kjframe.ui.BindView;

import com.yc.health.fragment.PersonalPopupWindow;
import com.yc.health.manager.ActivityManager;
import com.yc.health.util.Method;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.PopupWindow.OnDismissListener;

public class DetailActivity extends KJActivity implements OnGestureListener{

	@BindView(id = R.id.detail_back, click = true)
	private ImageView backBtn;
	@BindView(id = R.id.detail_location, click = true)
	private ImageView locationBtn;
	@BindView(id = R.id.detail_title)
	private TextView titleText;
	@BindView(id = R.id.detail_desc_title)
	private TextView descTitleText;//需要划下划线的标题
	@BindView(id = R.id.detail_recommend_title)
	private TextView recommendTitleText;//需要划下划线的标题
	@BindView(id = R.id.detail_collection, click = true)
	private ImageView collectionBtn;
	@BindView(id = R.id.detail_name)
	private TextView nameText;
	@BindView(id = R.id.detail_describe)
	private TextView describeText;
	
	@BindView(id = R.id.detail_recommend1, click = true)
	private ImageView recommend1;
	@BindView(id = R.id.detail_recommend2, click = true)
	private ImageView recommend2;
	@BindView(id = R.id.detail_recommend3, click = true)
	private ImageView recommend3;
	@BindView(id = R.id.detail_recommend1_name)
	private TextView name1Text;
	@BindView(id = R.id.detail_recommend2_name)
	private TextView name2Text;
	@BindView(id = R.id.detail_recommend3_name)
	private TextView name3Text;
	@BindView(id = R.id.detail_recommend1_desc)
	private TextView desc1Text;
	@BindView(id = R.id.detail_recommend2_desc)
	private TextView desc2Text;
	@BindView(id = R.id.detail_recommend3_desc)
	private TextView desc3Text;
	
	private SharedPreferences userPreferences;
	private String loginName = null;
	
	private PersonalPopupWindow menuWindow = null;
	private GestureDetector gestureDetector;
	
	@Override
	public void setRootView() {
		setContentView(R.layout.detail);
	}

	@SuppressLint("WorldReadableFiles") 
	@SuppressWarnings("deprecation")
	@Override
	public void initData() {
		super.initData();
		
		ActivityManager.getInstace().addActivity(aty);
		
		userPreferences = getSharedPreferences("user", MODE_WORLD_READABLE);
		loginName = userPreferences.getString("loginName", null);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void initWidget() {
		super.initWidget();
		
		gestureDetector = new GestureDetector(this); // 手势滑动
		
		descTitleText.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		descTitleText.getPaint().setAntiAlias(true);
		
		recommendTitleText.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		recommendTitleText.getPaint().setAntiAlias(true);
	}

	@Override
	public void widgetClick(View v) {
		super.widgetClick(v);
		
		switch(v.getId()) {
		case R.id.detail_back:
			this.finish();
			break;
		case R.id.detail_location:
			this.showActivity(aty, DoctNavActivity.class);
			break;
		case R.id.detail_collection:
			if ( loginName == null ) {
				Method method = new Method(aty);
				method.loginHint();
			} else {
				
			}
			break;
		case R.id.detail_recommend1:
			break;
		case R.id.detail_recommend2:
			break;
		case R.id.detail_recommend3:
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
	            menuWindow.showAtLocation(this.findViewById(R.id.detail), 
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
