package com.mutisocket;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.mutisocket.tools.App;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Stereo on 16/4/16.
 */
public class Service {


    String url;
    private String uii, ant_num, dt;
    public static List<Map<String, String>> list = new ArrayList<Map<String, String>>();
    List<String> lists=new ArrayList<String>();
    private String datajson;

    public Boolean Iswhile=false;

    public Service()
    {
        new Thread(runnable).start();
    }


    public void setTaginfo(String _uii, String _ant_num)
    {
        this.uii = "3000" + _uii.toUpperCase();
        this.ant_num = _ant_num;
        dt = String.valueOf(new Date().getTime());
        if (lists.contains(uii))
            return;
        else
            lists.add(uii);
        Map<String, String> map = new HashMap<String, String>();
        map.put("label_code", uii);
        map.put("ant_num", ant_num);
        map.put("dt", dt);
        list.add(map);

    }
    public Service(String _uii, String _ant_num) {

        this.uii = "3000" + _uii.toUpperCase();
        this.ant_num = _ant_num;
        dt = String.valueOf(new Date().getTime());

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("label_code", uii);
            jsonObject.put("time_stamp", dt);
            jsonObject.put("ant_num", ant_num);
            JSONArray jsonArray = new JSONArray();
            jsonArray.put(jsonObject);
            datajson = jsonArray.toString();


        } catch (Exception e) {
            e.printStackTrace();
        }
        new Thread(runnable).start();
    }

    public Service(String _uii, String _ant_num, String _dt) {

        this.uii = _uii;
        this.ant_num = _ant_num;
        this.dt = _dt;

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("label_code", uii);
            jsonObject.put("time_stamp", dt);
            jsonObject.put("ant_num", ant_num);
            JSONArray jsonArray = new JSONArray();
            jsonArray.put(jsonObject);
            datajson = jsonArray.toString();


        } catch (Exception e) {
            e.printStackTrace();
        }
        new Thread(runnable).start();
    }


    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            while (Iswhile) {
                try {
                    url = getUrl("uhf/scanBoxByTags");
                    for (Map<String,String> map:list)
                    {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("label_code", map.get("label_code"));
                        jsonObject.put("time_stamp", map.get("dt"));
                        jsonObject.put("ant_num", map.get("ant_num"));
                        JSONArray jsonArray = new JSONArray();
                        jsonArray.put(jsonObject);
                        datajson = jsonArray.toString();
                        KQ_scanBoxByTags(datajson,map);
                    }
                    Thread.sleep(3000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };


    public String getUrl(String Method) {
        return String.format("%1$s%2$s", App.ServerUrl, Method);
    }


    /**
     * 提交数据
     *
     * @param _json
     */
    public void KQ_scanBoxByTags(String _json,Map<String ,String> map) {
        HttpClientClass httpClientClass = null;

        try {
            AjaxHttpPlugin ajaxHttpPlugin = new AjaxHttpPlugin();
            httpClientClass = ajaxHttpPlugin.initHttp();
            httpClientClass.openRequest(url, HttpClientClass.REQ_METHOD_POST);
            httpClientClass.addBodyData("taglist", _json);
            httpClientClass.setEntity(httpClientClass.getPostBodyData());
            Boolean result = httpClientClass.sendRequest();
            if (!result) {
                return;
            }
            byte[] buffer = httpClientClass.getRespBodyData();
            String json = new String(buffer, "UTF-8");

            try {
                JSONObject jsonObject = new JSONObject(json);
                int r = jsonObject.getInt("type");
                if (r != 1) {
                    return;
                } else {
                    list.remove(map);
                    lists.remove(map.get("label_code"));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            Log.i("队列 ",String.valueOf(list.size()));
            Log.i("返回数据", json);


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (httpClientClass != null) {
                httpClientClass.closeRequest();
                httpClientClass = null;
            }
        }

    }


}
