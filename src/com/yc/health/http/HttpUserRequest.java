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
import com.yc.health.model.MemberFamilyModel;
import com.yc.health.model.MemberUserModel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class HttpUserRequest extends Thread {

	private Context context;
	private Handler mHandler;
	private int which;
	
	private final int login = 1;
	private final int addMember = 2;
	private final int getMembers = 3;
	private final int deleteMember = 4;
	private final int saveUserInfo = 5;
	private final int getUser = 6;
	private final int updateUserInfo = 7;
	
	private int userId = -1;
	private String loginName = null;
	private String password = null;
	private String memberName = null;
	private String memberConstitution = null;
	private int memberId = 0;
	private String sex = null;
	
	public HttpUserRequest(){
	}
	
	public HttpUserRequest(Context context, Handler mHandler, int which) {
		this.context = context;
		this.mHandler = mHandler;
		this.which = which;
	}
	
	public void loginInit( String loginName, String password ) {
		this.loginName = loginName;
		this.password = password;
	}
	
	public void addMemberInit( int userId, String memberName, String memberConstitution ) {
		this.userId = userId;
		this.memberConstitution = memberConstitution;
		this.memberName = memberName;
	}
	
	public void getMembersInit( int userId ) {
		this.userId = userId;
	}
	
	public void deleteMemberInit( int memberId ) {
		this.memberId = memberId;
	}
	
	public void saveUserInfoInit( int userId, String userName, String sex, 
			String constitution ) {
		this.userId = userId;
		this.memberName = userName;
		this.sex = sex;
		this.memberConstitution = constitution;
	}
	
	public void getUserInit( int userId ) {
		this.userId = userId;
	}
	
	public void updateUserInfoInit( Integer userId, String userName ){
		this.memberName = userName;
		this.userId = userId;
	}
	
	//用户登录判断
	public void loginRequest() {
		HttpParams params = new HttpParams();
		try {
			params.put("loginName", URLEncoder.encode(loginName, "utf-8"));
			params.put("password", URLEncoder.encode(password, "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		HttpConfig config = new HttpConfig();
		config.cacheTime=0;
		config.maxRetries = 10;// 出错重连次数
		KJHttp kjh = new KJHttp(config);
		String url = context.getString(R.string.localhost).concat(
				"/webUser/login");
		kjh.post(url, params, new HttpCallBack() {
			@Override
			public void onFinish() {
				super.onFinish();
			}

			@Override
			public void onSuccess(String t) {
				super.onSuccess(t);
				try {
					Message msg = new Message();
					
					JSONObject mJsonObject = new JSONObject(t);
					String message = mJsonObject.getString("message");
					if ( "noUser".equals(message) ) {
						msg.what = 1;
					} else if ( "pwdIsWrong".equals(message) ) {
						msg.what = 2;
					} else if ( "login".equals(message) ) {
						JSONObject user = mJsonObject.getJSONObject("user");
						
						int userId = user.getInt("membersUserID");
						String loginName = user.getString("loginName");
						String userName = user.getString("userName");
						String sex = user.getString("sex");
						String constitution = user.getString("bodyConstitution");
						
						MemberUserModel userModel = new MemberUserModel();
						userModel.setUserId(userId);
						userModel.setLoginName(loginName);
						userModel.setUserName(userName);
						userModel.setSex(showSex(sex));
						userModel.setConstitution(showConstitution(constitution));
						
						msg.what = 3;
						msg.obj = userModel;
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
	
	//添加家庭成员
	public void addMemberRequest() {
		HttpParams params = new HttpParams();
		try {
			params.put("userId", ""+userId);
			params.put("memberName", URLEncoder.encode(memberName, "utf-8"));
			params.put("memberConstitution", URLEncoder.encode(memberConstitution, "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		HttpConfig config = new HttpConfig();
		config.cacheTime=0;
		config.maxRetries = 0;// 出错重连次数
		KJHttp kjh = new KJHttp(config);
		String url = context.getString(R.string.localhost).concat(
				"/webUser/addMember");
		kjh.post(url, params, new HttpCallBack() {
			@Override
			public void onFinish() {
				super.onFinish();
			}

			@Override
			public void onSuccess(String t) {
				super.onSuccess(t);
				Message msg = new Message();
				msg.what = 1;
				mHandler.sendMessage(msg);
			}

			@SuppressLint("InlinedApi")
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
			}
		});
	}
	
	//获得所有家庭成员
	public void getMembersRequest() {
		HttpParams params = new HttpParams();
		params.put("userId", ""+userId);

		HttpConfig config = new HttpConfig();
		config.cacheTime=0;
		config.maxRetries = 10;// 出错重连次数
		KJHttp kjh = new KJHttp(config);
		String url = context.getString(R.string.localhost).concat(
				"/webUser/getMembers");
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
					JSONArray memberArray = mJsonObject.getJSONArray("members");
					List<MemberFamilyModel> members = new ArrayList<MemberFamilyModel>();
					MemberFamilyModel member = new MemberFamilyModel();
					for ( int i = 0; i < memberArray.length(); i++ ) {
						JSONObject itemObject = memberArray.getJSONObject(i);
						int id = itemObject.getInt("membersFamilyId");
						String memberName = itemObject.getString("membersName");
						String constitution = itemObject.getString("constitution");
						
						member.setConstitution(constitution);
						member.setMembersFamilyId(id);
						member.setMembersName(memberName);
						members.add(member);
					}
					
					Message msg = new Message();
					msg.what = 1;
					msg.obj = members;
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
	
	//删除家庭成员
	public void deleteMemberRequest() {
		HttpParams params = new HttpParams();
		try {
			params.put("memberId", URLEncoder.encode(""+memberId, "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		HttpConfig config = new HttpConfig();
		config.cacheTime=0;
		config.maxRetries = 10;// 出错重连次数
		KJHttp kjh = new KJHttp(config);
		String url = context.getString(R.string.localhost).concat(
				"/webUser/deleteMember");
		kjh.post(url, params, new HttpCallBack() {
			@Override
			public void onFinish() {
				super.onFinish();
			}

			@Override
			public void onSuccess(String t) {
				super.onSuccess(t);
				Message msg = new Message();
				msg.what = 2;
				mHandler.sendMessage(msg);
			}

			@SuppressLint("InlinedApi")
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
			}
		});
	}
	
	//保存会员信息
	public void saveUserRequest() {
		HttpParams params = new HttpParams();
		try {
			params.put("userId", ""+userId);
			params.put("userName", URLEncoder.encode(memberName, "utf-8"));
			params.put("constitution", URLEncoder.encode(memberConstitution, "utf-8"));
			params.put("sex", URLEncoder.encode(sex, "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		HttpConfig config = new HttpConfig();
		config.cacheTime=0;
		config.maxRetries = 0;// 出错重连次数
		KJHttp kjh = new KJHttp(config);
		String url = context.getString(R.string.localhost).concat(
				"/webUser/saveUserInfo");
		kjh.post(url, params, new HttpCallBack() {
			@Override
			public void onFinish() {
				super.onFinish();
			}

			@Override
			public void onSuccess(String t) {
				super.onSuccess(t);
				Message msg = new Message();
				msg.what = 2;
				mHandler.sendMessage(msg);
			}

			@SuppressLint("InlinedApi")
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
			}
		});
	}
	
	//获得当前登录用户
	public void getUserRequest() {
		HttpParams params = new HttpParams();
		if ( userId == -1 ) {
			return;
		}
		params.put("userId", ""+userId);

		HttpConfig config = new HttpConfig();
		config.cacheTime=0;
		config.maxRetries = 0;// 出错重连次数
		KJHttp kjh = new KJHttp(config);
		String url = context.getString(R.string.localhost).concat(
				"/webUser/getUser");
		kjh.post(url, params, new HttpCallBack() {
			@Override
			public void onFinish() {
				super.onFinish();
			}

			@Override
			public void onSuccess(String t) {
				super.onSuccess(t);
				try {
					JSONObject mJsonObject = new JSONObject(t);
					JSONObject user = mJsonObject.getJSONObject("user");
					
					int userId = user.getInt("membersUserID");
					String loginName = user.getString("loginName");
					String password = user.getString("password");
					String userName = user.getString("userName");
					String sex = user.getString("sex");
					String constitution = user.getString("bodyConstitution");
					
					MemberUserModel userModel = new MemberUserModel();
					userModel.setUserId(userId);
					userModel.setLoginName(loginName);
					userModel.setPassword(password);
					userModel.setUserName(userName);
					userModel.setSex(showSex(sex));
					userModel.setConstitution(showConstitution(constitution));
					
					Message msg = new Message();
					msg.what = 1;
					msg.obj = userModel;
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
	
	
	//更新用户名
	public void updateUserRequest() {
		HttpParams params = new HttpParams();
		try {
			params.put("userId", ""+userId);
			params.put("userName", URLEncoder.encode(memberName, "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		HttpConfig config = new HttpConfig();
		config.cacheTime=0;
		config.maxRetries = 0;// 出错重连次数
		KJHttp kjh = new KJHttp(config);
		String url = context.getString(R.string.localhost).concat(
				"/webUser/updateUserInfo");
		kjh.post(url, params, new HttpCallBack() {
			@Override
			public void onFinish() {
				super.onFinish();
			}

			@Override
			public void onSuccess(String t) {
				super.onSuccess(t);
				Message msg = new Message();
				msg.what = 1;
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
		case login:
			this.loginRequest();
			break;
		case addMember:
			this.addMemberRequest();
			break;
		case getMembers:
			this.getMembersRequest();
			break;
		case deleteMember:
			this.deleteMemberRequest();
			break;
		case saveUserInfo:
			this.saveUserRequest();
			break;
		case getUser:
			this.getUserRequest();
			break;
		case updateUserInfo:
			this.updateUserRequest();
			break;
		}
		Looper.loop();
	}
	
	private String showSex( String sex ) {
		if ( "Female".equals(sex) ) {
			return "女";
		} else if ( "Male".equals(sex) ){
			return "男";
		} else {
			return "未填写";
		}
	}
	
	private String showConstitution( String constitution ) {
		if ( "FlatAndQuality".equals(constitution)) {
			return "平和质";
		} else if ( "QiDeficiency".equals(constitution)) {
			return "气虚质";
		} else if ( "YangXuzhi".equals(constitution)) {
			return "阳虚质";
		} else if ( "YinDeficiency".equals(constitution)) {
			return "阴虚质";
		} else if ( "PhlegmDampnessQuality".equals(constitution)) {
			return "痰湿质";
		} else if ( "HotAndhumidQuality".equals(constitution)) {
			return "湿热质";
		} else if ( "BloodStasis".equals(constitution)) {
			return "血瘀质";
		} else if ( "QiStagnation".equals(constitution)) {
			return "气郁质";
		} else if ( "IntrinsicQuality".equals(constitution)) {
			return "特禀质";
		}
		return "未测试";
	}
}
