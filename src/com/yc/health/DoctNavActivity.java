package com.yc.health;

import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviPath;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.model.NaviLatLng;
import com.amap.api.navi.view.RouteOverLay;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.GeocodeSearch.OnGeocodeSearchListener;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.yc.health.util.Logutil;
import com.yc.health.util.amaputils;

public class DoctNavActivity extends Activity implements AMapLocationListener,AMapNaviListener,View.OnClickListener, LocationSource, OnGeocodeSearchListener {
	private String sAddCity="乌鲁木齐市";
	private String sAddLine2="汇通大厦";
	private String addressName;
	private AMap mAMap;
	private AMapNavi mAMapNavi;
	// 起点终点坐标
	private NaviLatLng mNaviStart;
	private NaviLatLng mNaviEnd;
	// 起点终点列表
	private ArrayList<NaviLatLng> mStartPoints = new ArrayList<NaviLatLng>();
	private ArrayList<NaviLatLng> mEndPoints = new ArrayList<NaviLatLng>();
	// 规划线路
	private RouteOverLay mRouteOverLay;
	// 是否驾车和是否计算成功的标志
	private boolean mIsDriveMode = true;
	private boolean mIsCalculateRouteSuccess = false;
	private TextView start_position_textview;
	private TextView end_position_textview;
	private Button car_navi_route;
	private Button car_navi_emulator;
	private Button car_navi_navi;
	private Button foot_navi_route;
	private Button foot_navi_emulator;
	private Button foot_navi_navi;
	private MapView simple_route_map;
	private Handler mHandler=null;
	private LocationManagerProxy mLocationManagerProxy;
	private Random mRandom=new Random();
	private ProgressDialog progDialog = null;
	private GeocodeSearch geocoderSearch;
	private Marker geoMarker;
	private Marker regeoMarker;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.doctnavilayout);
		mHandler=new Handler(){

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch (msg.what) {
				case 1://更新我的坐标
					mLocationManagerProxy.removeUpdates(DoctNavActivity.this);
					start_position_textview.setText(mNaviStart.getLatitude()+","+mNaviStart.getLongitude());
					Logutil.Log("=========="+mAMap.getScalePerPixel());
					CameraUpdate arg0= CameraUpdateFactory.newLatLngZoom(new LatLng(mNaviStart.getLatitude(), mNaviStart.getLongitude()), (float) 10000);
					mAMap.animateCamera(arg0);
					break;
				case 2://更新目的地坐标
					end_position_textview.setText(mNaviEnd.getLatitude()+","+mNaviEnd.getLongitude());
					break;
				default:
					break;
				}
			}};
		initviews(savedInstanceState);
		mHandler.postDelayed(getloc, 1000);
		initloc();
	}
	private Runnable getloc=new Runnable() {
		public void run() {
			Logutil.Log("getloc once");
			mLocationManagerProxy.removeUpdates(DoctNavActivity.this);
			int randomTime=mRandom.nextInt(1000);
			mLocationManagerProxy.requestLocationData(
					LocationProviderProxy.AMapNetwork, 60*1000+randomTime, -1, DoctNavActivity.this);
			mLocationManagerProxy.setGpsEnable(false);
		}
	};
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
				LocationProviderProxy.AMapNetwork, 60*1000, -1, DoctNavActivity.this);
		Logutil.Log("StartLocMgr");
		mHandler.postDelayed(getloc, 1000);
	}
	private void initviews(Bundle savedInstanceState){
		start_position_textview = (TextView) findViewById(R.id.start_position_textview);
		end_position_textview = (TextView) findViewById(R.id.end_position_textview);
		mNaviEnd= new NaviLatLng(39.983456, 116.3154950);
		car_navi_emulator=(Button)findViewById(R.id.car_navi_emulator);
		car_navi_emulator.setOnClickListener(this);
		car_navi_navi=(Button)findViewById(R.id.car_navi_navi);
		car_navi_navi.setOnClickListener(this);
		car_navi_route=(Button)findViewById(R.id.car_navi_route);
		car_navi_route.setOnClickListener(this);
		foot_navi_emulator=(Button)findViewById(R.id.foot_navi_emulator);
		foot_navi_emulator.setOnClickListener(this);
		foot_navi_navi=(Button)findViewById(R.id.foot_navi_navi);
		foot_navi_navi.setOnClickListener(this);
		foot_navi_route=(Button)findViewById(R.id.foot_navi_route);
		foot_navi_route.setOnClickListener(this);
		mAMapNavi = AMapNavi.getInstance(this);
		mAMapNavi.setAMapNaviListener(this);
		mStartPoints.add(mNaviStart);
		mEndPoints.add(mNaviEnd);
		simple_route_map=(MapView)findViewById(R.id.simple_route_map);
		if (simple_route_map==null) {
			Logutil.Log("mMap null");
		}
		if (savedInstanceState==null) {
			Logutil.Log("bundle null");
		}
		simple_route_map.onCreate(savedInstanceState);
		if (mAMap==null) {
			mAMap = simple_route_map.getMap();
//			geoMarker = mAMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
//					.icon(BitmapDescriptorFactory
//							.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
//			regeoMarker = mAMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
//					.icon(BitmapDescriptorFactory
//							.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
		}
		
		mRouteOverLay = new RouteOverLay(mAMap, null);
		mAMap.setLocationSource(this);// 设置定位监听
		mAMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
		mAMap.setMyLocationType(AMap.LOCATION_TYPE_MAP_ROTATE);//设置定位类型
		//mAMap.getUiSettings().setMyLocationButtonEnabled(true);
		geocoderSearch = new GeocodeSearch(this);
		geocoderSearch.setOnGeocodeSearchListener(this);
		progDialog = new ProgressDialog(this);
		getLatlon(sAddCity,sAddLine2);
	}
	//计算驾车路线
		private void calculateDriveRoute() {
			boolean isSuccess = mAMapNavi.calculateDriveRoute(mStartPoints,
					mEndPoints, null, AMapNavi.DrivingDefault);
			if (!isSuccess) {
				Logutil.ShowToast(DoctNavActivity.this, "路线计算失败,检查参数情况",true);
			}

		}
		//计算步行路线
		private void calculateFootRoute() {
			boolean isSuccess = mAMapNavi.calculateWalkRoute(mNaviStart, mNaviEnd);
			if (!isSuccess) {
				Logutil.ShowToast(DoctNavActivity.this, "路线计算失败,检查参数情况",true);
			}
		}
		private void startEmulatorNavi(boolean isDrive) {
			if ((isDrive && mIsDriveMode && mIsCalculateRouteSuccess)
					|| (!isDrive && !mIsDriveMode && mIsCalculateRouteSuccess)) {
				Intent emulatorIntent = new Intent(DoctNavActivity.this,
						SimpleNaviActivity.class);
				Bundle bundle = new Bundle();
				bundle.putBoolean(amaputils.ISEMULATOR, true);
				bundle.putInt(amaputils.ACTIVITYINDEX, amaputils.SIMPLEROUTENAVI);
				emulatorIntent.putExtras(bundle);
				emulatorIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(emulatorIntent);

			} else {
				Logutil.ShowToast(DoctNavActivity.this,"请先进行相对应的路径规划，再进行导航",true);
			}
		}

		private void startGPSNavi(boolean isDrive) {
			if ((isDrive && mIsDriveMode && mIsCalculateRouteSuccess)
					|| (!isDrive && !mIsDriveMode && mIsCalculateRouteSuccess)) {
				Intent gpsIntent = new Intent(DoctNavActivity.this,
						SimpleNaviActivity.class);
				Bundle bundle = new Bundle();
				bundle.putBoolean(amaputils.ISEMULATOR, false);
				bundle.putInt(amaputils.ACTIVITYINDEX, amaputils.SIMPLEROUTENAVI);
				gpsIntent.putExtras(bundle);
				gpsIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(gpsIntent);
			} else {
				Logutil.ShowToast(DoctNavActivity.this,"请先进行相对应的路径规划，再进行导航",true);
			}
		}
	@Override
	public void onArriveDestination() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onArrivedWayPoint(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCalculateRouteFailure(int arg0) {
		// TODO Auto-generated method stub
		Logutil.ShowToast(DoctNavActivity.this, "路径规划出错" + arg0,true);
		mIsCalculateRouteSuccess = false;
	}

	@Override
	public void onCalculateRouteSuccess() {
		// TODO Auto-generated method stub
		AMapNaviPath naviPath = mAMapNavi.getNaviPath();
		if (naviPath == null) {
			return;
		}
		// 获取路径规划线路，显示到地图上
		mRouteOverLay.setRouteInfo(naviPath);
		mRouteOverLay.addToMap();
		mIsCalculateRouteSuccess = true;
	}

	@Override
	public void onEndEmulatorNavi() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGetNavigationText(int arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGpsOpenStatus(boolean arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onInitNaviFailure() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onInitNaviSuccess() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLocationChange(AMapNaviLocation arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNaviInfoUpdate(NaviInfo arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNaviInfoUpdated(AMapNaviInfo arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReCalculateRouteForTrafficJam() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReCalculateRouteForYaw() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStartNavi(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTrafficStatusUpdate() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()) {
		case R.id.car_navi_route:
			mIsCalculateRouteSuccess = false;
			mIsDriveMode = true;
			calculateDriveRoute();
			break;
		case R.id.car_navi_emulator:
			startEmulatorNavi(true);
			break;
		case R.id.car_navi_navi:
			startGPSNavi(true);
			break;
		case R.id.foot_navi_route:
			mIsCalculateRouteSuccess = false;
			mIsDriveMode = false;
			calculateFootRoute();
			break;
		case R.id.foot_navi_emulator:
			startEmulatorNavi(false);
			break;
		case R.id.foot_navi_navi:
			startGPSNavi(false);
			break;
		}
	}
	//------------------生命周期重写函数---------------------------	

		@Override
		public void onResume() {
			super.onResume();
			simple_route_map.onResume();

		}

		@Override
		public void onPause() {
			super.onPause();
			simple_route_map.onPause();

		}

		@Override
		public void onDestroy() {
			super.onDestroy();
			simple_route_map.onDestroy();
			//删除监听 
			AMapNavi.getInstance(this).removeAMapNaviListener(this);
		 
		}
	
		@Override
		public void onLocationChanged(Location arg0) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void onProviderDisabled(String arg0) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void onProviderEnabled(String arg0) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void onLocationChanged(AMapLocation arg0) {
			// TODO Auto-generated method stub
			mNaviStart=new NaviLatLng(arg0.getLatitude(), arg0.getLongitude());
			Logutil.Log("got myloc");
			mStartPoints.clear();
			mStartPoints.add(mNaviStart);
			mHandler.sendEmptyMessage(1);//get myloc
		}
		@Override
		public void activate(OnLocationChangedListener arg0) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void deactivate() {
			// TODO Auto-generated method stub
			
		}
		/**
		 * 显示进度条对话框
		 */
		public void showDialog() {
			progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progDialog.setIndeterminate(false);
			progDialog.setCancelable(true);
			progDialog.setMessage("正在获取地址");
			progDialog.show();
		}

		/**
		 * 隐藏进度条对话框
		 */
		public void dismissDialog() {
			if (progDialog != null) {
				progDialog.dismiss();
			}
		}
		@Override
		public void onGeocodeSearched(GeocodeResult result, int rCode) {
			// TODO Auto-generated method stub
			dismissDialog();
			if (rCode == 0) {
				if (result != null && result.getGeocodeAddressList() != null
						&& result.getGeocodeAddressList().size() > 0) {
					GeocodeAddress address = result.getGeocodeAddressList().get(0);
					mAMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
							amaputils.convertToLatLng(address.getLatLonPoint()), 15));
//					geoMarker.setPosition(amaputils.convertToLatLng(address
//							.getLatLonPoint()));
					addressName = "经纬度值:" + address.getLatLonPoint() + "\n位置描述:"
							+ address.getFormatAddress();
					mNaviEnd.setLatitude(address.getLatLonPoint().getLatitude());
					mNaviEnd.setLongitude(address.getLatLonPoint().getLongitude());
					mHandler.sendEmptyMessage(2);
					Logutil.Log("target====="+address.getLatLonPoint().toString());
					Logutil.ShowToast(DoctNavActivity.this, addressName,false);
				} else {
					Logutil.ShowToast(DoctNavActivity.this, "未找到结果",true);
				}

			} else if (rCode == 27) {
				Logutil.ShowToast(DoctNavActivity.this, "网络错误",true);
			} else if (rCode == 32) {
				Logutil.ShowToast(DoctNavActivity.this, "证书错误",true);
			} else {
				Logutil.ShowToast(DoctNavActivity.this, "其他错误："+rCode,true);
			}
		}
		@Override
		public void onRegeocodeSearched(RegeocodeResult arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}
		/**
		 * 响应地理编码
		 */
		public void getLatlon(final String city,final String saddr) {
			showDialog();
			GeocodeQuery query = new GeocodeQuery(saddr, city);// 第一个参数表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode，
			geocoderSearch.getFromLocationNameAsyn(query);// 设置同步地理编码请求
			
		}
}
