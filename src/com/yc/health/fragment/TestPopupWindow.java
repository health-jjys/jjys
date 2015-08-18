package com.yc.health.fragment;

import org.kymjs.kjframe.KJActivity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.PopupWindow;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.Spinner;
import android.widget.TextView;

import com.yc.health.R;
import com.yc.health.util.Method;

public class TestPopupWindow extends PopupWindow{

	//省，城市数组
	private int[] city = { R.array.beijin_province_item,
			R.array.tianjin_province_item, R.array.heibei_province_item,
			R.array.shanxi1_province_item, R.array.neimenggu_province_item,
			R.array.liaoning_province_item, R.array.jilin_province_item,
			R.array.heilongjiang_province_item, R.array.shanghai_province_item,
			R.array.jiangsu_province_item, R.array.zhejiang_province_item,
			R.array.anhui_province_item, R.array.fujian_province_item,
			R.array.jiangxi_province_item, R.array.shandong_province_item,
			R.array.henan_province_item, R.array.hubei_province_item,
			R.array.hunan_province_item, R.array.guangdong_province_item,
			R.array.guangxi_province_item, R.array.hainan_province_item,
			R.array.chongqing_province_item, R.array.sichuan_province_item,
			R.array.guizhou_province_item, R.array.yunnan_province_item,
			R.array.xizang_province_item, R.array.shanxi2_province_item,
			R.array.gansu_province_item, R.array.qinghai_province_item,
			R.array.linxia_province_item, R.array.xinjiang_province_item,
			R.array.hongkong_province_item, R.array.aomen_province_item,
			R.array.taiwan_province_item };

	private View mMenuView; 
	
	private TextView sexText;
	private TextView female;
	private TextView male;
	
	private TextView dateText;
	private DatePicker datePicker;
	
	private TextView cityText;
	private Spinner province_spinner;
	private Spinner city_spinner;
	private ArrayAdapter<CharSequence> province_adapter;
	private ArrayAdapter<CharSequence> city_adapter;
	
	private Context context;
	private int which; //1为性别，2为日期，3为省市
	
	public TestPopupWindow(Context context, int which){
		this.context = context;
		this.which = which;
		init();
	}
	
	@SuppressWarnings("deprecation")
	private void init() {
		//加载布局
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
		mMenuView = inflater.inflate(R.layout.test_popupwindow, null);  
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
                int height = mMenuView.findViewById(R.id.test_popupwindow).getTop();  
                int y=(int) event.getY();  
                if(event.getAction() == MotionEvent.ACTION_UP){  
                    if(y < height){  
                        dismiss();  
                    }  
                }                 
                return true;  
            }  
        });  
        
        female = (TextView) mMenuView.findViewById(R.id.test_popupwindow_female);
		male = (TextView) mMenuView.findViewById(R.id.test_popupwindow_male);
		datePicker = (DatePicker) mMenuView.findViewById(R.id.test_popupwindow_date);
		province_spinner = (Spinner) mMenuView.findViewById(R.id.provience_spinner);
		city_spinner = (Spinner) mMenuView.findViewById(R.id.city_spinner);
        if ( which == 1 ) {
			sexText = (TextView) ((KJActivity)context).findViewById(R.id.test_sex);
			female.setVisibility(View.VISIBLE);
			male.setVisibility(View.VISIBLE);
			datePicker.setVisibility(View.GONE);
			province_spinner.setVisibility(View.GONE);
			city_spinner.setVisibility(View.GONE);
			
			female.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View arg0) {
					sexText.setText("女");
					dismiss();
				}
			});
			
			male.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View arg0) {
					sexText.setText("男");
					dismiss();
				}
			});
		} else if ( which == 2 ) {
			dateText = (TextView) ((KJActivity)context).findViewById(R.id.test_inputDate);
			
			female.setVisibility(View.GONE);
			male.setVisibility(View.GONE);
			datePicker.setVisibility(View.VISIBLE);
			province_spinner.setVisibility(View.GONE);
			city_spinner.setVisibility(View.GONE);
		} else if ( which == 3 ) {
			//省市
			cityText = (TextView) ((KJActivity)context).findViewById(R.id.test_city);
			
			female.setVisibility(View.GONE);
			male.setVisibility(View.GONE);
			datePicker.setVisibility(View.GONE);
			province_spinner.setVisibility(View.VISIBLE);
			city_spinner.setVisibility(View.VISIBLE);
			
			loadSpinner();
		}
        
		datePicker.init(2015, 7, 1, new OnDateChangedListener(){
			@Override
			public void onDateChanged(DatePicker view, int year,
                    int monthOfYear, int dayOfMonth) {
				String birthday = year + "-" + (monthOfYear+1) + "-" + dayOfMonth;
				dateText.setText(birthday);
			}
		});
	}
	
	//省、市、地区选择
	private void loadSpinner() {
		province_spinner.setPrompt("请选择省份");
		province_adapter = ArrayAdapter.createFromResource(context,R.array.province_item, android.R.layout.simple_spinner_item);
		province_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		province_spinner.setAdapter(province_adapter);
		province_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				int provinceId = province_spinner.getSelectedItemPosition();
				final String strProvince = province_spinner.getSelectedItem().toString();

				city_spinner.setPrompt("请选择城市");
				select(city_spinner, city_adapter, city[provinceId]);
				city_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
						String strCity = strProvince+"省"+city_spinner.getSelectedItem().toString();
						cityText.setText(strCity);
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
					}
				});
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
	}
		
	//每一个spinner数组适配
    private void select(Spinner spin, ArrayAdapter<CharSequence> adapter,
			int arry) {
		adapter = ArrayAdapter.createFromResource(context, arry,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spin.setAdapter(adapter);
	}
}