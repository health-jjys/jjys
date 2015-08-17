package com.yc.health.http;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpConfig;
import org.kymjs.kjframe.http.HttpParams;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.yc.health.R;
import com.yc.health.model.MemberShoppeModel;
import com.yc.health.model.PrivateOrderModel;
import com.yc.health.util.Method;

public class HttpMemberShoppeRequest extends Thread {

	private Context context;
	private Handler mHandler;
	private int which;
	
	private final int getMemberShoppe = 1;
	private final int getPrivateOrder = 2;
	private final int recommendation = 3;
	private final int getKnowledge = 4;
	
	private String type = null;
	private int num = -1;
	private int recommendTypeId = -1;
	private String constitutions = null;
	
	public HttpMemberShoppeRequest(){
	}
	
	public HttpMemberShoppeRequest(Context context, Handler mHandler, int which) {
		this.context = context;
		this.mHandler = mHandler;
		this.which = which;
	}
	
	public void getMemberShoppeInit(String type, int num) {
		this.type = type;
		this.num = num;
	}
	
	public void getPrivateOrderInit(String type, int num) {
		this.type = type;
		this.num = num;
	}
	
	public void getRecommendationInit(int recommendTypeId, String constitutions) {
		this.recommendTypeId = recommendTypeId;
		this.constitutions = constitutions;
	}
	
	//获取对应类型的list集
	public void getMemberShoppeRequest() {
		HttpParams params = new HttpParams();
		try {
			params.put("type", URLEncoder.encode(type, "utf-8"));
			params.put("num", ""+num);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		HttpConfig config = new HttpConfig();
		config.cacheTime=0;
		config.maxRetries = 10;// 出错重连次数
		KJHttp kjh = new KJHttp(config);
		String url = context.getString(R.string.localhost).concat(
				"/request/getMemberShoppe");
		kjh.get(url, params, new HttpCallBack() {
			@Override
			public void onFinish() {
				super.onFinish();
			}

			@Override
			public void onSuccess(String t) {
				super.onSuccess(t);
				try {
					JSONObject mJsonObject = new JSONObject(t);
					JSONArray memberShoppeList = mJsonObject.getJSONArray("list");
					ArrayList<MemberShoppeModel> list = new ArrayList<MemberShoppeModel>();
					MemberShoppeModel item = null;
					for (int i = 0; i < memberShoppeList.length(); i++) {
						JSONObject itemObject = memberShoppeList.getJSONObject(i);
						int memberShoppeId = itemObject.getInt("memberShoppeId");
						String name = itemObject.getString("name");
						String imagePath = itemObject.getString("imagePath");
						String description = itemObject.getString("description");
						String address = itemObject.getString("address");
						String type = itemObject.getString("productType");
						
						item = new MemberShoppeModel();
						item.setAddress(address);
						item.setDescription(description);
						item.setImagePath(imagePath);
						item.setMemberShoppeId(memberShoppeId);
						item.setName(name);
						Method method = new Method();
						item.setType(method.showMemberShppeType(type));
						list.add(item);
					}
					
					Message msg = new Message();
					msg.what = 1;
					msg.obj = list;
					mHandler.sendMessage(msg);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@SuppressLint("InlinedApi")
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
			}
		});
	}
	
	//获取私人定制对应类型的list集
	public void getPrivateOrderRequest() {
		HttpParams params = new HttpParams();
		try {
			params.put("type", URLEncoder.encode(type, "utf-8"));
			params.put("num", ""+num);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		HttpConfig config = new HttpConfig();
		config.cacheTime=0;
		config.maxRetries = 10;// 出错重连次数
		KJHttp kjh = new KJHttp(config);
		String url = context.getString(R.string.localhost).concat(
				"/request/getPrivateOrder");
		kjh.get(url, params, new HttpCallBack() {
			@Override
			public void onFinish() {
				super.onFinish();
			}

			@Override
			public void onSuccess(String t) {
				super.onSuccess(t);
				try {
					JSONObject mJsonObject = new JSONObject(t);
					JSONArray privateOrderList = mJsonObject.getJSONArray("list");
					ArrayList<PrivateOrderModel> list = new ArrayList<PrivateOrderModel>();
					PrivateOrderModel item = null;
					for (int i = 0; i < privateOrderList.length(); i++) {
						JSONObject itemObject = privateOrderList.getJSONObject(i);
						int privateOrderId = itemObject.getInt("privateOrderId");
						String name = itemObject.getString("name");
						String imagePath = itemObject.getString("imagePath");
						String description = itemObject.getString("description");
						String address = itemObject.getString("address");
						String phone = itemObject.getString("phone");
						String type = itemObject.getString("privateOrderType");
						
						item = new PrivateOrderModel();
						item.setAddress(address);
						item.setDescription(description);
						item.setImagePath(imagePath);
						item.setPrivateOrderId(privateOrderId);
						item.setName(name);
						item.setPhone(phone);
						Method method = new Method();
						item.setType(method.showPrivateOrderType(type));
						list.add(item);
					}
					
					Message msg = new Message();
					msg.what = 1;
					msg.obj = list;
					mHandler.sendMessage(msg);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@SuppressLint("InlinedApi")
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
			}
		});
	}
	
	@Override
	public void run() {
		super.run();
		
		Looper.prepare();
		switch(which) {
		case getMemberShoppe:
			this.getMemberShoppeRequest();
			break;
		case getPrivateOrder:
			this.getPrivateOrderRequest();
			break;
		}
		Looper.loop();
	}
}