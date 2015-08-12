package com.yc.health.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.yc.health.R;

public class LifeSphereGridViewAdapter extends BaseAdapter {
	
	private ArrayList<Integer> list = new ArrayList<Integer>();
	private LayoutInflater inflater = null;
	
	public LifeSphereGridViewAdapter(){}
	     
	public LifeSphereGridViewAdapter(Context context) {
		inflater = LayoutInflater.from(context);  
		
		for ( int i = 0 ; i < 8; i++ ) {
			list.add(R.drawable.btn_wd_nor);
		}
	}
	
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public static class LifeSphereItem {
		ImageView img;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		LifeSphereItem lifeSphereItem;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.lifesphere_grid_item, null);
			lifeSphereItem = new LifeSphereItem();
			lifeSphereItem.img = (ImageView) convertView.findViewById(R.id.lifesphere_img_grid_item);

			convertView.setTag(lifeSphereItem);
		} else {
			lifeSphereItem = (LifeSphereItem) convertView.getTag();
		}
		return convertView;
	}
}
