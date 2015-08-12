package com.yc.health.http;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpConfig;

import com.yc.health.R;
import com.yc.health.model.KnowledgeModel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class HttpLikeHealthRequest extends Thread {

	private Context context;
	private Handler mHandler;
	private int which;
	
	private final int getKnowledge = 1;
	private final int getVideo = 2;
	
	public HttpLikeHealthRequest(){
	}
	
	public HttpLikeHealthRequest(Context context, Handler mHandler, int which) {
		this.context = context;
		this.mHandler = mHandler;
		this.which = which;
	}
	
	//获取健康知识
	public void getKnowledgeRequest() {
		HttpConfig config = new HttpConfig();
		config.cacheTime=0;
		config.maxRetries = 10;// 出错重连次数
		KJHttp kjh = new KJHttp(config);
		String url = context.getString(R.string.localhost).concat(
				"/likeHealth/getKnowledge");
		kjh.get(url, new HttpCallBack() {
			@Override
			public void onFinish() {
				super.onFinish();
			}

			@Override
			public void onSuccess(String t) {
				super.onSuccess(t);
				try {
					JSONObject mJsonObject = new JSONObject(t);
					JSONArray shopcommodities = mJsonObject.getJSONArray("shopcommodities");

					ArrayList<KnowledgeModel> list = new ArrayList<KnowledgeModel>();
					KnowledgeModel item = null;

					for (int i = 0; i < shopcommodities.length(); i++) {
						JSONObject itemObject = shopcommodities
								.getJSONObject(i);
						int commCode = itemObject.getInt("commCode");
						String imagePath = itemObject.getString("shopCommImage");
						float special = (float) itemObject.getDouble("special");
						
						item = new KnowledgeModel();

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
	
	//获取健康视频
	public void getVideoRequest() {
		HttpConfig config = new HttpConfig();
		config.cacheTime=0;
		config.maxRetries = 10;// 出错重连次数
		KJHttp kjh = new KJHttp(config);
		String url = context.getString(R.string.localhost).concat(
				"/likeHealth/getKnowledge");
		kjh.get(url, new HttpCallBack() {
			@Override
			public void onFinish() {
				super.onFinish();
			}

			@Override
			public void onSuccess(String t) {
				super.onSuccess(t);
				try {
					JSONObject mJsonObject = new JSONObject(t);
					JSONArray shopcommodities = mJsonObject.getJSONArray("shopcommodities");

					ArrayList<KnowledgeModel> list = new ArrayList<KnowledgeModel>();
					KnowledgeModel item = null;

					for (int i = 0; i < shopcommodities.length(); i++) {
						JSONObject itemObject = shopcommodities
								.getJSONObject(i);
						int commCode = itemObject.getInt("commCode");
						String imagePath = itemObject.getString("shopCommImage");
						float special = (float) itemObject.getDouble("special");
						
						item = new KnowledgeModel();

						list.add(item);
					}
					
					Message msg = new Message();
					msg.what = 2;
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
		case getKnowledge:
			break;
		case getVideo:
			break;
		}
		Looper.loop();
	}
}
