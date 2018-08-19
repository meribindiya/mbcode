package com.meribindiyaemployee.meribindiyaemployee.services;

import com.meribindiyaemployee.meribindiyaemployee.responses.CommonResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface Services {

    @GET("login")
    Call<CommonResponse> getLogin(@QueryMap Map<String, String> filter);

    @GET("orders/all/{id}")
    Call<CommonResponse> getAllOrdersByBeauticianId(@Path("id") Integer id);

    @GET("orders/details/{id}")
    Call<CommonResponse> getOrderDetailByOrderId(@Path("id") Long id);

    @GET("orders/action/{orderid}/{code}")
    Call<CommonResponse> changeOrderStatus(@Path("orderid") Long orderid, @Path("code") Integer code);


}


