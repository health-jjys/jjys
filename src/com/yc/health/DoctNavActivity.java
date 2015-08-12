package com.yc.health;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.kymjs.kjframe.KJActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.model.LatLngBounds.Builder;
import com.baidu.mapapi.overlayutil.PoiOverlay;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BNRoutePlanNode.CoordinateType;
import com.baidu.navisdk.adapter.BaiduNaviManager;
import com.baidu.navisdk.adapter.BaiduNaviManager.NaviInitListener;
import com.baidu.navisdk.adapter.BaiduNaviManager.RoutePlanListener;
import com.yc.health.util.Logutil;

public class DoctNavActivity extends KJActivity implements
OnGetGeoCoderResultListener {
	private String mSDCardPath = null;
	private String APP_FOLDER_NAME;
	private PoiSearch mPoiSearch = null;
	GeoCoder mSearch = null;
	private SuggestionSearch mSuggestionSearch = null;
	private LocationMode tempMode=LocationMode.Hight_Accuracy;
	private LocationClient mLocationClient;
	private String tempcoor="bd09ll";
	// 定位相关
	LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	private LocationMode mCurrentMode;
	BitmapDescriptor mCurrentMarker;
	private String sAddCity="乌鲁木齐市";
	private String sAddLine2="汇通大厦";
	public static final String ROUTE_PLAN_NODE = "routePlanNode";
	private static final String LTAG = DoctNavActivity.class.getSimpleName();
	boolean isFirstLoc = true;// 是否首次定位
	boolean isStartNav =false;//是否已经开始导航
	private LatLng lTargetAdd,lMyLoc;
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	
	@Override
	public void setRootView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.doctnavilayout);
		mMapView = (MapView) findViewById(R.id.bmapView);

		mBaiduMap = mMapView.getMap();
		APP_FOLDER_NAME=getString(R.string.app_sd_cache_dir);
		// 初始化搜索模块，注册事件监听
		mSearch = GeoCoder.newInstance();
		mSearch.setOnGetGeoCodeResultListener(this);
		mSearch.geocode(new GeoCodeOption().city(sAddCity).address(sAddLine2));
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		// 定位初始化
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(1000);
		mLocClient.setLocOption(option);
		mLocClient.start();
	}
	
	private boolean initDirs() {
		mSDCardPath = getSdcardDir();
		if ( mSDCardPath == null ) {
			return false;
		}
		File f = new File(mSDCardPath, APP_FOLDER_NAME);
		if ( !f.exists() ) {
			try {
				f.mkdir();
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}
	
	String authinfo = null;
	
	private void initNavi() {
		BaiduNaviManager.getInstance().setNativeLibraryPath(mSDCardPath + "/"+APP_FOLDER_NAME+"_SO");
		BaiduNaviManager.getInstance().init(this, mSDCardPath,APP_FOLDER_NAME,
			new NaviInitListener() {
            @Override
            public void onAuthResult(int status, String msg) {
                if (0 == status) {
                    authinfo = "key校验成功!";
                } else {
                    authinfo = "key校验失败, " + msg;
                }
                Logutil.Log(authinfo);
//                BNDemoMainActivity.this.runOnUiThread(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        Toast.makeText(BNDemoMainActivity.this, authinfo, Toast.LENGTH_LONG).show();
//                    }
//                });

            }
				public void initSuccess() {
					//Logutil.Log("百度导航引擎初始化成功");
					//Logutil.ShowToast(DoctNavActivity.this, "百度导航引擎初始化成功", false);
					if ( BaiduNaviManager.isNaviInited()&&!isStartNav) {
						isStartNav=true;
						
					}
				}
				
				public void initStart() {
					Logutil.ShowToast(DoctNavActivity.this, "百度导航引擎初始化开始", false);
				}
				
				public void initFailed() {
					Logutil.ShowToast(DoctNavActivity.this, "百度导航引擎初始化失败", false);
				}
		}, null /*mTTSCallback*/);
	}
	private void routeplanToNavi(CoordinateType coType) {
	    BNRoutePlanNode sNode = null;
	    BNRoutePlanNode eNode = null;
	    if (lMyLoc==null||lTargetAdd==null) {
			Logutil.Log("addr null");
	    	return;
		}
		sNode = new BNRoutePlanNode(lMyLoc.longitude,lMyLoc.latitude,  
	    		"我的位置", null);
		eNode = new BNRoutePlanNode(lTargetAdd.longitude,lTargetAdd.latitude,  
	    		sAddLine2, null);
		if (sNode != null && eNode != null) {
	        List<BNRoutePlanNode> list = new ArrayList<BNRoutePlanNode>();
	        list.add(sNode);
	        list.add(eNode);
	        BaiduNaviManager.getInstance().launchNavigator(this, list, 1, true, new DemoRoutePlanListener(sNode));
		}
	}

	public class DemoRoutePlanListener implements RoutePlanListener {
		
		private BNRoutePlanNode mBNRoutePlanNode = null;
		public DemoRoutePlanListener(BNRoutePlanNode node){
			mBNRoutePlanNode = node;
		}
		
		@Override
		public void onJumpToNavigator() {
			 Intent intent = new Intent(DoctNavActivity.this, DoctNavGuide.class);
			 Bundle bundle = new Bundle();
			 bundle.putSerializable(ROUTE_PLAN_NODE, (BNRoutePlanNode) mBNRoutePlanNode);
			 intent.putExtras(bundle);
             startActivity(intent);
		}
		@Override
		public void onRoutePlanFailed() {
			// TODO Auto-generated method stub
			
		}
	}
	
	private String getSdcardDir() {
		if (Environment.getExternalStorageState().equalsIgnoreCase(
				Environment.MEDIA_MOUNTED)) {
			return Environment.getExternalStorageDirectory().toString();
		}
		return null;
	}

@Override
protected void onDestroy() {
	// TODO Auto-generated method stub
	try {
		mMapView.onDestroy();
		mSuggestionSearch.destroy();	
	} catch (Exception e) {
		// TODO: handle exception
	}
	
	super.onDestroy();
}
@Override
protected void onPause() {
	// TODO Auto-generated method stub
	mMapView.onPause();
	super.onPause();
}

@Override
protected void onResume() {
	// TODO Auto-generated method stub
	mMapView.onResume();
	super.onResume();
}


private class MyPoiOverlay extends PoiOverlay {

	public MyPoiOverlay(BaiduMap baiduMap) {
		super(baiduMap);
	}

	@Override
	public boolean onPoiClick(int index) {
		super.onPoiClick(index);
		PoiInfo poi = getPoiResult().getAllPoi().get(index);
		// if (poi.hasCaterDetails) {
			mPoiSearch.searchPoiDetail((new PoiDetailSearchOption())
					.poiUid(poi.uid));
		// }
		return true;
	}
}
/**
 * 定位SDK监听函数
 */
public class MyLocationListenner implements BDLocationListener {

	@Override
	public void onReceiveLocation(BDLocation location) {
		// map view 销毁后不在处理新接收的位置
		if (location == null || mMapView == null)
			return;
		MyLocationData locData = new MyLocationData.Builder()
				.accuracy(location.getRadius())
				// 此处设置开发者获取到的方向信息，顺时针0-360
				.direction(100).latitude(location.getLatitude())
				.longitude(location.getLongitude()).build();
		mBaiduMap.setMyLocationData(locData);
		if (isFirstLoc) {
			mLocClient.stop();
			isFirstLoc = false;
			LatLng ll = new LatLng(location.getLatitude(),
					location.getLongitude());
			MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
			mBaiduMap.animateMapStatus(u);
			lMyLoc=ll;
			if ( initDirs() ) {
				initNavi();
				Logutil.Log("初始化导航一次");
				initclicker();
			}
		}

	}

	public void onReceivePoi(BDLocation poiLocation) {
	}
}
private void initclicker() {
	Button btnstart=(Button)findViewById(R.id.btnstartnav);
	btnstart.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			routeplanToNavi(CoordinateType.BD09_MC);
		}
	});
}
@Override
public void onGetGeoCodeResult(GeoCodeResult result) {
	// TODO Auto-generated method stub
	Logutil.Log("geo:"+result.getLocation().latitude+" lon:"+result.getLocation().longitude);
	//43.796516 lon:87.607873
	LatLng ll;
	BitmapDescriptor bd = BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding);
	LatLngBounds.Builder builder = new Builder();
	ll = new LatLng(result.getLocation().latitude, result.getLocation().longitude);
	OverlayOptions oo = new MarkerOptions().icon(bd).position(ll);
	mBaiduMap.addOverlay(oo);
	builder.include(ll);
	LatLngBounds bounds = builder.build();
	MapStatusUpdate u = MapStatusUpdateFactory.newLatLngBounds(bounds);
	mBaiduMap.animateMapStatus(u);
	lTargetAdd=ll;
}

@Override
public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
	// TODO Auto-generated method stub
	Logutil.Log("geoRev"+result.getLocation().latitude+" lon:"+result.getLocation().longitude);
}
}
