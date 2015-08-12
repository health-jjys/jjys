package com.yc.health.adapter;

import java.util.ArrayList;

import com.yc.health.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyListAdapter extends BaseAdapter{

	private ArrayList<String> textList = null;
	private ArrayList<Integer> icons = null;
	private LayoutInflater inflater = null;
	
	private int which = 1;
	
	public MyListAdapter(){}
	
	public MyListAdapter(ArrayList<String> textList, ArrayList<Integer> icons, Context context,
			int which ){
		this.textList = textList;
		this.icons = icons;
		this.which = which;
		inflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		return textList.size();
	}

	@Override
	public Object getItem(int position) {
		return textList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	private static class MyItem {
		ImageView icon;
        TextView text;
        ImageView enterIcon;
    }

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		MyItem myItem;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.my_list_item, null);
			myItem = new MyItem();
			myItem.text = (TextView) convertView.findViewById(R.id.my_text_item);
			myItem.icon = (ImageView)convertView.findViewById(R.id.my_icon_item);
			myItem.enterIcon = (ImageView)convertView.findViewById(R.id.my_entericon_item);

			convertView.setTag(myItem);
		} else {
			myItem = (MyItem) convertView.getTag();
		}
		
		myItem.text.setText(textList.get(position));
		myItem.icon.setBackgroundResource(icons.get(position));
		if ( which == 1 ) {
			if ( position == 3 ) {
				myItem.enterIcon.setVisibility(View.GONE);
			}
		}

		return convertView;
	}
}
