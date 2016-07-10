package com.mutisocket.adt;

import java.net.Socket;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mutisocket.OnlineReaderActivity;
import com.mutisocket.R;
import com.mutisocket.ReaderSetAty;
import com.mutisocket.tools.Toast;

public class ItemAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private ArrayList<String> datas;
	private Drawable[] resdb;
	private Context mContext;
	ArrayList<Drawable> dataDrwable;
	private Socket devSocket;
	OnlineReaderActivity maty;

	public ItemAdapter(Context context) {
		inflater = LayoutInflater.from(context);
		mContext = context;
		maty = (OnlineReaderActivity) context;
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
				convertView = inflater.inflate(R.layout.listview_item, null);
				holder.iv_item = (ImageView) convertView
						.findViewById(R.id.iv_item);
				holder.textView = (TextView) convertView
						.findViewById(R.id.tv_item);
				holder.coating = (TextView) convertView
						.findViewById(R.id.tv_coating);
				holder.functions = (TextView) convertView
						.findViewById(R.id.tv_functions);
				holder.tv_inv = (TextView) convertView
						.findViewById(R.id.tv_inv);
				holder.tv_inv.setTag(datas.get(position));
				holder.tv_inv.setOnClickListener(invlistener);
				holder.tv_stopinv = (TextView) convertView
						.findViewById(R.id.tv_stopinv);
				holder.tv_stopinv.setTag(datas.get(position));
				holder.tv_stopinv.setOnClickListener(stopinvlistener);

				holder.textView.setTag(position);
				holder.functions.setTag(datas.get(position));
				holder.functions.setOnClickListener(listener);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.iv_item.setBackgroundResource(R.drawable.ic_launcher);

			holder.textView.setText(datas.get(position));
			holder.coating.setVisibility(View.GONE);
			holder.functions.setClickable(true);
		}
		return convertView;
	}

	private View.OnClickListener listener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			try {
				String data = (String) v.getTag();
				Intent intent = new Intent(mContext, ReaderSetAty.class);
				intent.putExtra("iport", data);
				mContext.startActivity(intent);
			} catch (Exception e) {
				// TODO: handle exception
				Toast.show(mContext, e.getMessage(), 2000);
			}
		}
	};

	private View.OnClickListener invlistener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			try {
				String data = (String) v.getTag();
				maty.inv(data);

			} catch (Exception e) {
				// TODO: handle exception
				Toast.show(mContext, e.getMessage(), 2000);
			}
		}
	};

	private View.OnClickListener stopinvlistener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			try {
				String data = (String) v.getTag();
				maty.stopinv(data);

			} catch (Exception e) {
				// TODO: handle exception
				Toast.show(mContext, e.getMessage(), 2000);
			}
		}
	};

	public final class ViewHolder {
		public ImageView iv_item;
		public TextView textView;
		public TextView coating;
		public TextView functions;
		public TextView tv_inv;
		public TextView tv_stopinv;
	}

}
