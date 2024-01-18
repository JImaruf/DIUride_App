package com.example.diuride.Notification;

import com.example.diuride.DirectionPlaceModel.DirectionResponseModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.HEAD;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface apiInterface {

    @Headers({"Content-Type:application/json",
            "Authorization:key=AAAAjbLZsuo:APA91bHLRoDg119ZCSEyk3h4QzsKRENMXi72Qk1Y80Hb7_RdSVGDbQ7t4H_gUVK_FD1VXMTlB9DXkaBouL5MEOBIDu-zD21OtVz_4m5P9RzUa0mCvszE9VwhQaQ0A7i1ZzzhV3CzRkGd"})
    @POST("fcm/send")
    Call<PushNotification> sendNotification(@Body PushNotification notification);



}
