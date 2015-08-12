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
import com.yc.health.model.RecommendModel;

public class RecommendListAdapter extends BaseAdapter{

	private List<RecommendModel> list = null;
	private LayoutInflater inflater = null;
	
	public RecommendListAdapter(){}
	
	public RecommendListAdapter(List<RecommendModel> list, Context context){
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
        TextView describe;
        TextView constitution1;
        TextView constitution2;
        TextView constitution3;
        TextView constitution4;
    }

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		RecommendItem recommendItem;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.recommend_list_item, null);
			recommendItem = new RecommendItem();
			recommendItem.img = (ImageView) convertView.findViewById(R.id.recommend_img_item);
			recommendItem.name = (TextView) convertView.findViewById(R.id.recommend_name_item);
			recommendItem.describe = (TextView) convertView.findViewById(R.id.recommend_describe_item);
			recommendItem.constitution1 = (TextView) convertView.findViewById(R.id.recommend_constitution1_item);
			recommendItem.constitution2 = (TextView) convertView.findViewById(R.id.recommend_constitution2_item);
			recommendItem.constitution3 = (TextView) convertView.findViewById(R.id.recommend_constitution3_item);
			recommendItem.constitution4 = (TextView) convertView.findViewById(R.id.recommend_constitution4_item);
			convertView.setTag(recommendItem);
		} else {
			recommendItem = (RecommendItem) convertView.getTag();
		}
		
		recommendItem.constitution2.setVisibility(View.GONE);
		recommendItem.constitution3.setVisibility(View.GONE);
		recommendItem.constitution4.setVisibility(View.GONE);
		
		return convertView;
	}
}
