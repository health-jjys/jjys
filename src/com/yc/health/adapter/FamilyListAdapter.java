package com.yc.health.adapter;

import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import com.yc.health.FamilyActivity;
import com.yc.health.R;
import com.yc.health.http.HttpUserRequest;
import com.yc.health.model.MemberFamilyModel;

public class FamilyListAdapter extends BaseAdapter{

	private List<MemberFamilyModel> family = null;
	private LayoutInflater inflater = null;
	
	private Context context;
	private Handler mHandler;
	
	public FamilyListAdapter(){}
	
	public FamilyListAdapter(List<MemberFamilyModel> family,Context context, 
			Handler mHandler){
		this.family = family;
		inflater = LayoutInflater.from(context);
		this.context = context;
		this.mHandler = mHandler;
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
		
		selectorItem.selector.setVisibility(View.GONE);
		if ( FamilyActivity.editStatus ) {
			selectorItem.deleteBtn.setVisibility(View.VISIBLE);
		} else {
			selectorItem.deleteBtn.setVisibility(View.GONE);
		}
		
		MemberFamilyModel info = family.get(position);
		String s1 = info.getMembersName();
		String s2 = info.getConstitution();
		selectorItem.info.setText(Html.fromHtml(s1+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+"<font color='grade'>"+s2+"</font>"));
		selectorItem.deleteBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				HttpUserRequest request = new HttpUserRequest(context,mHandler,4);
				request.deleteMemberInit(family.get(position).getMembersFamilyId());
				request.start();
				
				family.remove(position);
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
}