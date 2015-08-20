package com.yc.health.adapter;

import java.util.ArrayList;
import java.util.List;

import org.kymjs.kjframe.KJActivity;

import com.yc.health.KnowledgeDetailActivity;
import com.yc.health.R;
import com.yc.health.VideoDetailActivity;
import com.yc.health.model.KnowledgeModel;

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

public class LikeHealthGridViewAdapter extends BaseAdapter {
	
	private Context context = null;
	private List<KnowledgeModel> list = new ArrayList<KnowledgeModel>();
	private LayoutInflater inflater = null;
	private float x = 0;
	private float y = 0;
	private String type = null;
	
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
		WebView img;
		TextView title;
		TextView des;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		LikeHealthItem likeHealthItem;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.likehealth_grid_item, null);
			likeHealthItem = new LikeHealthItem();
			likeHealthItem.img = (WebView) convertView.findViewById(R.id.likehealth_img_item);
			likeHealthItem.title = (TextView) convertView.findViewById(R.id.likehealth_title_item);
			likeHealthItem.des = (TextView) convertView.findViewById(R.id.likehealth_text_item);

			convertView.setTag(likeHealthItem);
		} else {
			likeHealthItem = (LikeHealthItem) convertView.getTag();
		}
		
		likeHealthItem.title.setText(list.get(position).getTitle());
		likeHealthItem.des.setText(list.get(position).getContent());
		
		String path = context.getResources().getString(R.string.localhost)+list.get(position).getImagePath1();
		likeHealthItem.img.loadUrl(path);
		likeHealthItem.img.setBackgroundColor(context.getResources().getColor(R.color.transparent));
		likeHealthItem.img.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		likeHealthItem.img.getSettings().setLoadWithOverviewMode(true);
		
		likeHealthItem.img.setOnTouchListener(new OnTouchListener(){
			@Override
			public boolean onTouch(View arg0, MotionEvent e) {
				if ( e.getAction() == MotionEvent.ACTION_DOWN ) {
					x = e.getX();
					y = e.getY();
				} else if ( (e.getAction() == MotionEvent.ACTION_UP) ||  
						(e.getAction() == MotionEvent.ACTION_CANCEL) ) {
					if ( 30 > Math.abs(e.getX()-x) && 30 > Math.abs(e.getY()-y) ) {
						if ( "richText".equals(type) ) {
							Bundle bundle = new Bundle();
							bundle.putInt("id", list.get(position).getKnowledgeID());
							bundle.putString("path1", list.get(position).getImagePath1());
							bundle.putString("path2", list.get(position).getImagePath2());
							bundle.putString("path3", list.get(position).getImagePath3());
							bundle.putString("path4", list.get(position).getImagePath4());
							KJActivity activity = (KJActivity)context;
							activity.showActivity(activity, KnowledgeDetailActivity.class,bundle);
						} else {
							Bundle bundle = new Bundle();
							bundle.putInt("id", list.get(position).getKnowledgeID());
							bundle.putString("content", list.get(position).getContent());
							bundle.putString("path", list.get(position).getVideoUrlPath());
							KJActivity activity = (KJActivity)context;
							activity.showActivity(activity, VideoDetailActivity.class,bundle);
						}
					}
				}
				return false;
			}
		});
		return convertView;
	}
	
	public List<KnowledgeModel> getList() {
		return list;
	}

	public void setList(List<KnowledgeModel> list) {
		this.list = list;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}