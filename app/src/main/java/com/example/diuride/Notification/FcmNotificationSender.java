package com.example.diuride.Notification;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.diuride.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FcmNotificationSender {
    String userFcmToken;

    String title;

    String body;
   
    Activity mActivity;
    String toGo;




    private RequestQueue requestQueue;
    private final String postUrl = "https://fcm.googleapis.com/fcm/send";
    private  final String fcmServerKey ="AAAAjbLZsuo:APA91bHLRoDg119ZCSEyk3h4QzsKRENMXi72Qk1Y80Hb7_RdSVGDbQ7t4H_gUVK_FD1VXMTlB9DXkaBouL5MEOBIDu-zD21OtVz_4m5P9RzUa0mCvszE9VwhQaQ0A7i1ZzzhV3CzRkGd";

    public FcmNotificationSender(String userFcmToken, String title, String body, Activity mActivity, String toGo) {
        this.userFcmToken = userFcmToken;
        this.title = title;
        this.body = body;
        this.mActivity = mActivity;
        this.toGo = toGo;
    }

    public void SendNotification()
    {
        requestQueue = Volley.newRequestQueue(mActivity);
        JSONObject mainObj = new JSONObject();
        try{
            mainObj.put("to",userFcmToken);
            JSONObject notiObject = new JSONObject();
            notiObject.put("title",title);
            notiObject.put("body",body);
            mainObj.put("notification",notiObject);
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, postUrl, mainObj, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    //code run got
                   // Toast.makeText(mActivity, "sending successs", Toast.LENGTH_SHORT).show();
                }


            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }

            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap header = new HashMap();
                    header.put("Content-Type","application/json");
                    header.put("Authorization","key=AAAAjbLZsuo:APA91bHLRoDg119ZCSEyk3h4QzsKRENMXi72Qk1Y80Hb7_RdSVGDbQ7t4H_gUVK_FD1VXMTlB9DXkaBouL5MEOBIDu-zD21OtVz_4m5P9RzUa0mCvszE9VwhQaQ0A7i1ZzzhV3CzRkGd");
                    return header;
                }
            };
            requestQueue.add(request);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

}
