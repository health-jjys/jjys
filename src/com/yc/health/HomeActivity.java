package com.yc.health;

import org.kymjs.kjframe.KJActivity;
import org.kymjs.kjframe.ui.BindView;

import com.yc.health.fragment.PersonalPopupWindow;
import com.yc.health.manager.ActivityManager;
import com.yc.health.util.ListUtils;
import com.yc.health.util.Method;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;

public class HomeActivity extends KJActivity implements OnGestureListener{

	@BindView(id = R.id.home_ajk, click = true)
	private ImageButton ajkBtn;
	@BindView(id = R.id.home_zyys, click = true)
	private ImageButton zyysBtn;
	@BindView(id = R.id.home_shq, click = true)
	private ImageButton shqBtn;
	@BindView(id = R.id.home_wd, click = true)
	private ImageButton wdBtn;
	@BindView(id = R.id.home_srdz, click = true)
	private ImageButton srdzBtn;
	@BindView(id = R.id.home_hyzg, click = true)
	private ImageButton hyzgBtn;
	@BindView(id = R.id.home_layout1, click = true)
	private View layout1;
	@BindView(id = R.id.home_layout2, click = true)
	private View layout2;
	
	@BindView(id = R.id.home_menu, click = true)
	private ImageView menuBtn;
	
	private SharedPreferences userPreferences;
	private String loginName = null;
	
	private PersonalPopupWindow menuWindow = null;
	private GestureDetector gestureDetector;
	
	private int layoutHeight = 0;
	
    @Override
	public void setRootView() {
    	setContentView(R.layout.home);
	}

	@SuppressLint("WorldReadableFiles") 
	@SuppressWarnings("deprecation")
	@Override
	public void initData() {
		super.initData();
		
		ActivityManager.getInstace().addActivity(aty);
		
		userPreferences = getSharedPreferences("user", MODE_WORLD_READABLE);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void initWidget() {
		super.initWidget();
		
		gestureDetector = new GestureDetector(this); // 手势滑动
		
        ViewTreeObserver vto = layout1.getViewTreeObserver();   
        vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() { 
            @Override  
            public void onGlobalLayout() { 
            	layout1.getViewTreeObserver().removeGlobalOnLayoutListener(this); 
            	layoutHeight = layout1.getHeight();
            	
            	RelativeLayout.LayoutParams lp = (android.widget.RelativeLayout.LayoutParams) ajkBtn.getLayoutParams();
        		lp.bottomMargin = layoutHeight + ListUtils.dip2px(aty, 20) - layoutHeight/6;
        		ajkBtn.setLayoutParams(lp);
        		
        		RelativeLayout.LayoutParams lp1 = (android.widget.RelativeLayout.LayoutParams) layout2.getLayoutParams();
        		lp1.bottomMargin = ListUtils.dip2px(aty, 12) + layoutHeight/6 ;
        		layout2.setLayoutParams(lp1);
            }   
        });
	}

	@Override
	public void widgetClick(View v) {
		super.widgetClick(v);
		
		switch(v.getId()) {
		case R.id.home_menu:
			if ( menuWindow == null ) {
				menuWindow = new PersonalPopupWindow(aty);//显示窗口  
	            menuWindow.showAtLocation(this.findViewById(R.id.home), 
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
			break;
		case R.id.home_ajk:
			this.showActivity(aty, LikeHelathActivity.class);
			break;
		case R.id.home_hyzg:
			if ( loginName != null ) {
				this.showActivity(aty, MemberShoppeActivity.class);
			} else {
				this.showActivity(aty, LoginActivity.class);
			}
			break;
		case R.id.home_shq:
			if ( loginName != null ) {
				this.showActivity(aty, LifeSphereActivity.class);
			} else {
				this.showActivity(aty, LoginActivity.class);
			}
			break;
		case R.id.home_srdz:
			if ( loginName != null ) {
				this.showActivity(aty, PrivateOrderActivity.class);
			} else {
				this.showActivity(aty, LoginActivity.class);
			}
			break;
		case R.id.home_wd:
			this.showActivity(aty, PersonalActivity.class);
//			if ( loginName != null ) {
//				this.showActivity(aty, PersonalActivity.class);
//			} else {
//				this.showActivity(aty, LoginActivity.class);
//			}
			break;
		case R.id.home_zyys:
			String constitution = userPreferences.getString("constitution", null);
			if ( constitution != null ) {
				Bundle bundle = new Bundle();
				bundle.putString("constitution", constitution);
				this.showActivity(aty, ConstitutionRecommendActivity.class, bundle);
			} else {
				Bundle bundle = new Bundle();
				bundle.putString("who", "mine");
				this.showActivity(aty, TestActivity.class, bundle);
			}
			break;
		}
	}

	@Override
	protected void onResume() {
		loginName = userPreferences.getString("loginName", null);
		super.onResume();
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
	            menuWindow.showAtLocation(this.findViewById(R.id.home), 
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
