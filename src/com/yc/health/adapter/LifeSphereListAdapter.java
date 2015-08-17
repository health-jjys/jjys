package com.yc.health.adapter;

import java.util.List;

import org.kymjs.kjframe.KJActivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yc.health.R;
import com.yc.health.model.MemberShoppeModel;

public class LifeSphereListAdapter extends BaseAdapter{

	private List<MemberShoppeModel> list = null;
	private LayoutInflater inflater = null;
	private Context context;
	
	public LifeSphereListAdapter(){}
	
	public LifeSphereListAdapter(List<MemberShoppeModel> list, Context context){
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
	
	private static class LifeSphereItem {
		WebView img;
        TextView name;
        TextView type;
        TextView describe;
    }

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		LifeSphereItem lifeSphereItem;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.lifesphere_list_item, null);
			lifeSphereItem = new LifeSphereItem();
			lifeSphereItem.img = (WebView) convertView.findViewById(R.id.lifesphere_img_item);
			lifeSphereItem.name = (TextView) convertView.findViewById(R.id.lifesphere_name_item);
			lifeSphereItem.type = (TextView) convertView.findViewById(R.id.lifesphere_style_item);
			lifeSphereItem.describe = (TextView) convertView.findViewById(R.id.lifesphere_describe_item);
			convertView.setTag(lifeSphereItem);
		} else {
			lifeSphereItem = (LifeSphereItem) convertView.getTag();
		}
		
		lifeSphereItem.name.setText(list.get(position).getName());
		lifeSphereItem.describe.setText(list.get(position).getDescription());
		lifeSphereItem.type.setText(list.get(position).getType());
		
		String path = context.getResources().getString(R.string.localhost)+list.get(position).getImagePath();
		lifeSphereItem.img.loadUrl(path);
		@SuppressWarnings("deprecation")
		int windowsWidth = ((KJActivity) context).getWindowManager().getDefaultDisplay().getWidth();
		lifeSphereItem.img.getLayoutParams().width = (int) (windowsWidth*0.25);
		lifeSphereItem.img.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		lifeSphereItem.img.getSettings().setLoadWithOverviewMode(true);
		
		return convertView;
	}
	
	public List<MemberShoppeModel> getList() {
		return list;
	}

	public void setList(List<MemberShoppeModel> list) {
		this.list = list;
	}
}