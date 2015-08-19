package com.yc.health.fragment;

import java.util.ArrayList;

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
import android.widget.ListView;
import android.widget.PopupWindow;

import com.yc.health.R;
import com.yc.health.adapter.FamilyPopupWindowListAdapter;
import com.yc.health.model.MemberFamilyModel;
import com.yc.health.util.Method;

public class FamilyPopupWindow extends PopupWindow{

	private Context context;
	private View mMenuView; 
	private ListView listView;
	private Button okBtn;
	
	private FamilyPopupWindowListAdapter adapter = null;
	private ArrayList<MemberFamilyModel> family = null;
	private ArrayList<String> constitutions = null;
	
	public FamilyPopupWindow(Context context, ArrayList<MemberFamilyModel> family, ArrayList<String> constitutions){
		this.context = context;
		this.family = family;
		this.constitutions = constitutions;
		init();
	}
	
	@SuppressWarnings("deprecation")
	private void init() {
		//加载布局
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
		mMenuView = inflater.inflate(R.layout.familyselector, null);  
		listView = (ListView) mMenuView.findViewById(R.id.familyselector_list);
		okBtn = (Button) mMenuView.findViewById(R.id.familyselector_ok);
		
        this.setContentView(mMenuView);  
        this.setWidth(LayoutParams.FILL_PARENT);  
        this.setHeight(LayoutParams.WRAP_CONTENT);  
        this.setFocusable(true);  
        ColorDrawable dw = new ColorDrawable(Color.WHITE);  
        this.setBackgroundDrawable(dw);  
        Method method = new Method(context);
        method.backgroundAlpha(0.5f);
        mMenuView.setOnTouchListener(new OnTouchListener() {  
            public boolean onTouch(View v, MotionEvent event) {  
                int height = mMenuView.findViewById(R.id.familyselector).getTop();  
                int y=(int) event.getY();  
                if(event.getAction() == MotionEvent.ACTION_UP){  
                    if(y < height){  
                        dismiss();  
                    }  
                }                 
                return true;  
            }  
        });  
        
        adapter = new FamilyPopupWindowListAdapter(family,context);
        listView.setAdapter(adapter);
        
        okBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				//传送体质
				getConstitutions();
				dismiss();
			}
        });
	}
	
	@SuppressWarnings("static-access")
	public void getConstitutions() {
		for ( int i = 0; i < family.size(); i++ ) {
			if ( adapter.getIsSelected().get(i)) {
				MemberFamilyModel info = family.get(i);
				constitutions.add(info.getConstitution());
			}
		}
	}
}
