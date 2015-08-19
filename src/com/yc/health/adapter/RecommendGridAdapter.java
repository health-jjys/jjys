package com.yc.health.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yc.health.R;
import com.yc.health.model.PrivateOrderModel;

public class RecommendGridAdapter extends BaseAdapter{

	private List<PrivateOrderModel> list = null;
	private LayoutInflater inflater = null;
	
	public RecommendGridAdapter(){}
	
	public RecommendGridAdapter(List<PrivateOrderModel> list, Context context){
		this.list = list;
		inflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		if ( list != null ) {
			return list.size();
		} else {
			return 0;
		}
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	private static class RecommendItem {
		ImageView img;
        TextView name;
    }

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		RecommendItem recommendItem;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.recommend_grid_item, null);
			recommendItem = new RecommendItem();
			recommendItem.img = (ImageView) convertView.findViewById(R.id.recommend_grid_img_item);
			recommendItem.name = (TextView) convertView.findViewById(R.id.recommend_grid_name_item);
			convertView.setTag(recommendItem);
		} else {
			recommendItem = (RecommendItem) convertView.getTag();
		}
		
		return convertView;
	}
}