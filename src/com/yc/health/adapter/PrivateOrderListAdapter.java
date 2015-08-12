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

public class PrivateOrderListAdapter extends BaseAdapter{

	private List<PrivateOrderModel> list = null;
	private LayoutInflater inflater = null;
	
	public PrivateOrderListAdapter(){}
	
	public PrivateOrderListAdapter(List<PrivateOrderModel> list, Context context){
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
	
	private static class PrivateOrderItem {
		
		ImageView img;
        TextView name;
        TextView describe;
    }

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		PrivateOrderItem privateOrderItem;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.private_order_list_item, null);
			privateOrderItem = new PrivateOrderItem();
			privateOrderItem.img = (ImageView) convertView.findViewById(R.id.recommend_img_item);
			privateOrderItem.name = (TextView) convertView.findViewById(R.id.recommend_name_item);
			privateOrderItem.describe = (TextView) convertView.findViewById(R.id.recommend_describe_item);
			convertView.setTag(privateOrderItem);
		} else {
			privateOrderItem = (PrivateOrderItem) convertView.getTag();
		}
		
		return convertView;
	}
}