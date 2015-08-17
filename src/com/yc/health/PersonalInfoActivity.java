package com.yc.health;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.kymjs.kjframe.KJActivity;
import org.kymjs.kjframe.ui.BindView;
import org.kymjs.kjframe.widget.RoundImageView;

import com.yc.health.fragment.PersonalPopupWindow;
import com.yc.health.fragment.QuitPopupWindow;
import com.yc.health.http.HttpUserRequest;
import com.yc.health.manager.ActivityManager;
import com.yc.health.util.Method;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.PopupWindow.OnDismissListener;

public class PersonalInfoActivity extends KJActivity implements OnGestureListener{

	@BindView(id = R.id.personalinfo_back, click = true)
	private ImageView backBtn;
	@BindView(id = R.id.personalinfo_headimg, click = true)
	private RoundImageView headImg;
	@BindView(id = R.id.personalinfo_name)
	private EditText nameText;
	@BindView(id = R.id.personalinfo_account)
	private TextView accountText;
	@BindView(id = R.id.personalinfo_sex)
	private TextView sexText;
	@BindView(id = R.id.personalinfo_quit, click = true)
	private ImageView quitBtn;
	
	private PersonalPopupWindow menuWindow = null;
	private GestureDetector gestureDetector;
	
	private SharedPreferences userPreferences;
	private int userId = -1;
	private String userName = null;
	private String loginName = null;
	private String sex = null;
	
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
		}
	};
	
	//头像替换
	public static final int NONE = 0;
	public static final int PHOTOHRAPH = 1;// 拍照
	public static final int PHOTOZOOM = 2; // 缩放
	public static final int PHOTORESOULT = 3;// 结果
	public static final String IMAGE_UNSPECIFIED = "image/*";
	
	@Override
	public void setRootView() {
		setContentView(R.layout.personalinfo);
	}

	@SuppressLint("WorldReadableFiles") 
	@SuppressWarnings("deprecation")
	@Override
	public void initData() {
		super.initData();
		
		ActivityManager.getInstace().addActivity(aty);
		
		userPreferences = getSharedPreferences("user", MODE_WORLD_READABLE);
		userId = userPreferences.getInt("userId", -1);
		userName = userPreferences.getString("userName", null);
		loginName = userPreferences.getString("loginName", null);
		sex = userPreferences.getString("sex", null);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void initWidget() {
		super.initWidget();
		
		gestureDetector = new GestureDetector(this); // 手势滑动
		
		Method method = new Method(aty);
		Bitmap bm = method.getHeadBitmap();
		BitmapDrawable bd= new BitmapDrawable(aty.getResources(), bm);
		headImg.setImageDrawable(bd);
		
		
		accountText.setText(loginName);
		sexText.setText(sex);
		nameText.setText(userName);
	}

	@Override
	public void widgetClick(View v) {
		super.widgetClick(v);
		
		switch(v.getId()) {
		case R.id.personalinfo_back:
			this.finish();
			break;
		case R.id.personalinfo_headimg:
			QuitPopupWindow menuWindow = new QuitPopupWindow(aty,"image");//显示窗口  
            menuWindow.showAtLocation(this.findViewById(R.id.personal_info), 
            		Gravity.BOTTOM, 0, 0); 
            menuWindow.setOnDismissListener(new OnDismissListener(){
				@Override
				public void onDismiss() {
					Method method = new Method(aty);
					method.backgroundAlpha(1f);
				}
            });
			break;
		case R.id.personalinfo_quit:
			QuitPopupWindow menuWindow1 = new QuitPopupWindow(aty,"quit");//显示窗口  
            menuWindow1.showAtLocation(this.findViewById(R.id.personal_info), 
            		Gravity.BOTTOM, 0, 0); 
            menuWindow1.setOnDismissListener(new OnDismissListener(){
				@Override
				public void onDismiss() {
					Method method = new Method(aty);
					method.backgroundAlpha(1f);
				}
            });
			break;
		case R.id.personalinfo_name:
			nameText.setCursorVisible(true);
			break;
		}
	}
	
	@Override
	protected void onPause() {
		Editor editor = userPreferences.edit();
		editor.putString("userName", nameText.getText().toString());
		editor.commit();
		
		HttpUserRequest request = new HttpUserRequest(aty,mHandler,7);
		request.updateUserInfoInit(userId,nameText.getText().toString());
		request.start();
		super.onPause();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == NONE)
			return;
		// 拍照
		if (requestCode == PHOTOHRAPH) {
			// 设置文件保存路径这里放在跟目录下
			File picture = new File(Environment.getExternalStorageDirectory()
					+ aty.getResources().getString(R.string.headimg));
			startPhotoZoom(Uri.fromFile(picture));
		}

		if (data == null)
			return;

		// 读取相册缩放图片
		if (requestCode == PHOTOZOOM) {
			startPhotoZoom(data.getData());
		}
		// 处理结果
		if (requestCode == PHOTORESOULT) {
			Bundle extras = data.getExtras();
			if (extras != null) {
				Bitmap photo = extras.getParcelable("data");
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				photo.compress(Bitmap.CompressFormat.PNG, 75, stream);
				headImg.setImageBitmap(photo);
				
				File path = new File(Environment.getExternalStorageDirectory()
						+ aty.getResources().getString(R.string.headimg));
				if (  path.exists() ) { 
					path.delete(); 
				} 
				try { 
					FileOutputStream out = new FileOutputStream(path); 
					photo.compress(Bitmap.CompressFormat.PNG, 75, out); 
					out.flush(); 
					out.close(); 
				} catch (FileNotFoundException e) { 
					e.printStackTrace(); 
				} catch (IOException e) { 
					e.printStackTrace(); 
				} 
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	public void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 120);
		intent.putExtra("outputY", 120);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, PHOTORESOULT);
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
	            menuWindow.showAtLocation(this.findViewById(R.id.personal_info), 
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
