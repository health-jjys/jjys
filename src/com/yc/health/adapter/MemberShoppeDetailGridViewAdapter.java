package com.yc.health.adapter;

import java.util.List;

import org.kymjs.kjframe.KJActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.webkit.WebView;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yc.health.DetailActivity;
import com.yc.health.R;
import com.yc.health.model.MemberShoppeModel;

public class MemberShoppeDetailGridViewAdapter extends BaseAdapter{

	private List<MemberShoppeModel> list = null;
	private LayoutInflater inflater = null;
	private Context context;
	
	private float x = 0;
	private float y = 0;
	
	public MemberShoppeDetailGridViewAdapter(){}
	
	public MemberShoppeDetailGridViewAdapter(List<MemberShoppeModel> list, Context context){
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
	
	private static class MemberShoppeItem {
		WebView img;
        TextView name;
        TextView describe;
    }

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		MemberShoppeItem memberShoppeItem;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.likehealth_grid_item, null);
			memberShoppeItem = new MemberShoppeItem();
			memberShoppeItem.img = (WebView) convertView.findViewById(R.id.likehealth_img_item);
			memberShoppeItem.name = (TextView) convertView.findViewById(R.id.likehealth_title_item);
			memberShoppeItem.describe = (TextView) convertView.findViewById(R.id.likehealth_text_item);
			convertView.setTag(memberShoppeItem);
		} else {
			memberShoppeItem = (MemberShoppeItem) convertView.getTag();
		}
		
		memberShoppeItem.name.setText(list.get(position).getName());
		memberShoppeItem.describe.setText(list.get(position).getDescription());
		
		String path = context.getResources().getString(R.string.localhost)+list.get(position).getImagePath();
		memberShoppeItem.img.loadUrl(path);
		memberShoppeItem.img.setBackgroundColor(context.getResources().getColor(R.color.transparent));
		memberShoppeItem.img.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		memberShoppeItem.img.getSettings().setLoadWithOverviewMode(true);
		
		memberShoppeItem.img.setOnTouchListener(new OnTouchListener(){
			@Override
			public boolean onTouch(View arg0, MotionEvent e) {
				if ( e.getAction() == MotionEvent.ACTION_DOWN ) {
					x = e.getX();
					y = e.getY();
				} else if ( (e.getAction() == MotionEvent.ACTION_UP) ||  
						(e.getAction() == MotionEvent.ACTION_CANCEL) ) {
					if ( 30 > Math.abs(e.getX()-x) && 30 > Math.abs(e.getY()-y) ) {
						Bundle bundle = new Bundle();
						bundle.putInt("id", list.get(position).getMemberShoppeId());
						bundle.putString("name", list.get(position).getName());
						bundle.putString("description", list.get(position).getName());
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
	
	public List<MemberShoppeModel> getList() {
		return list;
	}

	public void setList(List<MemberShoppeModel> list) {
		this.list = list;
	}
}