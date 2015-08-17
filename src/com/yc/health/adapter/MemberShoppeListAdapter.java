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

public class MemberShoppeListAdapter extends BaseAdapter{

	private List<MemberShoppeModel> list = null;
	private LayoutInflater inflater = null;
	private Context context;
	
	public MemberShoppeListAdapter(){}
	
	public MemberShoppeListAdapter(List<MemberShoppeModel> list, Context context){
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
			convertView = inflater.inflate(R.layout.private_order_list_item, null);
			memberShoppeItem = new MemberShoppeItem();
			memberShoppeItem.img = (WebView) convertView.findViewById(R.id.private_order_img_item);
			memberShoppeItem.name = (TextView) convertView.findViewById(R.id.private_order_name_item);
			memberShoppeItem.describe = (TextView) convertView.findViewById(R.id.private_order_describe_item);
			convertView.setTag(memberShoppeItem);
		} else {
			memberShoppeItem = (MemberShoppeItem) convertView.getTag();
		}
		
		memberShoppeItem.name.setText(list.get(position).getName());
		memberShoppeItem.describe.setText(list.get(position).getDescription());
		
		String path = context.getResources().getString(R.string.localhost)+list.get(position).getImagePath();
		memberShoppeItem.img.loadUrl(path);
		@SuppressWarnings("deprecation")
		int windowsWidth = ((KJActivity) context).getWindowManager().getDefaultDisplay().getWidth();
		memberShoppeItem.img.getLayoutParams().width = (int) (windowsWidth*0.25);
		memberShoppeItem.img.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		memberShoppeItem.img.getSettings().setLoadWithOverviewMode(true);
		
		
		return convertView;
	}
	
	public List<MemberShoppeModel> getList() {
		return list;
	}

	public void setList(List<MemberShoppeModel> list) {
		this.list = list;
	}
}