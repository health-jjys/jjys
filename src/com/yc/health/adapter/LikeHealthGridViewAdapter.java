package com.yc.health.adapter;

import java.util.ArrayList;

import org.kymjs.kjframe.KJActivity;

import com.yc.health.KnowledgeDetailActivity;
import com.yc.health.R;
import com.yc.health.model.KnowledgeModel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LikeHealthGridViewAdapter extends BaseAdapter {
	
	private Context context = null;
	private ArrayList<KnowledgeModel> list = new ArrayList<KnowledgeModel>();
	private LayoutInflater inflater = null;
	private int x = 0;
	private int y = 0;
	
	public LikeHealthGridViewAdapter(){}
	     
	public LikeHealthGridViewAdapter(Context context) {
		this.context = context;
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

	public static class LikeHealthItem {
		ImageView img;
		TextView title;
		TextView des;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		LikeHealthItem likeHealthItem;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.likehealth_grid_item, null);
			likeHealthItem = new LikeHealthItem();
			likeHealthItem.img = (ImageView) convertView.findViewById(R.id.likehealth_img_item);
			likeHealthItem.title = (TextView) convertView.findViewById(R.id.likehealth_title_item);
			likeHealthItem.des = (TextView) convertView.findViewById(R.id.likehealth_text_item);

			convertView.setTag(likeHealthItem);
		} else {
			likeHealthItem = (LikeHealthItem) convertView.getTag();
		}
		
		likeHealthItem.img.setOnTouchListener(new OnTouchListener(){
			@Override
			public boolean onTouch(View arg0, MotionEvent e) {
				if ( e.getAction() == MotionEvent.ACTION_DOWN ) {
					x = (int)e.getX();
					y = (int)e.getY();
				}
				
				if ( e.getAction() == MotionEvent.ACTION_UP ) {
					if ( 50 > Math.abs(e.getX()-x) && 50 > Math.abs(e.getY()-y) ) {
						KJActivity activity = (KJActivity) context;
						activity.showActivity(activity, KnowledgeDetailActivity.class);
					}
				}
				return false;
			}
		});
		return convertView;
	}
	
	public ArrayList<KnowledgeModel> getList() {
		return list;
	}

	public void setList(ArrayList<KnowledgeModel> list) {
		this.list = list;
	}
}