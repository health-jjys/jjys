package com.yc.health.adapter;

import java.util.List;

import org.kymjs.kjframe.KJActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yc.health.DetailActivity;
import com.yc.health.R;
import com.yc.health.model.PrivateOrderModel;

public class PrivateOrderDetailGridViewAdapter extends BaseAdapter{

	private List<PrivateOrderModel> list = null;
	private LayoutInflater inflater = null;
	private Context context;
	
	private float x = 0;
	private float y = 0;
	
	public PrivateOrderDetailGridViewAdapter(){}
	
	public PrivateOrderDetailGridViewAdapter(List<PrivateOrderModel> list, Context context){
		this.list = list;
		inflater = LayoutInflater.from(context);
		this.context = context;
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
		WebView img;
        TextView name;
        TextView describe;
    }

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		PrivateOrderItem privateOrderItem;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.likehealth_grid_item, null);
			privateOrderItem = new PrivateOrderItem();
			privateOrderItem.img = (WebView) convertView.findViewById(R.id.likehealth_img_item);
			privateOrderItem.name = (TextView) convertView.findViewById(R.id.likehealth_title_item);
			privateOrderItem.describe = (TextView) convertView.findViewById(R.id.likehealth_text_item);
			convertView.setTag(privateOrderItem);
		} else {
			privateOrderItem = (PrivateOrderItem) convertView.getTag();
		}
		
		privateOrderItem.name.setText(list.get(position).getName());
		privateOrderItem.describe.setText(list.get(position).getDescription());
		
		String path = context.getResources().getString(R.string.localhost)+list.get(position).getImagePath();
		privateOrderItem.img.loadUrl(path);
		privateOrderItem.img.setBackgroundColor(context.getResources().getColor(R.color.transparent));
		privateOrderItem.img.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		privateOrderItem.img.getSettings().setLoadWithOverviewMode(true);
		
		privateOrderItem.img.setOnTouchListener(new OnTouchListener(){
			@Override
			public boolean onTouch(View arg0, MotionEvent e) {
				if ( e.getAction() == MotionEvent.ACTION_DOWN ) {
					x = e.getX();
					y = e.getY();
				} else if ( (e.getAction() == MotionEvent.ACTION_UP) ||  
						(e.getAction() == MotionEvent.ACTION_CANCEL) ) {
					if ( 30 > Math.abs(e.getX()-x) && 30 > Math.abs(e.getY()-y) ) {
						Bundle bundle = new Bundle();
						bundle.putInt("id", list.get(position).getPrivateOrderId());
						bundle.putString("name", list.get(position).getName());
						bundle.putString("description", list.get(position).getName());
						bundle.putString("phone", list.get(position).getPhone());
						bundle.putString("address", list.get(position).getName());
						bundle.putString("path", list.get(position).getImagePath());
						bundle.putString("type", list.get(position).getType());
						KJActivity activity = (KJActivity)context;
						activity.showActivity(activity, DetailActivity.class,bundle);
					}
				}
				return false;
			}
		});
		
		return convertView;
	}
	
	public List<PrivateOrderModel> getList() {
		return list;
	}

	public void setList(List<PrivateOrderModel> list) {
		this.list = list;
	}
}