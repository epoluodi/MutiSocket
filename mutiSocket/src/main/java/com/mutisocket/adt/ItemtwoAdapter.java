package com.mutisocket.adt;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mutisocket.R;

public class ItemtwoAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private ArrayList<String> datas;
	private Drawable[] resdb;
	private Context mContext;
	ArrayList<Drawable> dataDrwable;

	public ItemtwoAdapter(Context context) {
		inflater = LayoutInflater.from(context);
		mContext = context;
	}

	public void setData(ArrayList<String> datas, Drawable[] db) {
		this.datas = datas;
		this.resdb = db;
		dataDrwable = new ArrayList<Drawable>();
	}

	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public Object getItem(int position) {
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (position < datas.size()) {
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = inflater
						.inflate(R.layout.listview_item_two, null);
				holder.iv_item = (ImageView) convertView
						.findViewById(R.id.iv_item);
				holder.iv_item.setVisibility(View.GONE);
				holder.textView = (TextView) convertView
						.findViewById(R.id.tv_item);
				holder.coating = (TextView) convertView
						.findViewById(R.id.tv_coating);
				holder.functions = (TextView) convertView
						.findViewById(R.id.tv_functions);
				holder.functions.setVisibility(View.GONE);
				holder.textView.setTag(position);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.textView.setText(datas.get(position));
		}
		return convertView;
	}

	public final class ViewHolder {
		public ImageView iv_item;
		public TextView textView;
		public TextView coating;
		public TextView functions;
	}

}
