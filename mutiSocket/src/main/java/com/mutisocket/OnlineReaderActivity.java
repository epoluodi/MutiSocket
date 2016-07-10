package com.mutisocket;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.comm.ModelControl;
import com.comm.net.MainServer;
import com.mutisocket.adt.ItemAdapter;
import com.mutisocket.tools.Toast;
import com.ntzzDecode.ModelType;

public class OnlineReaderActivity extends Activity implements OnClickListener {

	Context mContext;
	ListView devsList;
	private ItemAdapter adapter;
	private ArrayList<String> dataSourceList = new ArrayList<String>();
	
	private Socket devSocket;

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				adapter.notifyDataSetChanged();
				break;
			}
			super.handleMessage(msg);
		}
	};

	private void sendMsg(int flag) {
		Message msg = new Message();
		msg.what = flag;
		handler.sendMessage(msg);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_online_reader);		
		mContext = OnlineReaderActivity.this;

		devsList = (ListView) findViewById(R.id.devsList);	
		
		adapter = new ItemAdapter(mContext);
		adapter.setData(dataSourceList, null);
		devsList.setAdapter(adapter);
		
		if(MainServer.map!=null){
			for(Map.Entry<String, Socket> adds : MainServer.map.entrySet()){
				//System.out.println("Key = " + adds.getKey() + ", Value = " + adds.getValue());
				if (!dataSourceList.contains(adds.getKey())) {
					dataSourceList.add(adds.getKey());
					sendMsg(0);
				}
			}
		}
	}
	
	@SuppressWarnings("static-access")
	public void inv(String data) {
		devSocket = MainServer.map.get(data);
		new ModelControl().MethodCalled(devSocket, 0, ModelType.STARTINV, null);
		Toast.show(mContext, "盘存开始", 2000);
	}

	@SuppressWarnings("static-access")
	public void stopinv(String data) {
		devSocket = MainServer.map.get(data);
		new ModelControl().MethodCalled(devSocket, 0, ModelType.STOPINV, null);
		Toast.show(mContext, "停止盘存", 2000);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}
}
