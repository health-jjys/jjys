package com.yc.health.fragment;

import java.util.ArrayList;

import org.kymjs.kjframe.KJActivity;
import org.kymjs.kjframe.widget.RoundImageView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.yc.health.HomeActivity;
import com.yc.health.PersonalActivity;
import com.yc.health.R;
import com.yc.health.TestActivity;
import com.yc.health.adapter.MyListAdapter;
import com.yc.health.util.Method;

public class PersonalPopupWindow extends PopupWindow{

	private View mMenuView; 
	private TextView nameText;
	private TextView testText;
	private TextView constitutionText;
	private RoundImageView headImg;
	private ListView myList;
	
	private Context context;
	private ArrayList<String> textList = new ArrayList<String>();
	private ArrayList<Integer> icons = new ArrayList<Integer>();
	private MyListAdapter adapter = null;
	
	private SharedPreferences userPreferences;
	private String loginName = null;
	
	public PersonalPopupWindow(Context context){
		this.context = context;
		adapterDataInit();
		adapter = new MyListAdapter(textList,icons,context,2);
		init();
	}
	
	@SuppressLint("WorldReadableFiles") 
	@SuppressWarnings({ "static-access", "deprecation" })
	private void adapterDataInit() {
		userPreferences = ((KJActivity)context).getSharedPreferences("user", 
				((KJActivity)context).MODE_WORLD_READABLE);
		loginName = userPreferences.getString("loginName", null);
		
		textList.add("回到首页");
		textList.add("体质测试");
		textList.add("软件设置");
		textList.add("关于我们");
		
		icons.add(R.drawable.icon_grxx);
		icons.add(R.drawable.icon_grxx);
		icons.add(R.drawable.icon_wdjr);
		icons.add(R.drawable.icon_sc);
		
		if ( loginName != null ) {
			textList.add("个人信息");
			textList.add("资讯预约");
			
			icons.add(R.drawable.icon_fxrj);
			icons.add(R.drawable.icon_sz);
		} 
	}

	@SuppressWarnings("deprecation")
	private void init() {
		//加载布局
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
		mMenuView = inflater.inflate(R.layout.leftmenu_popupwindow, null); 
		if ( loginName != null ) {
			nameText = (TextView) mMenuView.findViewById(R.id.leftmenu_name);
			testText = (TextView) mMenuView.findViewById(R.id.leftmenu_test_result);
			constitutionText = (TextView) mMenuView.findViewById(R.id.leftmenu_constitution);
			headImg = (RoundImageView) mMenuView.findViewById(R.id.leftmenu_headimg);
			
			String userName = userPreferences.getString("userName", null);
			String constitution = userPreferences.getString("constitution", null);
			loginName = userPreferences.getString("loginName", null);
			nameText.setText(userName);
			if ( constitution != null ) {
				constitutionText.setText(constitution);
				testText.setText("已测试");
			}
			
			Method method = new Method(context);
			Bitmap bm = method.getHeadBitmap();
			BitmapDrawable bd= new BitmapDrawable(context.getResources(), bm);
			headImg.setImageDrawable(bd);
		} 
		
        this.setContentView(mMenuView);  
        Rect rect = new Rect();  
        ((KJActivity)context).getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        int windowsHeight = ((KJActivity)context).getWindowManager().getDefaultDisplay().getHeight();
        int windowsWidth = ((KJActivity)context).getWindowManager().getDefaultDisplay().getWidth();
        this.setWidth(windowsWidth*3/4);  
        this.setHeight(windowsHeight-rect.top);  
        this.setFocusable(true);  
        ColorDrawable dw = new ColorDrawable(context.getResources().getColor(R.color.bg));  
        this.setBackgroundDrawable(dw);  
        this.setAnimationStyle(R.style.PersonalAnimation);
        Method method = new Method(context);
        method.backgroundAlpha(0.5f);
        mMenuView.setOnTouchListener(new OnTouchListener() {  
            public boolean onTouch(View v, MotionEvent event) {  
                int height = mMenuView.findViewById(R.id.leftmenu).getTop();  
                int y=(int) event.getY();  
                if(event.getAction() == MotionEvent.ACTION_UP){  
                    if(y < height){  
                        dismiss();  
                    }  
                }                 
                return true;  
            }  
        });  
        
        myList = (ListView) mMenuView.findViewById(R.id.leftmenu_list);
		myList.setAdapter(adapter);
		myList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				switch(position){
				case 0:
					com.yc.health.manager.ActivityManager.getInstace().enterHome();
					((KJActivity)context).showActivity((KJActivity)context, HomeActivity.class);
					break;
				case 1:
					Bundle bundle = new Bundle();
					bundle.putString("who", "mine");
					((KJActivity)context).showActivity((KJActivity)context, TestActivity.class, bundle);
					break;
				case 2:
					break;
				case 3:
					break;
				case 4:
					KJActivity activity1 = (KJActivity)context;
					activity1.showActivity(activity1, PersonalActivity.class);
					break;
				case 5:
					break;
				}
			}
		});
	}
}