package com.yc.health.http;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpConfig;
import org.kymjs.kjframe.http.HttpParams;

import com.yc.health.R;
import com.yc.health.model.CollectionModel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class HttpUserRequest2 extends Thread {

	private Context context;
	private Handler mHandler;
	private int which;
	
	private final int addCollection = 1;
	private final int getCollection = 2;
	private final int isCollection = 3;
	private final int deleteCollection = 4;
	
	private int userID = -1;
	private int knowID = -1;
	private String types = null;
	private int collectionID = -1;
	
	public HttpUserRequest2(){
	}
	
	public HttpUserRequest2(Context context, Handler mHandler, int which) {
		this.context = context;
		this.mHandler = mHandler;
		this.which = which;
	}
	
	public void addCollectionInit( int userID, int knowID, String types ) {
		this.userID = userID;
		this.knowID = knowID;
		this.types = types;
	}
	
	public void getCollectionInit( int userID, String types ) {
		this.userID = userID;
		this.types = types;
	}
	
	public void isCollectionInit( int userID, int knowID, String types ) {
		this.userID = userID;
		this.knowID = knowID;
		this.types = types;
	}
	
	public void deleteCollectionInit( int collectionID ) {
		this.collectionID = collectionID;
	}

	//添加收藏
	public void addCollectionRequest() {
		HttpParams params = new HttpParams();
		try {
			params.put("userID", ""+userID);
			params.put("knowID", ""+knowID);
			params.put("types", URLEncoder.encode(types, "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		HttpConfig config = new HttpConfig();
		config.cacheTime=0;
		config.maxRetries = 0;// 出错重连次数
		KJHttp kjh = new KJHttp(config);
		String url = context.getString(R.string.localhost).concat(
				"/appMemberShoppe/addCollection");
		kjh.get(url, params, new HttpCallBack() {
			@Override
			public void onFinish() {
				super.onFinish();
			}

			@Override
			public void onSuccess(String t) {
				super.onSuccess(t);
				Message msg = new Message();
				msg.what = 4;
				mHandler.sendMessage(msg);
			}

			@SuppressLint("InlinedApi")
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
			}
		});
	}
	
	//查询收藏
	public void getCollectionRequest() {
		HttpParams params = new HttpParams();
		try {
			params.put("userID", ""+userID);
			params.put("types", URLEncoder.encode(types, "utf-8"));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

		HttpConfig config = new HttpConfig();
		config.cacheTime=0;
		config.maxRetries = 10;// 出错重连次数
		KJHttp kjh = new KJHttp(config);
		String url = context.getString(R.string.localhost).concat(
				"/appMemberShoppe/searchCollectionByComm");
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
					JSONArray collectionArray = mJsonObject.getJSONArray("list");
					List<CollectionModel> collections = new ArrayList<CollectionModel>();
					CollectionModel collection = new CollectionModel();
					for ( int i = 0; i < collectionArray.length(); i++ ) {
						JSONObject itemObject = collectionArray.getJSONObject(i);
						int collectionID = itemObject.getInt("collectionID");
						String title = itemObject.getString("title");
						String content = itemObject.getString("content");
						String url = itemObject.getString("url");
						String phone = itemObject.getString("phone");
						String address = itemObject.getString("address");
						
						collection.setCollectionID(collectionID);
						collection.setTitle(title);
						collection.setContent(content);
						collection.setUrl(url);
						collection.setPhone(phone);
						collection.setAddress(address);
						collections.add(collection);
					}
					
					Message msg = new Message();
					msg.what = 1;
					msg.obj = collections;
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
		
	//是否收藏
	public void isCollectionRequest() {
		HttpParams params = new HttpParams();
		try {
			params.put("userID", ""+userID);
			params.put("knowID", ""+knowID);
			params.put("types", URLEncoder.encode(types, "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		HttpConfig config = new HttpConfig();
		config.cacheTime=0;
		config.maxRetries = 0;// 出错重连次数
		KJHttp kjh = new KJHttp(config);
		String url = context.getString(R.string.localhost).concat(
				"/appMemberShoppe/isCollected");
		kjh.post(url, params, new HttpCallBack() {
			@Override
			public void onFinish() {
				super.onFinish();
			}

			@Override
			public void onSuccess(String t) {
				super.onSuccess(t);
				try {
					JSONObject flagObject = new JSONObject(t);
					
					Message msg = new Message();
					boolean isCollected = flagObject.getBoolean("isCollected");
					if ( isCollected ) {
						msg.what = 2;
						msg.obj = flagObject.getInt("collectionID");
					} else {
						msg.what = 3;
					}
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
	
	//删除收藏
	public void deleteCollectionRequest() {
		HttpParams params = new HttpParams();
		params.put("collectionID", ""+collectionID);

		HttpConfig config = new HttpConfig();
		config.cacheTime=0;
		config.maxRetries = 0;// 出错重连次数
		KJHttp kjh = new KJHttp(config);
		String url = context.getString(R.string.localhost).concat(
				"/appMemberShoppe/deleteCollection");
		kjh.get(url, params, new HttpCallBack() {
			@Override
			public void onFinish() {
				super.onFinish();
			}

			@Override
			public void onSuccess(String t) {
				super.onSuccess(t);
				Message msg = new Message();
				msg.what = 5;
				mHandler.sendMessage(msg);
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
		case addCollection:
			this.addCollectionRequest();
			break;
		case getCollection:
			this.getCollectionRequest();
			break;
		case isCollection:
			this.isCollectionRequest();
			break;
		case deleteCollection:
			this.deleteCollectionRequest();
			break;
		}
		Looper.loop();
	}
}