package com.yc.health;

import java.util.List;

import org.kymjs.kjframe.KJActivity;
import org.kymjs.kjframe.ui.BindView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.PopupWindow.OnDismissListener;

import com.yc.health.adapter.RecommendGridAdapter;
import com.yc.health.fragment.PersonalPopupWindow;
import com.yc.health.fragment.TextPopupWindow;
import com.yc.health.manager.ActivityManager;
import com.yc.health.model.PrivateOrderModel;
import com.yc.health.util.Method;
import com.yc.health.widget.GridCommodity;
public class TestResultActivity extends KJActivity implements OnGestureListener{

	@BindView(id = R.id.constitutionshow_constitution)
	private TextView constitutionText;
	@BindView(id = R.id.constitutionshow_food, click = true)
	private GridCommodity foodGrid;
	@BindView(id = R.id.constitutionshow_recipe, click = true)
	private GridCommodity recipeGrid;
	@BindView(id = R.id.constitutionshow_sport, click = true)
	private GridCommodity sportGrid;
	
	@BindView(id = R.id.constitutionshow_back, click = true)
	private ImageView backBtn;
	
	@BindView(id = R.id.constitutionshow_what, click = true)
	private TextView constitutionDesc;
	@BindView(id = R.id.constitutionshow_all, click = true)
	private TextView allConstitution;

	private String constitution = null;
	private TextPopupWindow textWindow;
	
	private PersonalPopupWindow menuWindow = null;
	private GestureDetector gestureDetector;
	
	private RecommendGridAdapter adapter = null;
	private List<PrivateOrderModel> list = null;
	
	@Override
	public void setRootView() {
		setContentView(R.layout.test_result);
	}

	@SuppressLint("WorldReadableFiles") 
	@Override
	public void initData() {
		super.initData();
		
		ActivityManager.getInstace().addActivity(aty);
		
		adapter = new RecommendGridAdapter(list,aty);
		
		Bundle bundle = this.getIntent().getExtras();
		constitution = bundle.getString("constitution");
	}

	@SuppressWarnings("deprecation")
	@Override
	public void initWidget() {
		super.initWidget();
		
		gestureDetector = new GestureDetector(this); // 手势滑动
		
		constitutionText.setText(constitution);
		
		foodGrid = (GridCommodity) this.findViewById(R.id.constitutionshow_food);
		recipeGrid = (GridCommodity) this.findViewById(R.id.constitutionshow_recipe);
		sportGrid = (GridCommodity) this.findViewById(R.id.constitutionshow_sport);
		
		foodGrid.setAdapter(adapter);
		recipeGrid.setAdapter(adapter);
		sportGrid.setAdapter(adapter);
		
		foodGrid.setOnItemClickListener(new OnItemClickListener() {
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
		case R.id.constitutionshow_back:
			this.finish();
			break;
		case R.id.constitutionshow_what:
			if ( textWindow == null ) {
				textWindow = new TextPopupWindow(aty,1);//显示窗口  
				textWindow.showAtLocation(this.findViewById(R.id.constitutionshow), 
	            		Gravity.CENTER, 0, 0); 
				textWindow.setOnDismissListener(new OnDismissListener(){
					@Override
					public void onDismiss() {
						Method method = new Method(aty);
						method.backgroundAlpha(1f);
						textWindow = null;
					}
	            });
			}
			break;
		case R.id.constitutionshow_all:
			if ( textWindow == null ) {
				textWindow = new TextPopupWindow(aty,2);//显示窗口  
				textWindow.showAtLocation(this.findViewById(R.id.constitutionshow), 
	            		Gravity.CENTER, 0, 0); 
				textWindow.setOnDismissListener(new OnDismissListener(){
					@Override
					public void onDismiss() {
						Method method = new Method(aty);
						method.backgroundAlpha(1f);
						textWindow = null;
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
	            menuWindow.showAtLocation(this.findViewById(R.id.test_result), 
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