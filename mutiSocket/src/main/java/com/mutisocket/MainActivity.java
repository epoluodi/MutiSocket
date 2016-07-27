package com.mutisocket;

import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
    DownloadManager downloadManager;
    DownloadCompleteReceiver downloadCompleteReceiver;


    SimpleDateFormat time = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
    ImageView imgbtn;
    String longinname;
    String addressPort;
    ImageView pichead;
    TextView pepname;
    TextView peptime;
    Button addDev;
    Context mContext;
    Button update;

    Service service;
    Timer timer;
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
                    Bundle bundle=msg.getData();

                    String uii = bundle.getString("tag");
                    String tx = bundle.getString("tx");
                    service.setTaginfo(uii,tx);
                    // System.out.println(uii);
//                    index = checkIsExist(uii, tagList);

                    if (uii == null) {
                    } else {
//                        tagList.add(tmpmap);
//                        datasList.clear();
                        //lvTags.setAdapter(adapter);
                        datasList.add(0, time.format(new Date(System.currentTimeMillis()))
                                + "\n返回标签:" + uii + ";" + "天线:" + tx);
                        tagsAdapter.notifyDataSetChanged();

                        if (datasList.size()>6)
                            datasList.clear();

                    }

                    break;
                case 1:
                    tagsAdapter.notifyDataSetChanged();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private void sendMsg(Message msg) {

        handler.sendMessage(msg);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags(0x80000000);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        service =new Service();
        service.Iswhile=true;
//        timer=new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                List<Map<String,String>> mapList = new ArrayList<Map<String, String>>();
//                for (Map<String,String> map:Service.list) {
//                    Map<String,String> map2 =new HashMap<String, String>();
//                    map2.put("uii",map.get("uii"));
//                    map2.put("ant_num",map.get("ant_num"));
//                    map2.put("dt",map.get("dt"));
//                    mapList.add(map2);
//                }
//
//                for (Map<String,String> map:mapList) {
//                   new Service(map.get("uii"),map.get("ant_num"),map.get("dt"));
//                }
//                mapList.clear();
//                mapList=null;
//
//            }
//        },15000);


        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                        | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        mContext = MainActivity.this;

        imgbtn = (ImageView) findViewById(R.id.undo);
//        imgbtn.setOnTouchListener(new OnTouchListener() {
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                MainActivity.this.finish();
//                return false;
//            }
//        });
        update = (Button)findViewById(R.id.update);
        update.setOnClickListener(onClickListener);
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


    //更新app
    OnClickListener onClickListener=new OnClickListener() {
        @Override
        public void onClick(View view) {
            String updateurl = "http://services.kaiqiaole.com/UpdateApp/RFIDController.apk";
            downloadCompleteReceiver = new DownloadCompleteReceiver();
            DownloadServer downloadServer = new DownloadServer(getApplicationContext(), downloadCompleteReceiver);

            registerReceiver(downloadCompleteReceiver, new IntentFilter(
                    DownloadManager.ACTION_DOWNLOAD_COMPLETE));
            downloadManager = downloadServer.initDownloadServer(updateurl);
            if (downloadManager == null)
            {
                unregisterReceiver(downloadCompleteReceiver);

                android.widget.Toast.makeText(MainActivity.this,"更新失败", android.widget.Toast.LENGTH_SHORT).show();
            }
        }
    };

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
//                map = new HashMap<String, String>();
//                map.put("tagUii", tag);
//                map.put("tx", tx);
//                map.put("tagLen", String.valueOf(tag.length()));
//                map.put("tagCount", String.valueOf(1));

                Message message=handler.obtainMessage();
                message.what=0;
                Bundle bundle=new Bundle();
                bundle.putString("tag",tag);
                bundle.putString("tx",tx);
                message.setData(bundle);
                message.obj = tag;
                sendMsg(message);
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
            if (Service.list.size()!=0)
            {

                AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("提示").setMessage("正在上传数据不能返回！");
                builder.setPositiveButton("确定", null);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

                return false;
            }


            AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("提示").setMessage("返回到登录界面吗！");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    service.Iswhile=false;
                    Service.list.clear();
                    service.lists.clear();
                    Socket devSocket = MainServer.map.get("192.168.199.10");
                    new ModelControl().MethodCalled(devSocket, 0, ModelType.STOPINV, null);
//                    timer.cancel();
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


    // 接受下载完成后的intent
    public class DownloadCompleteReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            //判断是否下载完成的广播
            if (intent.getAction().equals(
                    DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {

                //获取下载的文件id
                long downId = intent.getLongExtra(
                        DownloadManager.EXTRA_DOWNLOAD_ID, -1);

                //自动安装apk
                unregisterReceiver(downloadCompleteReceiver);

                installAPK(downloadManager.getUriForDownloadedFile(downId));
            }
        }

        /**
         * 安装apk文件
         */
        private void installAPK(Uri apk) {

            // 通过Intent安装APK文件
            if (apk ==null)
            {

                android.widget.Toast.makeText(MainActivity.this,"下载更新失败，请重新尝试", android.widget.Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intents = new Intent();
            intents.setAction("android.intent.action.VIEW");
            intents.addCategory("android.intent.category.DEFAULT");
            intents.setType("application/vnd.android.package-archive");
            intents.setData(apk);
            intents.setDataAndType(apk, "application/vnd.android.package-archive");
            intents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intents);
            finish();


        }

    }


    public class DownloadServer {
        Context context;
        DownloadManager downloadManager;
        MainActivity.DownloadCompleteReceiver downloadCompleteReceiver;

        /**
         * 初始化下载器 *
         */

        public DownloadServer(Context context1, MainActivity.DownloadCompleteReceiver downloadCompleteReceiver1) {
            downloadCompleteReceiver = downloadCompleteReceiver1;
            context = context1;
        }

        public DownloadManager initDownloadServer(final String updateurl) {

            try {
                downloadManager = (DownloadManager) context.getSystemService(context.DOWNLOAD_SERVICE);

                //设置下载地址
                DownloadManager.Request down = new DownloadManager.Request(
                        Uri.parse(updateurl));
                down.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE
                        | DownloadManager.Request.NETWORK_WIFI);
                down.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
                down.setVisibleInDownloadsUi(true);
                down.setDestinationInExternalFilesDir(context,
                        Environment.DIRECTORY_DOWNLOADS, "RFIDController.apk");
                downloadManager.enqueue(down);

                return downloadManager;
            }
            catch (Exception e)
            {
                return null;
            }

        }


    }


}
