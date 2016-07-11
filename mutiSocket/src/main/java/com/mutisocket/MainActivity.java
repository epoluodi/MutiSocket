package com.mutisocket;

import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.comm.ModelControl;
import com.comm.callbackimp.CallBack;
import com.comm.net.MainServer;
import com.mutisocket.adt.ItemtwoAdapter;
import com.mutisocket.tools.HelpUtil;
import com.mutisocket.tools.Toast;
import com.ntzzDecode.MacCmd;
import com.ntzzDecode.ModelType;

public class MainActivity extends Activity implements OnClickListener, CallBack {

    SimpleDateFormat time = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
    ImageView imgbtn;
    String longinname;
    String addressPort;
    ImageView pichead;
    TextView pepname;
    TextView peptime;
    Button addDev;
    Context mContext;

    ListView tagsList;
    private ItemtwoAdapter tagsAdapter;
    private ArrayList<String> datasList = new ArrayList<String>();

    HashMap<String, String> map;
    String tag = "";
    int i = 0;
    static ArrayList<HashMap<String, String>> tagList;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (i == 2) {
                        i = 0;
                    } else {
                        i++;
                    }

                    int index = -1;

                    HashMap<String, String> tmpmap = map;

                    String uii = tmpmap.get("tagUii");
                    String tx = tmpmap.get("tx");
                    // System.out.println(uii);
                    index = checkIsExist(uii, tagList);

                    if (uii == null) {
                    } else {
                        if (index == -1) {
                            tagList.add(tmpmap);
                            datasList.clear();
                            //lvTags.setAdapter(adapter);
                            datasList.add(0, time.format(new Date(System.currentTimeMillis()))
                                    + "\n返回标签:" + uii + ";" + "天线:"+tx);
                        } else {
                            int tagcount = Integer.parseInt(
                                    tagList.get(index).get("tagCount"), 10) + 1;
                            tmpmap.put("tagCount", String.valueOf(tagcount));
                            tagList.set(index, tmpmap);
                            //adapter.notifyDataSetChanged();
//						Toast.show(mContext, "收到标签号为：" + uii + "; 读取次数为:" + String.valueOf(tagcount), 1000);
                        }
                    }
                    sendMsg(1);

                    break;
                case 1:
                    tagsAdapter.notifyDataSetChanged();
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
        setContentView(R.layout.activity_main);

        // �������ı���Ľ�����Զ������ļ���
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                        | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        mContext = MainActivity.this;

        imgbtn = (ImageView) findViewById(R.id.undo);
        imgbtn.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                MainActivity.this.finish();
                return false;
            }
        });
        pichead = (ImageView) findViewById(R.id.imageView1);
        pepname = (TextView) findViewById(R.id.textView1);
        peptime = (TextView) findViewById(R.id.TextView01);
        addDev = (Button) findViewById(R.id.addDev);
        addDev.setOnClickListener(this);

        Intent i = getIntent();
        longinname = i.getStringExtra("lname");
        addressPort = i.getStringExtra("addressPort");
        if (longinname.equals("test1")) {
            pichead.setImageResource(R.drawable.t1);
        } else if (longinname.equals("test2")) {
            pichead.setImageResource(R.drawable.t2);
        }
        pepname.setText("操作人:" + longinname);
        peptime.setText("操作时间:"
                + time.format(new Date(System.currentTimeMillis())));

        tagsList = (ListView) findViewById(R.id.tagsList);

        tagsAdapter = new ItemtwoAdapter(mContext);
        tagsAdapter.setData(datasList, null);
        tagsList.setAdapter(tagsAdapter);

        tagList = new ArrayList<HashMap<String, String>>();

        // 服务端初始化写法
        new MainServer(Integer.parseInt(addressPort), this).start();
        // 作为客户端而初始化写法
//		 Socket socket = new Socket("192.168.31.175", 40000);
//		 MainServer.map.put("192.168.31.175", socket);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.addDev:
                startActivity(new Intent(mContext, OnlineReaderActivity.class));
                break;
        }
    }

    public int checkIsExist(String uiiStr,
                            ArrayList<HashMap<String, String>> tagList) {
        int existFlag = -1;
        try {
            String tempStr = "";
            for (int i = 0; i < tagList.size(); i++) {
                HashMap<String, String> temp = new HashMap<String, String>();
                temp = tagList.get(i);
                tempStr = temp.get("tagUii");
                if (uiiStr != "" && uiiStr.equals(tempStr)) {
                    existFlag = i;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return existFlag;
    }

    @Override
    public void execute(Object... arg0) {
        // TODO Auto-generated method stub
        int cmd = Integer.parseInt((String) arg0[1], 10);
        String returnstr = (String) arg0[0];
        String address = (String) arg0[2];
        switch (cmd) {
            case MacCmd.RCP_CMD_HEARTPACKET:
                //datasList.add(0, time.format(new Date(System.currentTimeMillis())) + "\n返回:" + returnstr + ";地址:" + address);
                // System.out.println("心跳返回" + returnstr + ";设备地址为:" + address);}
                break;
            case MacCmd.RCP_CMD_READ_C_UII:
                System.out.println("标签返回" + returnstr + ";设备地址为:" + address);
                tag = HelpUtil.getTagByCons((String) arg0[0]);
                if (tag.equals(""))
                    return;
                String tx = returnstr.substring(returnstr.length()-2,returnstr.length());
                map = new HashMap<String, String>();
                map.put("tagUii", tag);
                map.put("tx", tx);
                map.put("tagLen", String.valueOf(tag.length()));
                map.put("tagCount", String.valueOf(1));
                sendMsg(0);
                break;
        }
        sendMsg(1);
    }

    @Override
    public void execute2(Object... arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void execute3(Object... arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 100) {

        } else {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode ==4)
        {
            AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("提示").setMessage("返回到登录界面吗！");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Socket devSocket = MainServer.map.get("192.168.31.200");
                    new ModelControl().MethodCalled(devSocket, 0, ModelType.STOPINV, null);
                    finish();
                    return;
                }
            });
            builder.setNegativeButton("取消",null);
            AlertDialog alertDialog = builder.create();
            alertDialog.show();


            return false;


        }
        return super.onKeyUp(keyCode, event);
    }
}
