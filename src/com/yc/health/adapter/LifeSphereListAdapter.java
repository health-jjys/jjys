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
import com.yc.health.model.LifeSphereModel;

public class LifeSphereListAdapter extends BaseAdapter{

	private List<LifeSphereModel> list = null;
	private LayoutInflater inflater = null;
	
	public LifeSphereListAdapter(){}
	
	public LifeSphereListAdapter(List<LifeSphereModel> list, Context context){
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
	
	private static class LifeSphereItem {
		ImageView img;
        TextView name;
        TextView style;
        TextView describe;
    }

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		LifeSphereItem lifeSphereItem;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.lifesphere_list_item, null);
			lifeSphereItem = new LifeSphereItem();
			lifeSphereItem.img = (ImageView) convertView.findViewById(R.id.lifesphere_img_item);
			lifeSphereItem.name = (TextView) convertView.findViewById(R.id.lifesphere_name_item);
			lifeSphereItem.style = (TextView) convertView.findViewById(R.id.lifesphere_style_item);
			lifeSphereItem.describe = (TextView) convertView.findViewById(R.id.lifesphere_describe_item);
			convertView.setTag(lifeSphereItem);
		} else {
			lifeSphereItem = (LifeSphereItem) convertView.getTag();
		}
		
		return convertView;
	}
}