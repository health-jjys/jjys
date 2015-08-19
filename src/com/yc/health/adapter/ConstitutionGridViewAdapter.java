package com.yc.health.adapter;

import java.util.ArrayList;
import java.util.List;

import com.yc.health.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;

public class ConstitutionGridViewAdapter extends BaseAdapter{

	private List<Integer> constitutions = new ArrayList<Integer>();
	private LayoutInflater inflater = null;
	
	public ConstitutionGridViewAdapter(){}
	
	public ConstitutionGridViewAdapter(Context context){
		inflater = LayoutInflater.from(context);
		
		init();
	}
	
	private void init() {
		constitutions.add(R.drawable.btn_phz_normal);
		constitutions.add(R.drawable.btn_phz_normal);
		constitutions.add(R.drawable.btn_phz_normal);
		constitutions.add(R.drawable.btn_phz_normal);
		constitutions.add(R.drawable.btn_phz_normal);
		constitutions.add(R.drawable.btn_phz_normal);
		constitutions.add(R.drawable.btn_phz_normal);
		constitutions.add(R.drawable.btn_phz_normal);
		constitutions.add(R.drawable.btn_phz_normal);
	}
	
	@Override
	public int getCount() {
		return constitutions.size();
	}

	@Override
	public Object getItem(int position) {
		return constitutions.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	private static class ConstitutionItem {
        ImageButton item;
    }

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ConstitutionItem constitutionItem;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.constitution_grid_item, null);
			constitutionItem = new ConstitutionItem();
			constitutionItem.item = (ImageButton) convertView.findViewById(R.id.constitution_item);

			convertView.setTag(constitutionItem);
		} else {
			constitutionItem = (ConstitutionItem) convertView.getTag();
		}
		
		constitutionItem.item.setBackgroundResource(constitutions.get(position));

		return convertView;
	}
}
