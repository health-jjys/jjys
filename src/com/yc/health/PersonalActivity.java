package com.yc.health;

import java.util.ArrayList;
import java.util.Random;

import org.kymjs.kjframe.KJActivity;
import org.kymjs.kjframe.ui.BindView;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.yc.health.adapter.MyListAdapter;
import com.yc.health.fragment.PersonalPopupWindow;
import com.yc.health.http.HttpUserRequest;
import com.yc.health.manager.ActivityManager;
import com.yc.health.model.MemberUserModel;
import com.yc.health.util.Logutil;
import com.yc.health.util.Method;

public class PersonalActivity extends KJActivity implements OnGestureListener,AMapLocationListener{
	private LocationManagerProxy mLocationManagerProxy;
	@BindView(id = R.id.personal_back, click = true)
	private ImageView backBtn;
	@BindView(id = R.id.personal_headimg, click = true)
	private ImageView headImg;
	@BindView(id = R.id.personal_name, click = true)
	private TextView nameText;
	@BindView(id = R.id.personal_constitution, click = true)
	private TextView constitutionText;
	@BindView(id = R.id.personal_location, click = true)
	private TextView locationText;
	@BindView(id = R.id.personal_code, click = true)
	private ImageView qrCodeBtn;
	@BindView(id = R.id.my_info, click = true)
	private ListView myList;
	private Random mRandom=new Random();
	private ArrayList<String> textList = null;
	private ArrayList<Integer> icons = null;
	private MyListAdapter adapter = null;
	private SharedPreferences userPreferences;
	private int userId = -1;
	
	private PersonalPopupWindow menuWindow = null;
	private GestureDetector gestureDetector;
	
	@SuppressLint("HandlerLeak") 
	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			if ( msg.what == 1 ) {
				MemberUserModel user = (MemberUserModel) msg.obj;
				if ( user.getUserName() == null ) {
					nameText.setText(user.getLoginName());
				} else {
					nameText.setText(user.getUserName());
				}
				constitutionText.setText(user.getConstitution());
			}
			super.handleMessage(msg);
		}
	};
	
	@Override
	public void setRootView() {
		setContentView(R.layout.personal);
		initloc();
	}

	private void initloc() {
		// 初始化定位，只采用网络定位
		mLocationManagerProxy = LocationManagerProxy.getInstance(this);
		mLocationManagerProxy.setGpsEnable(false);
		// 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
		// 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用removeUpdates()方法来取消定位请求
		// 在定位结束后，在合适的生命周期调用destroy()方法
		// 其中如果间隔时间为-1，则定位只定一次,
		//在单次定位情况下，定位无论成功与否，都无需调用removeUpdates()方法移除请求，定位sdk内部会移除
		mLocationManagerProxy.requestLocationData(
				LocationProviderProxy.AMapNetwork, 6*1000, 100, this);
		Logutil.Log("StartLocMgr");
		mHandler.postDelayed(getloc, 1000);
	}
	Runnable getloc= new Runnable() {
		public void run() {
			Logutil.Log("getloc once");
			locationText.setText("正在获取当前位置");
			mLocationManagerProxy.removeUpdates(PersonalActivity.this);
			int randomTime=mRandom.nextInt(1000);
			mLocationManagerProxy.setGpsEnable(false);
			mLocationManagerProxy.requestLocationData(
					LocationProviderProxy.AMapNetwork, 6*1000, 100, PersonalActivity.this);
			
		}
	};
	@Override
	protected void onStop() {
		super.onStop();
	}
	
	@SuppressLint("WorldReadableFiles") 
	@SuppressWarnings("deprecation")
	@Override
	public void initData() {
		super.initData();
		
		ActivityManager.getInstace().addActivity(aty);
		
		textList = new ArrayList<String>();
		icons = new ArrayList<Integer>();
		
		adapter = new MyListAdapter(textList,icons,aty,1);
		
		init();
		
		userPreferences = getSharedPreferences("user", MODE_WORLD_READABLE);
		userId = userPreferences.getInt("userId", -1);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mLocationManagerProxy.destroy();
	}

	private void init() {
		textList.add("个人信息");
		textList.add("我的家人");
		textList.add("收藏");
		textList.add("分享软件");
		textList.add("设置");
		
		icons.add(R.drawable.icon_grxx);
		icons.add(R.drawable.icon_wdjr);
		icons.add(R.drawable.icon_sc);
		icons.add(R.drawable.icon_fxrj);
		icons.add(R.drawable.icon_sz);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void initWidget() {
		super.initWidget();
		
		gestureDetector = new GestureDetector(this); // 手势滑动
		
		myList = (ListView) this.findViewById(R.id.my_info);
		myList.setAdapter(adapter);
		myList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				switch(position){
				case 0:
					PersonalActivity.this.showActivity(aty, PersonalInfoActivity.class);
					break;
				case 1:
					PersonalActivity.this.showActivity(aty, FamilyActivity.class);
					break;
				case 2:
					break;
				case 3:
//					Intent shareInt=new Intent(Intent.ACTION_SEND);
//					shareInt.setType("text/plain");   
//					shareInt.putExtra(Intent.EXTRA_SUBJECT, "分享");   
//					shareInt.putExtra(Intent.EXTRA_TEXT, "快下载郡健养生吧，带给你健康的人生~~");    
//					shareInt.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
//					startActivity(shareInt);
					Method method = new Method(aty);
					method.shareInit();
					break;
				case 4:
					break;
				}
			}
		});
	}

	@Override
	public void widgetClick(View v) {
		super.widgetClick(v);
		
		switch(v.getId()) {
		case R.id.personal_back:
			this.finish();
			break;
		case R.id.personal_location:
			mHandler.postDelayed(getloc, 1000);
			break;
		}
	}
	
	@Override
	protected void onResume() {
		Method method = new Method(aty);
		Bitmap bm = method.getHeadBitmap();
		BitmapDrawable bd= new BitmapDrawable(aty.getResources(), bm);
		headImg.setImageDrawable(bd);
		
		HttpUserRequest request = new HttpUserRequest(aty,mHandler,6);
		request.getUserInit(userId);
		request.start();
		
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
	            menuWindow.showAtLocation(this.findViewById(R.id.personal), 
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


	@Override
	public void onLocationChanged(Location arg0) {
		Logutil.Log(arg0.getLongitude()+"_lat:"+arg0.getLatitude());
	}


	@Override
	public void onProviderDisabled(String arg0) {
	}


	@Override
	public void onProviderEnabled(String arg0) {
	}


	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
	}


	@Override
	public void onLocationChanged(AMapLocation arg0) {
		if (arg0!=null&&arg0.getAMapException().getErrorCode() == 0) {
			Logutil.Log("locChgAmp_"+arg0.getProvince()+"_"+arg0.getCity());
			locationText.setText(arg0.getProvince()+",\n"+arg0.getCity());
		}else {
			Logutil.Log("LocErr:"+arg0.getAMapException().getErrorMessage());
			Logutil.ShowToast(PersonalActivity.this, "定位错误："+arg0.getAMapException().getMessage(),true);
		}
	}
	

}

