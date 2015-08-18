package com.yc.health;

import java.util.ArrayList;
import java.util.List;

import org.kymjs.kjframe.KJActivity;
import org.kymjs.kjframe.ui.BindView;

import com.yc.health.adapter.PrivateOrderListAdapter;
import com.yc.health.fragment.PersonalPopupWindow;
import com.yc.health.manager.ActivityManager;
import com.yc.health.model.PrivateOrderModel;
import com.yc.health.util.ListUtils;
import com.yc.health.util.Method;

import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

public class PrivateOrderActivity extends KJActivity implements OnGestureListener{

	@BindView(id = R.id.private_order_back, click = true)
	private ImageView backBtn;
	@BindView(id = R.id.private_order_safeFood, click = true)
	private Button safeFoodBtn;
	@BindView(id = R.id.private_order_doctor, click = true)
	private Button doctorBtn;
	@BindView(id = R.id.private_order_life, click = true)
	private Button lifeBtn;
	@BindView(id = R.id.private_order_btn_selected, click = true)
	private ImageView btnSelected;
	
	@BindView(id = R.id.private_order_list, click = true)
	private ListView listView;
	
	private List<PrivateOrderModel> privateOrders = new ArrayList<PrivateOrderModel>();
	private PrivateOrderListAdapter adapter;
	
	private int curBtn = 1;//1为饮食，2为医疗，3为生活管家
	private int clickBtn = 1;
	private int windowsWidth;

	private PersonalPopupWindow menuWindow = null;
	private GestureDetector gestureDetector;
	
	@Override
	public void setRootView() {
		setContentView(R.layout.private_order);
	}

	@Override
	public void initData() {
		super.initData();
		
		ActivityManager.getInstace().addActivity(aty);
		
		adapter = new PrivateOrderListAdapter(privateOrders,aty);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void initWidget() {
		super.initWidget();
		
		// 分类下的彩色条
		gestureDetector = new GestureDetector(this); // 手势滑动
		windowsWidth = this.getWindowManager().getDefaultDisplay().getWidth();
		int offset = ListUtils.dip2px(aty, 30);
		btnSelected.getLayoutParams().width = windowsWidth/3-offset;
				
		listView = (ListView) this.findViewById(R.id.private_order_list);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				PrivateOrderActivity.this.showActivity(aty, DetailActivity.class);
			}
		});
	}

	@Override
	public void widgetClick(View v) {
		super.widgetClick(v);
		
		if ( v.getId() == R.id.private_order_back ) {
			this.finish();
		} else {
			if ( v.getId() == R.id.private_order_safeFood ) {
				if ( curBtn != 1) {
					clickBtn = 1;
					moveAnimation(curBtn - clickBtn);
					curBtn = 1;
				}
			} else if ( v.getId() == R.id.private_order_doctor ) {
				if ( curBtn != 2) {
					clickBtn = 2;
					moveAnimation(curBtn - clickBtn);
					curBtn = 2;
				}
			} else if ( v.getId() == R.id.private_order_life ) {
				if ( curBtn != 3) {
					clickBtn = 3;
					moveAnimation(curBtn - clickBtn);
					curBtn = 3;
				}
			}
			
			if ( curBtn == 1 ) {
				safeFoodBtn.setTextColor(this.getResources().getColor(R.color.ajk_selected));
				doctorBtn.setTextColor(this.getResources().getColor(R.color.ajk));
				lifeBtn.setTextColor(this.getResources().getColor(R.color.ajk));
			} else if ( curBtn == 2 ) {
				safeFoodBtn.setTextColor(this.getResources().getColor(R.color.ajk));
				doctorBtn.setTextColor(this.getResources().getColor(R.color.ajk_selected));
				lifeBtn.setTextColor(this.getResources().getColor(R.color.ajk));
			} else if ( curBtn == 3 ) {
				safeFoodBtn.setTextColor(this.getResources().getColor(R.color.ajk));
				doctorBtn.setTextColor(this.getResources().getColor(R.color.ajk));
				lifeBtn.setTextColor(this.getResources().getColor(R.color.ajk_selected));
			} 
		}
	}
	
	//分类底部彩色条移动
	private void moveAnimation(final int num) {
		final int offset = ListUtils.dip2px(aty, 10);
		TranslateAnimation animation = new TranslateAnimation(0,
				-(windowsWidth/3-offset)*num, 0, 0);
		animation.setDuration(500);
		btnSelected.startAnimation(animation);
		animation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationEnd(Animation arg0) {
				btnSelected.clearAnimation();
				btnSelected.setX(btnSelected.getX() - (windowsWidth/3-offset) * num);
			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
			}

			@Override
			public void onAnimationStart(Animation arg0) {
			}
		});
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
		if ( (e2.getX() - e1.getX()) > 120 && Math.abs(e2.getY() - e1.getY()) < 50 ) {
			if ( curBtn > 1 ) {
				clickBtn--;
				moveAnimation(curBtn - clickBtn);
				curBtn--;
				
				if ( curBtn == 1 ) {
					safeFoodBtn.setTextColor(this.getResources().getColor(R.color.ajk_selected));
					doctorBtn.setTextColor(this.getResources().getColor(R.color.ajk));
					lifeBtn.setTextColor(this.getResources().getColor(R.color.ajk));
				} else if ( curBtn == 2 ) {
					safeFoodBtn.setTextColor(this.getResources().getColor(R.color.ajk));
					doctorBtn.setTextColor(this.getResources().getColor(R.color.ajk_selected));
					lifeBtn.setTextColor(this.getResources().getColor(R.color.ajk));
				} else if ( curBtn == 3 ) {
					safeFoodBtn.setTextColor(this.getResources().getColor(R.color.ajk));
					doctorBtn.setTextColor(this.getResources().getColor(R.color.ajk));
					lifeBtn.setTextColor(this.getResources().getColor(R.color.ajk_selected));
				} 
			}
		} else if ( (e2.getX() - e1.getX()) < -120 && Math.abs(e2.getY() - e1.getY()) < 50 ) {
			if ( curBtn < 3) {
				clickBtn++;
				moveAnimation(curBtn - clickBtn);
				curBtn++;
				
				if ( curBtn == 1 ) {
					safeFoodBtn.setTextColor(this.getResources().getColor(R.color.ajk_selected));
					doctorBtn.setTextColor(this.getResources().getColor(R.color.ajk));
					lifeBtn.setTextColor(this.getResources().getColor(R.color.ajk));
				} else if ( curBtn == 2 ) {
					safeFoodBtn.setTextColor(this.getResources().getColor(R.color.ajk));
					doctorBtn.setTextColor(this.getResources().getColor(R.color.ajk_selected));
					lifeBtn.setTextColor(this.getResources().getColor(R.color.ajk));
				} else if ( curBtn == 3 ) {
					safeFoodBtn.setTextColor(this.getResources().getColor(R.color.ajk));
					doctorBtn.setTextColor(this.getResources().getColor(R.color.ajk));
					lifeBtn.setTextColor(this.getResources().getColor(R.color.ajk_selected));
				} 
			} else if ( (e2.getX() - e1.getX()) > 50 && Math.abs(e2.getY() - e1.getY()) < 120 ) {
				if ( menuWindow == null ) {
					menuWindow = new PersonalPopupWindow(aty);//显示窗口  
		            menuWindow.showAtLocation(this.findViewById(R.id.private_order), 
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
