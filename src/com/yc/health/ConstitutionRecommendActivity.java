package com.yc.health;

import java.util.ArrayList;
import java.util.List;

import org.kymjs.kjframe.KJActivity;
import org.kymjs.kjframe.ui.BindView;

import com.yc.health.adapter.RecommendGridAdapter;
import com.yc.health.fragment.FamilyPopupWindow;
import com.yc.health.fragment.PersonalPopupWindow;
import com.yc.health.manager.ActivityManager;
import com.yc.health.model.MemberFamilyModel;
import com.yc.health.model.PrivateOrderModel;
import com.yc.health.util.Method;
import com.yc.health.widget.GridCommodity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.PopupWindow.OnDismissListener;

public class ConstitutionRecommendActivity extends KJActivity implements OnGestureListener{

	@BindView(id = R.id.constitution_recommend_food, click = true)
	private GridCommodity foodGrid;
	@BindView(id = R.id.constitution_recommend_recipe, click = true)
	private GridCommodity recipeGrid;
	@BindView(id = R.id.constitution_recommend_sport, click = true)
	private GridCommodity sportGrid;
	
	@BindView(id = R.id.constitution_recommend_add, click = true)
	private ImageView addBtn;
	@BindView(id = R.id.constitution_recommend_back, click = true)
	private ImageView backBtn;
	
	private ArrayList<MemberFamilyModel> family = new ArrayList<MemberFamilyModel>();
	private ArrayList<String> constitutions = new ArrayList<String>();
	private String constitution = null;
	
	private SharedPreferences userPreferences;
	private FamilyPopupWindow menuWindow;//为家人选择的弹出层
	private PersonalPopupWindow personalWindow;
	private GestureDetector gestureDetector;
	
	private RecommendGridAdapter adapter = null;
	private List<PrivateOrderModel> list = null;
	
	@Override
	public void setRootView() {
		setContentView(R.layout.constitution_recommend);
	}

	@SuppressLint("WorldReadableFiles") 
	@SuppressWarnings("deprecation")
	@Override
	public void initData() {
		super.initData();
		
		ActivityManager.getInstace().addActivity(aty);
		
		userPreferences = getSharedPreferences("user", MODE_WORLD_READABLE);
		
		adapter = new RecommendGridAdapter(list,aty);
		
		Bundle bundle = this.getIntent().getExtras();
		constitution = bundle.getString("constitution");
		constitutions.add(constitution);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void initWidget() {
		super.initWidget();
		
		gestureDetector = new GestureDetector(this); // 手势滑动
		
		foodGrid = (GridCommodity) this.findViewById(R.id.constitution_recommend_food);
		recipeGrid = (GridCommodity) this.findViewById(R.id.constitution_recommend_recipe);
		sportGrid = (GridCommodity) this.findViewById(R.id.constitution_recommend_sport);
		
		foodGrid.setAdapter(adapter);
		recipeGrid.setAdapter(adapter);
		sportGrid.setAdapter(adapter);
		
		foodGrid.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				Bundle bundle = new Bundle();
				switch(position) {
				case 0:
					bundle.putString("title", "fruit");
					bundle.putStringArrayList("constitutions", constitutions);
					ConstitutionRecommendActivity.this.showActivity(aty, RecommendListActivity.class, bundle);
					break;
				case 1:
					break;
				case 2:
					break;
				case 3:
					break;
				case 4:
					break;
				case 5:
					break;
				}
			}
		});
		
		recipeGrid.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				switch(position) {
				case 0:
					break;
				case 1:
					break;
				case 2:
					break;
				case 3:
					break;
				case 4:
					break;
				case 5:
					break;
				}
			}
		});
		
		sportGrid.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				switch(position) {
				case 0:
					break;
				case 1:
					break;
				case 2:
					break;
				case 3:
					break;
				case 4:
					break;
				case 5:
					break;
				}
			}
		});
	}

	@Override
	public void widgetClick(View v) {
		super.widgetClick(v);
		
		switch(v.getId()) {
		case R.id.constitution_recommend_back:
			this.finish();
			break;
		case R.id.constitution_recommend_add:
			String loginName = userPreferences.getString("loginName", null);
			if ( loginName != null ) {
				if ( menuWindow == null ) {
					menuWindow = new FamilyPopupWindow(aty,family,constitutions);//显示窗口  
		            menuWindow.showAtLocation(this.findViewById(R.id.constitutionshow), 
		            		Gravity.CENTER, 0, 0); 
		            menuWindow.setOnDismissListener(new OnDismissListener(){
						@Override
						public void onDismiss() {
							Method method = new Method(aty);
							method.backgroundAlpha(1f);
							menuWindow = null;
						}
		            });
				}
			} else {
				//登陆提醒
				Method method = new Method(aty);
				method.loginHint();
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
			if ( personalWindow == null ) {
				personalWindow = new PersonalPopupWindow(aty);//显示窗口  
				personalWindow.showAtLocation(this.findViewById(R.id.constitutionshow), 
	            		Gravity.LEFT | Gravity.BOTTOM, 0, 0); 
				personalWindow.setOnDismissListener(new OnDismissListener(){
					@Override
					public void onDismiss() {
						Method method = new Method(aty);
						method.backgroundAlpha(1f);
						personalWindow = null;
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