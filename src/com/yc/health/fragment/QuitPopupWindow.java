package com.yc.health.fragment;

import java.io.File;

import org.kymjs.kjframe.KJActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;

import com.yc.health.LoginActivity;
import com.yc.health.PersonalInfoActivity;
import com.yc.health.R;
import com.yc.health.manager.ActivityManager;
import com.yc.health.util.Method;

public class QuitPopupWindow extends PopupWindow{

	private View mMenuView; 
	private Button quitBtn;
	private Button cancleBtn;
	
	private Context context;
	private String page;
	
	public QuitPopupWindow(Context context, String page){
		this.context = context;
		this.page = page;
		init();
	}
	
	@SuppressWarnings("deprecation")
	private void init() {
		//加载布局
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
		mMenuView = inflater.inflate(R.layout.quit_menu, null);  
		quitBtn = (Button) mMenuView.findViewById(R.id.quit_menu_quit);
		cancleBtn = (Button) mMenuView.findViewById(R.id.quit_menu_cancle);
		
		if ( "image".equals(page) ) {
			quitBtn.setText("相机");
			quitBtn.setTextColor(context.getResources().getColor(R.color.ajk));
			cancleBtn.setText("相册");
		}
		
        this.setContentView(mMenuView);  
        this.setWidth(LayoutParams.FILL_PARENT);  
        this.setHeight(LayoutParams.WRAP_CONTENT);  
        this.setFocusable(true);  
        ColorDrawable dw = new ColorDrawable(Color.WHITE);  
        this.setBackgroundDrawable(dw);  
        this.setAnimationStyle(R.style.quitAnimation);
        Method method = new Method(context);
        method.backgroundAlpha(0.5f);
        mMenuView.setOnTouchListener(new OnTouchListener() {  
            public boolean onTouch(View v, MotionEvent event) {  
                int height = mMenuView.findViewById(R.id.quit_menu).getTop();  
                int y=(int) event.getY();  
                if(event.getAction() == MotionEvent.ACTION_UP){  
                    if(y < height){  
                        dismiss();  
                    }  
                }                 
                return true;  
            }  
        });  
        
        quitBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				if ( "quit".equals(page) ) {
					KJActivity activity = (KJActivity)context;
					activity.showActivity(activity, LoginActivity.class);
					ActivityManager.getInstace().enterHome();
				} else if ( "image".equals(page) ) {
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
							Environment.getExternalStorageDirectory(), "headimg.png")));
					((KJActivity)context).startActivityForResult(intent, PersonalInfoActivity.PHOTOHRAPH);
					dismiss();
				}
			}
        });
        
        cancleBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				if ( "quit".equals(page) ) {
					dismiss();
				} else if ( "image".equals(page) ) {
					Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
					intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
							PersonalInfoActivity.IMAGE_UNSPECIFIED);
					((KJActivity)context).startActivityForResult(intent, PersonalInfoActivity.PHOTOZOOM);
					dismiss();
				}
			}
        });
	}
}