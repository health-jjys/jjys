package com.yc.health.adapter;

import java.util.ArrayList;
import java.util.List;

import com.yc.health.R;
import com.yc.health.model.MemberFamilyModel;
import com.yc.health.widget.CustomToast;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class FamilyPopupWindowListAdapter extends BaseAdapter{

	private static List<Boolean> isSelected = new ArrayList<Boolean>();
	private int checkNum = 0;
	
	private List<MemberFamilyModel> family = null;
	private Context context = null;
	private LayoutInflater inflater = null;
	
	public FamilyPopupWindowListAdapter(){}
	
	public FamilyPopupWindowListAdapter(List<MemberFamilyModel> family,Context context){
		this.family = family;
		inflater = LayoutInflater.from(context);
		this.context = context;
		for(int i = 0; i < 6; i++) {  
            getIsSelected().add(false); 
        }
	}
	
	@Override
	public int getCount() {
		if ( family != null ) {
			return family.size();
		} else {
			return 0;
		}
	}

	@Override
	public Object getItem(int position) {
		return family.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	private static class SelectorItem {
		CheckBox selector;
		ImageButton deleteBtn;
		TextView info;
	}

	@SuppressLint("ResourceAsColor") 
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final SelectorItem selectorItem;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.selector_list_item, null);
			selectorItem = new SelectorItem();
			selectorItem.selector = (CheckBox) convertView.findViewById(R.id.familyselector_item_select);
			selectorItem.info = (TextView) convertView.findViewById(R.id.familyselector_info_item);
			selectorItem.deleteBtn = (ImageButton) convertView.findViewById(R.id.familyselector_item_delete);
			convertView.setTag(selectorItem);
		} else {
			selectorItem = (SelectorItem) convertView.getTag();
		}
		selectorItem.deleteBtn.setVisibility(View.GONE);

		MemberFamilyModel info = family.get(position);
		String s1 = info.getMembersName();
		String s2 = info.getConstitution();
		selectorItem.info.setText(Html.fromHtml(s1+"&nbsp;&nbsp;&nbsp;&nbsp;"+"<font color='grade'>"+s2+"</font>"));
		
		selectorItem.selector.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean flag) {
				if ( flag ) {
					if ( checkNum < 3 ) {
						checkNum++;
						selectorItem.selector.setBackgroundResource(R.drawable.radio_rdo_selected);
						getIsSelected().set(position, true);
					} else {
						CustomToast.showToast(context, "只能选择3位家人，请取消一个再进行选择！", 6000);
						selectorItem.selector.setChecked(false);
					}
				} else {
					checkNum--;
					selectorItem.selector.setBackgroundResource(R.drawable.radio_rdo_nor);
					getIsSelected().set(position, false);
				}
			}
		});
		
		return convertView;
	}
	
	public List<MemberFamilyModel> getFamily() {
		return family;
	}

	public void setFamily(List<MemberFamilyModel> family) {
		this.family = family;
	}

	public static List<Boolean> getIsSelected() {
		return isSelected;
	}

	public static void setIsSelected(List<Boolean> isSelected) {
		FamilyPopupWindowListAdapter.isSelected = isSelected;
	}
}
