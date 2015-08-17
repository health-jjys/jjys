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
import com.yc.health.model.PrivateOrderModel;

public class PrivateOrderListAdapter extends BaseAdapter{

	private List<PrivateOrderModel> list = null;
	private LayoutInflater inflater = null;
	private Context context;
	
	public PrivateOrderListAdapter(){}
	
	public PrivateOrderListAdapter(List<PrivateOrderModel> list, Context context){
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
			convertView = inflater.inflate(R.layout.private_order_list_item, null);
			privateOrderItem = new PrivateOrderItem();
			privateOrderItem.img = (WebView) convertView.findViewById(R.id.private_order_img_item);
			privateOrderItem.name = (TextView) convertView.findViewById(R.id.private_order_name_item);
			privateOrderItem.describe = (TextView) convertView.findViewById(R.id.private_order_describe_item);
			convertView.setTag(privateOrderItem);
		} else {
			privateOrderItem = (PrivateOrderItem) convertView.getTag();
		}
		
		privateOrderItem.name.setText(list.get(position).getName());
		privateOrderItem.describe.setText(list.get(position).getDescription());
		
		String path = context.getResources().getString(R.string.localhost)+list.get(position).getImagePath();
		privateOrderItem.img.loadUrl(path);
		@SuppressWarnings("deprecation")
		int windowsWidth = ((KJActivity) context).getWindowManager().getDefaultDisplay().getWidth();
		privateOrderItem.img.getLayoutParams().width = (int) (windowsWidth*0.25);
		privateOrderItem.img.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		privateOrderItem.img.getSettings().setLoadWithOverviewMode(true);
		
		return convertView;
	}

	public List<PrivateOrderModel> getList() {
		return list;
	}

	public void setList(List<PrivateOrderModel> list) {
		this.list = list;
	}
	
}