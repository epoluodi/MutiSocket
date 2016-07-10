package com.mutisocket;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Spinner;

import com.comm.ModelControl;
import com.comm.callbackimp.CallBack;
import com.comm.net.MainServer;
import com.mutisocket.tools.Toast;
import com.ntzzDecode.MacCmd;
import com.ntzzDecode.ModelType;

public class ReaderSetAty extends Activity implements OnClickListener, CallBack {

	Context mContext;
	private String devAddress;
	private Socket devSocket;

	// 重启阅读器
	Button button1;
	// 重设阅读器
	Button button18;
	// 天线功率
	Spinner attno;
	EditText attndb;
	Button attnR;
	Button attnS;
	private List<String> anntlist = new ArrayList<String>();
	private ArrayAdapter<String> annt_adapter;
	String AnntSpinnerValue = "";
	// 盘存时间
	Spinner spinner2;
	EditText editText2;
	Button button4;
	Button button5;
	// 盘存停止模式
	Spinner spinner3;
	Button button6;
	Button button7;
	String AnntInvMod = "";
	// 心跳包
	Spinner spinner4;
	Button button8;
	Button button9;
	String IsHeartMod = "";
	// 蜂鸣器控制
	CheckBox checkBox1;
	String cb_pcs = "00";
	CheckBox checkBox2;
	String cb_gcs = "00";
	CheckBox checkBox3;
	String cb_sbs = "00";
	Button button10;
	Button button11;
	// 设备地址
	EditText editText3;
	Button button12;
	Button button13;
	// Gpio设置
	Spinner spinner6;
	Spinner spinner7;
	Button button14;
	Button button15;
	String gpioAddress = "";
	String gpioaddresstype = "";
	// Gpio输出设置
	Spinner spinner8;
	Spinner spinner9;
	Button button16;
	Button button17;
	String gpiosctype = "";

	Handler gpioszHandler = new Handler() {
		public void dispatchMessage(Message msg) {
			int type = (Integer) msg.obj;
			spinner7.setSelection(type);
		};
	};

	Handler gpioscHandler = new Handler() {
		public void dispatchMessage(Message msg) {
			int type = (Integer) msg.obj;
			spinner9.setSelection(type);
		};
	};

	Handler cb1Handler = new Handler() {
		public void dispatchMessage(Message msg) {
			int ischeck = (Integer) msg.obj;
			if (ischeck == 0) {
				checkBox1.setChecked(false);
			} else if (ischeck == 1) {
				checkBox1.setChecked(true);
			}
		};
	};

	Handler cb2Handler = new Handler() {
		public void dispatchMessage(Message msg) {
			int ischeck = (Integer) msg.obj;
			if (ischeck == 0) {
				checkBox2.setChecked(false);
			} else if (ischeck == 1) {
				checkBox2.setChecked(true);
			}
		};
	};

	Handler cb3Handler = new Handler() {
		public void dispatchMessage(Message msg) {
			int ischeck = (Integer) msg.obj;
			if (ischeck == 0) {
				checkBox3.setChecked(false);
			} else if (ischeck == 1) {
				checkBox3.setChecked(true);
			}
		};
	};

	Handler isheartHandler = new Handler() {
		public void dispatchMessage(Message msg) {
			int t = (Integer) msg.obj;
			if (t == 0) {
				spinner4.setSelection(0);
			} else if (t == 1) {
				spinner4.setSelection(1);
			}
		};
	};

	Handler invmodHandler = new Handler() {
		public void handleMessage(Message msg) {
			int t = (Integer) msg.obj;
			if (t == 0) {
				spinner3.setSelection(0);
			} else if (t == 1) {
				spinner3.setSelection(1);
			} else if (t == 2) {
				spinner3.setSelection(2);
			}
		};
	};

	Handler sbmsgHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			editText3.setText((CharSequence) msg.obj);
		};
	};

	Handler anntUiHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			attndb.setText((CharSequence) msg.obj);
			super.handleMessage(msg);
		}
	};

	Handler anntinvmsgHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			editText2.setText((CharSequence) msg.obj);
		};
	};

	Handler msgHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			Toast.show(mContext, (CharSequence) msg.obj, 2000);
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reader_set_aty);
		// �������ı���Ľ�����Զ������ļ���
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
						| WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		mContext = ReaderSetAty.this;

		Intent i = getIntent();
		devAddress = i.getStringExtra("iport");
		devSocket = MainServer.map.get(devAddress);

		if (devSocket != null) {
			Toast.show(mContext, "获取阅读器对象成功:" + devSocket, 2000);
			MacCmd.callBackSet = this;
			// 初始化界面
			initAty();
		} else {
			finish();
		}

	}

	private void initAty() {
		attno = (Spinner) findViewById(R.id.spinner1);
		attndb = (EditText) findViewById(R.id.editText1);
		attnR = (Button) findViewById(R.id.button2);
		attnR.setOnClickListener(this);
		attnS = (Button) findViewById(R.id.button3);
		attnS.setOnClickListener(this);
		anntlist.add("0");
		anntlist.add("1");
		anntlist.add("2");
		anntlist.add("3");
		annt_adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, anntlist);
		annt_adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		attno.setAdapter(annt_adapter);
		attno.setOnItemSelectedListener(new anntspinnerOnItemSelectedListener());

		button1 = (Button) findViewById(R.id.button1);
		button1.setOnClickListener(this);
		button18 = (Button) findViewById(R.id.button18);
		button18.setOnClickListener(this);

		spinner2 = (Spinner) findViewById(R.id.spinner2);
		spinner2.setAdapter(annt_adapter);
		spinner2.setOnItemSelectedListener(new anntspinnerOnItemSelectedListener());
		editText2 = (EditText) findViewById(R.id.editText2);
		button4 = (Button) findViewById(R.id.button4);
		button4.setOnClickListener(this);
		button5 = (Button) findViewById(R.id.button5);
		button5.setOnClickListener(this);

		spinner3 = (Spinner) findViewById(R.id.spinner3);
		List lt = new ArrayList();
		lt.add("手动停止盘存");
		lt.add("自动停止盘存");
		lt.add("开机读卡");
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext,
				android.R.layout.simple_spinner_item, lt);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner3.setAdapter(adapter);
		spinner3.setOnItemSelectedListener(new spinner3OnItemSelectedListener());
		button6 = (Button) findViewById(R.id.button6);
		button6.setOnClickListener(this);
		button7 = (Button) findViewById(R.id.button7);
		button7.setOnClickListener(this);

		spinner4 = (Spinner) findViewById(R.id.spinner4);
		List xlt = new ArrayList();
		xlt.add("没心跳包");
		xlt.add("有心跳包");
		ArrayAdapter<String> xadapter = new ArrayAdapter<String>(mContext,
				android.R.layout.simple_spinner_item, xlt);
		xadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner4.setAdapter(xadapter);
		spinner4.setOnItemSelectedListener(new spinner4OnTiemSelectedListener());
		button8 = (Button) findViewById(R.id.button8);
		button8.setOnClickListener(this);
		button9 = (Button) findViewById(R.id.button9);
		button9.setOnClickListener(this);

		checkBox1 = (CheckBox) findViewById(R.id.checkBox1);
		checkBox1.setOnCheckedChangeListener(new cbpcs());
		checkBox2 = (CheckBox) findViewById(R.id.checkBox2);
		checkBox2.setOnCheckedChangeListener(new cbgcs());
		checkBox3 = (CheckBox) findViewById(R.id.checkBox3);
		checkBox3.setOnCheckedChangeListener(new cbsbs());
		button10 = (Button) findViewById(R.id.button10);
		button10.setOnClickListener(this);
		button11 = (Button) findViewById(R.id.button11);
		button11.setOnClickListener(this);

		editText3 = (EditText) findViewById(R.id.editText3);
		button12 = (Button) findViewById(R.id.button12);
		button12.setOnClickListener(this);
		button13 = (Button) findViewById(R.id.button13);
		button12.setOnClickListener(this);

		spinner6 = (Spinner) findViewById(R.id.spinner6);
		List gplt = new ArrayList();
		for (int i = 0; i < 30; i++) {
			gplt.add(i);
		}
		ArrayAdapter<String> gpadapter = new ArrayAdapter<String>(mContext,
				android.R.layout.simple_spinner_item, gplt);
		gpadapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner6.setAdapter(gpadapter);
		spinner6.setOnItemSelectedListener(new spinner8OnItemSelectedListener());
		spinner7 = (Spinner) findViewById(R.id.spinner7);
		List gpsclt = new ArrayList();
		gpsclt.add("输出");
		gpsclt.add("输入");
		gpsclt.add("光电触发");
		ArrayAdapter<String> gpscadapter = new ArrayAdapter<String>(mContext,
				android.R.layout.simple_spinner_item, gpsclt);
		gpscadapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner7.setAdapter(gpscadapter);
		spinner7.setOnItemSelectedListener(new spinner7OnItemSelectedListener());
		button14 = (Button) findViewById(R.id.button14);
		button14.setOnClickListener(this);
		button15 = (Button) findViewById(R.id.button15);
		button15.setOnClickListener(this);

		spinner8 = (Spinner) findViewById(R.id.spinner8);
		List gplt1 = new ArrayList();
		for (int i = 0; i < 30; i++) {
			gplt1.add(i);
		}
		ArrayAdapter<String> gpadapter1 = new ArrayAdapter<String>(mContext,
				android.R.layout.simple_spinner_item, gplt1);
		gpadapter1
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner8.setAdapter(gpadapter1);
		spinner8.setOnItemSelectedListener(new spinner8OnItemSelectedListener());
		spinner9 = (Spinner) findViewById(R.id.spinner9);
		List gpsclt1 = new ArrayList();
		gpsclt1.add("0");
		gpsclt1.add("1");
		ArrayAdapter<String> gpscadapter1 = new ArrayAdapter<String>(mContext,
				android.R.layout.simple_spinner_item, gpsclt1);
		gpscadapter1
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner9.setAdapter(gpscadapter1);
		spinner9.setOnItemSelectedListener(new spinner9OnItemSelectedListener());
		button16 = (Button) findViewById(R.id.button16);
		button16.setOnClickListener(this);
		button17 = (Button) findViewById(R.id.button17);
		button17.setOnClickListener(this);
	}

	public class cbpcs implements OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			// TODO Auto-generated method stub
			if (checkBox1.isChecked()) {
				cb_pcs = "01";
			} else {
				cb_pcs = "00";
			}
		}
	}

	public class cbgcs implements OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			// TODO Auto-generated method stub
			if (checkBox2.isChecked()) {
				cb_gcs = "01";
			} else {
				cb_gcs = "00";
			}
		}
	}

	public class cbsbs implements OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			// TODO Auto-generated method stub
			if (checkBox3.isChecked()) {
				cb_sbs = "01";
			} else {
				cb_sbs = "00";
			}
		}
	}

	public class spinner3OnItemSelectedListener implements
			OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			String v = arg0.getSelectedItem().toString();
			if (v.equals("手动停止盘存")) {
				v = "0";
			} else if (v.equals("自动停止盘存")) {
				v = "1";
			} else if (v.equals("开机读卡")) {
				v = "2";
			}
			AnntInvMod = v;
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub

		}
	}

	public class spinner4OnTiemSelectedListener implements
			OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			String v = arg0.getSelectedItem().toString();
			if (v.equals("没心跳包")) {
				v = "0";
			} else if (v.equals("有心跳包")) {
				v = "1";
			}
			IsHeartMod = v;
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub

		}
	}

	public class spinner8OnItemSelectedListener implements
			OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			String v = arg0.getSelectedItem().toString();
			gpioAddress = Integer.toHexString(Integer.parseInt(v));
			if (gpioAddress.length() < 2) {
				gpioAddress = "0" + gpioAddress;
			}
			gpioAddress = gpioAddress + " 00";
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub

		}
	}

	public class spinner9OnItemSelectedListener implements
			OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			if (arg2 == 0) {
				gpiosctype = "00 00";
			} else if (arg2 == 1) {
				gpiosctype = "01 00";
			}

		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub

		}
	}

	public class spinner7OnItemSelectedListener implements
			OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			// TODO Auto-generated method stub
			if (position == 0) {
				gpioaddresstype = "00 00";
			} else if (position == 1) {
				gpioaddresstype = "01 00";
			} else if (position == 2) {
				gpioaddresstype = "02 00";
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub

		}

	}

	public class anntspinnerOnItemSelectedListener implements
			OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			// TODO Auto-generated method stub
			String v = parent.getSelectedItem().toString();
			if (v.equals("0")) {
				AnntSpinnerValue = "00 00";
			} else if (v.equals("1")) {
				AnntSpinnerValue = "01 00";
			} else if (v.equals("2")) {
				AnntSpinnerValue = "02 00";
			} else if (v.equals("3")) {
				AnntSpinnerValue = "03 00";
			} else {
				AnntSpinnerValue = "00 00";
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub

		}
	}

	@SuppressWarnings({ "unchecked", "static-access", "rawtypes" })
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.button1:
			new ModelControl().MethodCalled(devSocket, 0,
					ModelType.READER_RESET, null);
			break;
		case R.id.button18:
			new ModelControl().MethodCalled(devSocket, 0,
					ModelType.SET_FACTORY, null);
			break;
		case R.id.button2:
			try {
				if (AnntSpinnerValue != "") {
					Map map = new HashMap();
					map.put("anntno", AnntSpinnerValue);
					new ModelControl().MethodCalled(devSocket, 0,
							ModelType.GETANTENNA, map);
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			break;
		case R.id.button3:
			try {
				if (AnntSpinnerValue != "") {
					// 运算功率
					String glv = attndb.getText().toString();
					if (!"".equals(glv)) {
						Map map = new HashMap();
						map.put("anntno", AnntSpinnerValue);
						map.put("atnpower", glv);
						new ModelControl().MethodCalled(devSocket, 0,
								ModelType.SETANTENNA, map);
					} else {
						Toast.show(mContext, "没有填写功率,填写范围0~30", 2000);
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			break;
		case R.id.button4:
			Map map = new HashMap();
			map.put("anntno", AnntSpinnerValue);
			new ModelControl().MethodCalled(devSocket, 0,
					ModelType.GET_ANTENNA_INVTIME, map);
			break;
		case R.id.button5:
			map = new HashMap();
			map.put("anntno", AnntSpinnerValue);
			map.put("anntinvtime", editText2.getText().toString());
			new ModelControl().MethodCalled(devSocket, 0,
					ModelType.SET_ANTENNA_INVTIME, map);
			break;
		case R.id.button6:
			new ModelControl().MethodCalled(devSocket, 0,
					ModelType.GET_ANTENNA_INVMOD, null);
			break;
		case R.id.button7:
			map = new HashMap();
			map.put("invmodtype", AnntInvMod);
			new ModelControl().MethodCalled(devSocket, 0,
					ModelType.SET_ANTENNA_INVMOD, map);
			break;
		case R.id.button8:
			new ModelControl().MethodCalled(devSocket, 0,
					ModelType.ISHEARTPACK, null);
			break;
		case R.id.button9:
			map = new HashMap();
			map.put("isheartpacket", IsHeartMod);
			new ModelControl().MethodCalled(devSocket, 0,
					ModelType.GETSETHEARTPACKET, map);
			break;
		case R.id.button10:
			new ModelControl().MethodCalled(devSocket, 0,
					ModelType.GET_CMD_BIBIBIGO, null);
			break;
		case R.id.button11:
			map = new HashMap();
			map.put("setbibimodel", cb_pcs + " " + cb_gcs + " " + cb_sbs);
			new ModelControl().MethodCalled(devSocket, 0,
					ModelType.SET_CMD_BIBIBIGO, map);
			break;
		case R.id.button12:
			new ModelControl().MethodCalled(devSocket, 0,
					ModelType.GET_PRODUCT_ADDRESS, null);
			break;
		case R.id.button13:

			break;
		case R.id.button14:
			map = new HashMap();
			map.put("gpioaddress", gpioAddress);
			new ModelControl().MethodCalled(devSocket, 0,
					ModelType.GET_CMD_GPIO_MOD, map);
			break;
		case R.id.button15:
			map = new HashMap();
			map.put("gpioaddress", gpioAddress);
			map.put("gpioaddresstype", gpioaddresstype);
			new ModelControl().MethodCalled(devSocket, 0,
					ModelType.SET_CMD_GPIO_MOD, map);
			break;
		case R.id.button16:
			map = new HashMap();
			map.put("gpioaddress", gpioAddress);
			new ModelControl().MethodCalled(devSocket, 0,
					ModelType.GET_CMD_GPIO_CONFIG, map);
			break;
		case R.id.button17:
			map = new HashMap();
			map.put("gpioaddress", gpioAddress);
			map.put("gpiosctype", gpiosctype);
			new ModelControl().MethodCalled(devSocket, 0,
					ModelType.SET_CMD_GPIO_CONFIG, map);
			break;
		}
	}

	@Override
	public void execute(Object... arg0) {
		// TODO Auto-generated method stub
		int cmd = Integer.parseInt((String) arg0[1], 10);
		String returnstr = (String) arg0[0];
		String address = (String) arg0[2];
		switch (cmd) {
		case MacCmd.CMD_CLOSE_POWER_SET:
			Log.v("天线的返回", (String) arg0[0]);
			Message msg = new Message();
			msg.obj = arg0[0];
			anntUiHandler.sendMessage(msg);
			break;
		case MacCmd.CMD_READER_RESET:
			String returnstr1 = (((String) arg0[0]).split(" "))[0];
			int i1 = Integer.parseInt(returnstr1);
			if (i1 > 0) {
				Message msg11 = new Message();
				msg11.obj = "重启失败,代码为： " + i1;
				msgHandler.sendMessage(msg11);
			} else {
				Message msg11 = new Message();
				msg11.obj = "重启成功";
				msgHandler.sendMessage(msg11);
			}
			break;
		case MacCmd.CMD_SET_FACTORY:
			String returnstr11 = (((String) arg0[0]).split(" "))[0];
			int i11 = Integer.parseInt(returnstr11);
			if (i11 > 0) {
				Message msg11 = new Message();
				msg11.obj = "重设失败,代码为： " + i11;
				msgHandler.sendMessage(msg11);
			} else {
				Message msg11 = new Message();
				msg11.obj = "重设成功";
				msgHandler.sendMessage(msg11);
			}
			break;
		case MacCmd.CMD_INV_TIME:
			Message msg21 = new Message();
			String[] t1 = ((String) arg0[0]).split(" ");
			String hexstr = t1[3] + t1[2];
			int ten = Integer.parseInt(hexstr, 16);
			msg21.obj = String.valueOf(ten);
			anntinvmsgHandler.sendMessage(msg21);
			break;
		case MacCmd.CMD_PRODUCT_ADDRESS:
			Message msg2 = new Message();
			String[] t = ((String) arg0[0]).split(" ");
			String nt = t[1] + t[0];
			int it = Integer.parseInt(nt);
			String nit = Integer.toHexString(it);
			msg2.obj = nit;
			sbmsgHandler.sendMessage(msg2);
			break;
		case MacCmd.CMD_ANTENNA_INVMOD:
			Message msg211 = new Message();
			int i22 = Integer.parseInt((((String) arg0[0]).split(" "))[0]);
			msg211.obj = i22;
			invmodHandler.sendMessage(msg211);
			break;
		case MacCmd.CMD_ISHEARTPACKET:
			Message msg1 = new Message();
			int i112 = Integer.parseInt((((String) arg0[0]).split(" "))[0]);
			msg1.obj = i112;
			isheartHandler.sendMessage(msg1);
			break;
		case MacCmd.CMD_BIBIBIGO:
			String[] str = ((String) arg0[0]).split(" ");
			for (int i = 0; i < str.length; i++) {
				int ischeck = Integer.parseInt(str[i]);
				if (i == 0) {
					Message m1 = new Message();
					m1.obj = ischeck;
					cb1Handler.sendMessage(m1);
				} else if (i == 1) {
					Message m2 = new Message();
					m2.obj = ischeck;
					cb2Handler.sendMessage(m2);
				} else if (i == 2) {
					Message m3 = new Message();
					m3.obj = ischeck;
					cb3Handler.sendMessage(m3);
				}
			}
			break;
		case MacCmd.CMD_GPIO_MOD:
			String rstr = returnstr.split(" ")[0];
			int gpsztype = Integer.parseInt(returnstr.split(" ")[2]);
			String dbrstr = gpioAddress.split(" ")[0];
			if (rstr.equals(dbrstr)) {
				Message mgpsz = new Message();
				mgpsz.obj = gpsztype;
				gpioszHandler.sendMessage(mgpsz);
			} else {
				Toast.show(mContext, "地址写入不正确", 2000);
			}
			break;
		case MacCmd.CMD_GPIO_CONFIG:
			rstr = returnstr.split(" ")[0];
			gpsztype = Integer.parseInt(returnstr.split(" ")[2]);
			dbrstr = gpioAddress.split(" ")[0];
			if (rstr.equals(dbrstr)) {
				Message mgpsz = new Message();
				mgpsz.obj = gpsztype;
				gpioscHandler.sendMessage(mgpsz);
			} else {
				Toast.show(mContext, "地址写入不正确", 2000);
			}
			break;
		}
	}

	@Override
	public void execute2(Object... arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void execute3(Object... arg0) {
		// TODO Auto-generated method stub

	}
}
