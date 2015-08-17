package com.yc.health;

import java.util.ArrayList;

import org.kymjs.kjframe.KJActivity;
import org.kymjs.kjframe.ui.BindView;

import com.yc.health.adapter.MemberShoppeListAdapter;
import com.yc.health.fragment.PersonalPopupWindow;
import com.yc.health.http.HttpMemberShoppeRequest;
import com.yc.health.manager.ActivityManager;
import com.yc.health.model.MemberShoppeModel;
import com.yc.health.util.ListUtils;
import com.yc.health.util.Method;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.PopupWindow.OnDismissListener;

public class MemberShoppeActivity extends KJActivity implements OnGestureListener{

	@BindView(id = R.id.private_order_back, click = true)
	private ImageView backBtn;
	@BindView(id = R.id.private_order_title, click = true)
	private ImageView titleImg;
	@BindView(id = R.id.private_order_item1, click = true)
	private Button foodBtn;
	@BindView(id = R.id.private_order_item2, click = true)
	private Button textileBtn;
	@BindView(id = R.id.private_order_item3, click = true)
	private Button washBtn;
	@BindView(id = R.id.private_order_btn_selected, click = true)
	private ImageView btnSelected;
	
	@BindView(id = R.id.private_order_list, click = true)
	private ListView listView;
	
	private int curBtn = 1;//1为饮食，2为医疗，3为生活管家
	private int clickBtn = 1;
	private int windowsWidth;
	
	private PersonalPopupWindow menuWindow = null;
	private GestureDetector gestureDetector;
	
	private String type = null;
	private ArrayList<MemberShoppeModel> list = new ArrayList<MemberShoppeModel>();
	private MemberShoppeListAdapter adapter;
	private Handler mHandler = new Handler() {
		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			if ( msg.what == 1 ) {
				list = (ArrayList<MemberShoppeModel>) msg.obj;
				adapter.setList(list);
				adapter.notifyDataSetChanged();
			}
			super.handleMessage(msg);
		}
	};
	
	@Override
	public void setRootView() {
		setContentView(R.layout.private_order);
	}

	@Override
	public void initData() {
		super.initData();
		
		ActivityManager.getInstace().addActivity(aty);
		
		adapter = new MemberShoppeListAdapter(list,aty);
		
		type = "food";
		HttpMemberShoppeRequest request = new HttpMemberShoppeRequest(aty,mHandler,1);
		request.getMemberShoppeInit(type,-1);
		request.start();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void initWidget() {
		super.initWidget();
		
		gestureDetector = new GestureDetector(this); // 手势滑动
		
		titleImg.setBackgroundResource(R.drawable.title_splb);
		foodBtn.setText("食品");
		textileBtn.setText("家纺");
		washBtn.setText("洗护");
		
		// 分类下的彩色条
		gestureDetector = new GestureDetector(this); // 手势滑动
		windowsWidth = this.getWindowManager().getDefaultDisplay().getWidth();
		int offset = ListUtils.dip2px(aty, 30);
		btnSelected.getLayoutParams().width = windowsWidth/3-offset;
				
		listView = (ListView) this.findViewById(R.id.private_order_list);
		listView.setAdapter(adapter);
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
				MemberShoppeActivity.this.showActivity(aty, TypeDetailActivity.class, bundle);
			}
		});
	}

	@Override
	public void widgetClick(View v) {
		super.widgetClick(v);
		
		if ( v.getId() == R.id.private_order_back ) {
			this.finish();
		} else {
			if ( v.getId() == R.id.private_order_item1 ) {
				if ( curBtn != 1) {
					clickBtn = 1;
					moveAnimation(curBtn - clickBtn);
					curBtn = 1;
				}
			} else if ( v.getId() == R.id.private_order_item2 ) {
				if ( curBtn != 2) {
					clickBtn = 2;
					moveAnimation(curBtn - clickBtn);
					curBtn = 2;
				}
			} else if ( v.getId() == R.id.private_order_item3 ) {
				if ( curBtn != 3) {
					clickBtn = 3;
					moveAnimation(curBtn - clickBtn);
					curBtn = 3;
				}
			}
			
			if ( curBtn == 1 ) {
				foodBtn.setTextColor(this.getResources().getColor(R.color.ajk_selected));
				textileBtn.setTextColor(this.getResources().getColor(R.color.ajk));
				washBtn.setTextColor(this.getResources().getColor(R.color.ajk));
				
				type = "food";
				HttpMemberShoppeRequest request = new HttpMemberShoppeRequest(aty,mHandler,1);
				request.getMemberShoppeInit(type,-1);
				request.start();
			} else if ( curBtn == 2 ) {
				foodBtn.setTextColor(this.getResources().getColor(R.color.ajk));
				textileBtn.setTextColor(this.getResources().getColor(R.color.ajk_selected));
				washBtn.setTextColor(this.getResources().getColor(R.color.ajk));
				
				type = "textile";
				HttpMemberShoppeRequest request = new HttpMemberShoppeRequest(aty,mHandler,1);
				request.getMemberShoppeInit(type,-1);
				request.start();
			} else if ( curBtn == 3 ) {
				foodBtn.setTextColor(this.getResources().getColor(R.color.ajk));
				textileBtn.setTextColor(this.getResources().getColor(R.color.ajk));
				washBtn.setTextColor(this.getResources().getColor(R.color.ajk_selected));
				
				type = "wash";
				HttpMemberShoppeRequest request = new HttpMemberShoppeRequest(aty,mHandler,1);
				request.getMemberShoppeInit(type,-1);
				request.start();
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
				btnSelected.setX(btnSelected.getX() - (windowsWidth/3 -offset) * num);
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
					foodBtn.setTextColor(this.getResources().getColor(R.color.ajk_selected));
					textileBtn.setTextColor(this.getResources().getColor(R.color.ajk));
					washBtn.setTextColor(this.getResources().getColor(R.color.ajk));
					
					type = "food";
					HttpMemberShoppeRequest request = new HttpMemberShoppeRequest(aty,mHandler,1);
					request.getMemberShoppeInit(type,-1);
					request.start();
				} else if ( curBtn == 2 ) {
					foodBtn.setTextColor(this.getResources().getColor(R.color.ajk));
					textileBtn.setTextColor(this.getResources().getColor(R.color.ajk_selected));
					washBtn.setTextColor(this.getResources().getColor(R.color.ajk));
					
					type = "textile";
					HttpMemberShoppeRequest request = new HttpMemberShoppeRequest(aty,mHandler,1);
					request.getMemberShoppeInit(type,-1);
					request.start();
				} else if ( curBtn == 3 ) {
					foodBtn.setTextColor(this.getResources().getColor(R.color.ajk));
					textileBtn.setTextColor(this.getResources().getColor(R.color.ajk));
					washBtn.setTextColor(this.getResources().getColor(R.color.ajk_selected));
					
					type = "wash";
					HttpMemberShoppeRequest request = new HttpMemberShoppeRequest(aty,mHandler,1);
					request.getMemberShoppeInit(type,-1);
					request.start();
				} 
			}
		} else if ( (e2.getX() - e1.getX()) < -120 && Math.abs(e2.getY() - e1.getY()) < 50 ) {
			if ( curBtn < 3) {
				clickBtn++;
				moveAnimation(curBtn - clickBtn);
				curBtn++;
				
				if ( curBtn == 1 ) {
					foodBtn.setTextColor(this.getResources().getColor(R.color.ajk_selected));
					textileBtn.setTextColor(this.getResources().getColor(R.color.ajk));
					washBtn.setTextColor(this.getResources().getColor(R.color.ajk));
					
					type = "food";
					HttpMemberShoppeRequest request = new HttpMemberShoppeRequest(aty,mHandler,1);
					request.getMemberShoppeInit(type,-1);
					request.start();
				} else if ( curBtn == 2 ) {
					foodBtn.setTextColor(this.getResources().getColor(R.color.ajk));
					textileBtn.setTextColor(this.getResources().getColor(R.color.ajk_selected));
					washBtn.setTextColor(this.getResources().getColor(R.color.ajk));
					
					type = "textile";
					HttpMemberShoppeRequest request = new HttpMemberShoppeRequest(aty,mHandler,1);
					request.getMemberShoppeInit(type,-1);
					request.start();
				} else if ( curBtn == 3 ) {
					foodBtn.setTextColor(this.getResources().getColor(R.color.ajk));
					textileBtn.setTextColor(this.getResources().getColor(R.color.ajk));
					washBtn.setTextColor(this.getResources().getColor(R.color.ajk_selected));
					
					type = "wash";
					HttpMemberShoppeRequest request = new HttpMemberShoppeRequest(aty,mHandler,1);
					request.getMemberShoppeInit(type,-1);
					request.start();
				} 
			} if ( (e2.getX() - e1.getX()) > 50 && Math.abs(e2.getY() - e1.getY()) < 120 ) {
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
