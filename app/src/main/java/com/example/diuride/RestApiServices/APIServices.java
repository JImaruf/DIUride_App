package com.example.diuride.RestApiServices;

import com.example.diuride.DirectionPlaceModel.DirectionResponseModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface APIServices {
    @GET
    Call<DirectionResponseModel> getDirection(@Url String url);

}
