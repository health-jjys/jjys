package com.yc.health.fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.yc.health.R;
import com.yc.health.util.Method;

public class TextPopupWindow extends PopupWindow{

	private View mMenuView; 
	private TextView text;
	private Button confirmBtn;
	
	private Context context;
	private int which;//1为具体体质介绍，2为九种体质介绍，3为入会需知，4为忘记密码
	
	public TextPopupWindow(Context context, int which){
		this.context = context;
		this.which = which;
		init();
	}
	
	private void init() {
		//加载布局
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
		mMenuView = inflater.inflate(R.layout.text_popupwindow, null);  
		text = (TextView) mMenuView.findViewById(R.id.text_popupwindow_text);
		confirmBtn = (Button) mMenuView.findViewById(R.id.text_popupwindow_confirm);
        this.setContentView(mMenuView);  
        this.setWidth(LayoutParams.WRAP_CONTENT);  
        this.setHeight(LayoutParams.WRAP_CONTENT); 
        this.setFocusable(true);  
        ColorDrawable dw = new ColorDrawable(Color.WHITE);  
        this.setBackgroundDrawable(dw);  
        this.setAnimationStyle(R.style.textAnimation);
        Method method = new Method(context);
        method.backgroundAlpha(0.2f);
        mMenuView.setOnTouchListener(new OnTouchListener() {  
            public boolean onTouch(View v, MotionEvent event) {  
                int height = mMenuView.findViewById(R.id.text_popupwindow).getTop();  
                int y=(int) event.getY();  
                if(event.getAction() == MotionEvent.ACTION_UP){  
                    if(y < height){  
                        dismiss();  
                    }  
                }                 
                return true;  
            }  
        });  
        
        if ( which == 1 ) {
        	text.setText("这是相关的介绍");
        } else if ( which == 2 ) {
        	
        } else if ( which == 3 ) {
        	
        } else if ( which == 4 ) {
        	
        }
        
        confirmBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				dismiss();
			}
        });
	}
}