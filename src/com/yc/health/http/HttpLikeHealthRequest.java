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
	
	private String types = null;
	
	public HttpLikeHealthRequest(){
	}
	
	public HttpLikeHealthRequest(Context context, Handler mHandler, int which) {
		this.context = context;
		this.mHandler = mHandler;
		this.which = which;
	}
	
	public void getKnowledgeInit( String types ) {
		this.types = types;
	}
	
	//获取健康知识
	public void getKnowledgeRequest() {
		HttpParams params = new HttpParams();
		try {
			params.put("types", URLEncoder.encode(types, "utf-8"));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		
		HttpConfig config = new HttpConfig();
		config.cacheTime=0;
		config.maxRetries = 10;// 出错重连次数
		KJHttp kjh = new KJHttp(config);
		String url = context.getString(R.string.localhost).concat(
				"/appMemberShoppe/getKnowledge");
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
					JSONArray knowledges = mJsonObject.getJSONArray("list");

					ArrayList<KnowledgeModel> list = new ArrayList<KnowledgeModel>();
					KnowledgeModel item = null;
					for (int i = 0; i < knowledges.length(); i++) {
						JSONObject itemObject = knowledges.getJSONObject(i);
						int knowledgeID = itemObject.getInt("knowledgeID");
						String content = itemObject.getString("content");
						String title = itemObject.getString("title");
						String videoUrlPath = itemObject.getString("videoUrlPath");
						String imagePath1 = itemObject.getString("imagePath1");
						String imagePath2 = itemObject.getString("imagePath2");
						String imagePath3 = itemObject.getString("imagePath3");
						String imagePath4 = itemObject.getString("imagePath4");
						String knowledgeType = itemObject.getString("knowledgeType");
						
						item = new KnowledgeModel();
						item.setKnowledgeID(knowledgeID);
						item.setContent(content);
						item.setTitle(title);
						item.setVideoUrlPath(videoUrlPath);
						item.setImagePath1(imagePath1);
						item.setImagePath2(imagePath2);
						item.setImagePath3(imagePath3);
						item.setImagePath4(imagePath4);
						item.setKnowledgeType(knowledgeType);
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
		case getKnowledge:
			this.getKnowledgeRequest(); 
			break;
		}
		Looper.loop();
	}
}
