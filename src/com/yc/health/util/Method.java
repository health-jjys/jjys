package com.yc.health.util;

import java.io.File;

import org.kymjs.kjframe.KJActivity;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.UMImage;
import com.yc.health.LoginActivity;
import com.yc.health.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.view.WindowManager;

public class Method {
	
	private Context context = null;
	
	public Method(){}
	
	public Method(Context context){
		this.context = context;
	}
	
	public void backgroundAlpha(float bgAlpha) {  
		WindowManager.LayoutParams lp = ((Activity)context).getWindow().getAttributes();  
		lp.alpha = bgAlpha; 
    	((Activity)context).getWindow().setAttributes(lp);  
    }
	
	@SuppressLint("InlinedApi") 
	public void loginHint() {
		Builder dialog = new AlertDialog.Builder(context,AlertDialog.THEME_HOLO_LIGHT);
		dialog.setTitle("消息提醒");
		dialog.setMessage("亲,需要登录了才能添加家人哦，请先登录好吗？");
		dialog.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				KJActivity activity = (KJActivity)context;
				activity.showActivity(activity, LoginActivity.class);
			}
		});
		
		dialog.setNegativeButton(R.string.cancle, new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}
	
	public Bitmap getHeadBitmap() {
		String path = Environment.getExternalStorageDirectory() + "/headimg.png";
		File mFile=new File(path);
        //若该文件存在
        if (mFile.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            return bitmap;
        } 
        return null;
	}
	
	public void shareInit() {
		final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share");
		// 设置分享内容
		mController.setShareContent("友盟社会化组件（SDK）让移动应用快速整合社交分享功能，http://www.umeng.com/social");
		// 设置分享图片, 参数2为图片的url地址
		mController.setShareMedia(new UMImage(context, 
		                                      "http://www.umeng.com/images/pic/banner_module_social.png"));
		mController.setAppWebSite(SHARE_MEDIA.RENREN, "http://www.umeng.com/social");
		mController.getConfig().removePlatform( SHARE_MEDIA.RENREN, SHARE_MEDIA.DOUBAN);
		mController.openShare((KJActivity)context, false);
		//		SocializeConstants.APPKEY = "55c1e70967e58e2938000e3a";
//		// 首先在您的Activity中添加如下成员变量
//		final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share",RequestType.SOCIAL);
//		// 设置分享内容
//		mController.setShareContent("快下载郡健养身哦~~");
//		// wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
//		String appID = "wx88818f8c48a95eb4";
//		// 微信图文分享必须设置一个url 
//		String contentUrl = "http://www.umeng.com/social";
//		// 添加微信平台，参数1为当前Activity, 参数2为用户申请的AppID, 参数3为点击分享内容跳转到的目标url
//		UMWXHandler wxHandler = mController.getConfig().supportWXPlatform(context,appID, contentUrl);
//		//设置分享标题
//		wxHandler.setWXTitle("郡健养身");
//		// 支持微信朋友圈
//		UMWXHandler circleHandler = mController.getConfig().supportWXCirclePlatform(context,appID, contentUrl) ;
//		circleHandler.setCircleTitle("郡健养身");
//		
//		//  参数1为当前Activity， 参数2为用户点击分享内容时跳转到的目标地址
//		mController.getConfig().supportQQPlatform((KJActivity)context, "http://www.umeng.com/social");   
//		mController.getConfig().setSsoHandler(new QZoneSsoHandler((KJActivity) context));
//		
//		//设置腾讯微博SSO handler
//		mController.getConfig().setSsoHandler(new TencentWBSsoHandler());
	}
}