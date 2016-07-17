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
    private String uii, ant_num;
    public static List<Map<String, String>> list = new ArrayList<Map<String, String>>();
    private String datajson;


    public Service(String uii, String ant_num) {

        this.uii ="3000"+ uii.toUpperCase();
        this.ant_num = ant_num;

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("label_code", uii);
            jsonObject.put("time_stamp", String.valueOf(new Date().getTime()));
            jsonObject.put("label_code", ant_num);
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
            try {
                url = getUrl("uhf/scanBoxByTags");
                KQ_scanBoxByTags(datajson);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


    public String getUrl(String Method) {
        return String.format("%1$s%2$s", App.ServerUrl, Method);
    }


    /**
     * 提交数据
     * @param _json
     */
    public void KQ_scanBoxByTags(String _json) {
        HttpClientClass httpClientClass = null;
        Map<String, String> map = new HashMap<String, String>();
        map.put("label_code", uii);
        map.put("ant_num", ant_num);
        try {
            AjaxHttpPlugin ajaxHttpPlugin = new AjaxHttpPlugin();
            httpClientClass = ajaxHttpPlugin.initHttp();
            httpClientClass.openRequest(url, HttpClientClass.REQ_METHOD_POST);
            httpClientClass.addBodyData("taglist", _json);
            httpClientClass.setEntity(httpClientClass.getPostBodyData());
            Boolean result = httpClientClass.sendRequest();
            if (!result) {
                list.add(map);
                return;
            }
            byte[] buffer = httpClientClass.getRespBodyData();
            String json = new String(buffer, "UTF-8");

            try {
                JSONObject jsonObject = new JSONObject(json);
                int r = jsonObject.getInt("type");
                if (r != 1) {
                    list.add(map);
                    return;
                }
                else
                    list.remove(map);

            } catch (Exception e) {
                e.printStackTrace();
            }

            Log.i("返回数据", json);


        } catch (Exception e) {
            e.printStackTrace();
            list.add(map);
        } finally {
            if (httpClientClass != null) {
                httpClientClass.closeRequest();
                httpClientClass = null;
            }
        }

    }




}
