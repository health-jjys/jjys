package com.yc.health;

import java.util.ArrayList;
import java.util.List;

import org.kymjs.kjframe.KJActivity;
import org.kymjs.kjframe.ui.BindView;

import com.yc.health.adapter.PrivateOrderDetailGridViewAdapter;
import com.yc.health.fragment.PersonalPopupWindow;
import com.yc.health.http.HttpMemberShoppeRequest;
import com.yc.health.manager.ActivityManager;
import com.yc.health.model.PrivateOrderModel;
import com.yc.health.util.Method;
import com.yc.health.widget.GridCommodity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.webkit.WebView;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.PopupWindow.OnDismissListener;

public class DetailActivity extends KJActivity implements OnGestureListener{

	@BindView(id = R.id.detail_back, click = true)
	private ImageView backBtn;
	@BindView(id = R.id.detail_location, click = true)
	private ImageView locationBtn;
	@BindView(id = R.id.detail_title)
	private TextView titleText;
	
	@BindView(id = R.id.detail_img, click = true)
	private WebView img;
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
	@BindView(id = R.id.detail_message, click = true)
	private ImageView messageBtn;
	@BindView(id = R.id.detail_phone, click = true)
	private ImageView phoneBtn;
	
	@BindView(id = R.id.detail_grid, click = true)
	private GridCommodity recommendGrid;
	
	private SharedPreferences userPreferences;
	private String loginName = null;
	
	private PersonalPopupWindow menuWindow = null;
	private GestureDetector gestureDetector;
	
	private String type = null;
	private List<PrivateOrderModel> list = new ArrayList<PrivateOrderModel>();
	private PrivateOrderDetailGridViewAdapter adapter = null;
	private PrivateOrderModel item = new PrivateOrderModel();
	private Handler mHandler = new Handler(){
		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			if ( msg.what == 1 ) {
				list = (List<PrivateOrderModel>) msg.obj;
				adapter.setList(list);
				adapter.notifyDataSetChanged();
			}
			super.handleMessage(msg);
		}
	};
	
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
		
		Bundle bundle = this.getIntent().getExtras();
		item.setPhone(bundle.getString("phone"));
		item.setAddress(bundle.getString("address"));
		item.setDescription(bundle.getString("description"));
		item.setImagePath(bundle.getString("path"));
		item.setName(bundle.getString("name"));
		
		Method method = new Method();
		type = method.reversePrivateOrderType(bundle.getString("type"));
		adapter = new PrivateOrderDetailGridViewAdapter(list,aty);
		
		HttpMemberShoppeRequest request = new HttpMemberShoppeRequest(aty,mHandler,2);
		request.getPrivateOrderInit(type, 3);
		request.start();
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
		
		
		nameText.setText(item.getName());
		describeText.setText(item.getDescription());
		
		String path = aty.getResources().getString(R.string.localhost)+item.getImagePath();
		img.loadUrl(path);
		img.setBackgroundColor(this.getResources().getColor(R.color.transparent));
		img.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		img.getSettings().setLoadWithOverviewMode(true);
		
		recommendGrid = (GridCommodity) this.findViewById(R.id.detail_grid);
		recommendGrid.setAdapter(adapter);
		recommendGrid.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				Bundle bundle = new Bundle();
				bundle.putInt("id", list.get(position).getPrivateOrderId());
				bundle.putString("name", list.get(position).getName());
				bundle.putString("description", list.get(position).getName());
				bundle.putString("phone", list.get(position).getPhone());
				bundle.putString("address", list.get(position).getName());
				bundle.putString("path", list.get(position).getImagePath());
				bundle.putString("type", list.get(position).getType());
				DetailActivity.this.showActivity(aty, DetailActivity.class, bundle);
			}
		});
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
		case R.id.detail_phone:
			Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:" + item.getPhone()));
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
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
