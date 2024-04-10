package com.example.testthi1;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;

public interface ApiService {
    public static String DOMAIN = "http://192.168.0.103:3000/xemay/";

    @GET("getListxemay")
    Call<ArrayList<XeMay>> getListSanPham();

    @Multipart
    @POST("addxemay")
    Call<Response<XeMay>> addXeMay(@PartMap Map<String, RequestBody> requestBodyMap,
                                       @Part MultipartBody.Part img);
    
    @Multipart
    @PUT("updatexemay/{id}")
    Call<Response<XeMay>> updateXeMay(
            @Path("id") String id,
            @Part("xemay") XeMay xeMay,
            @Part MultipartBody.Part img
    );

    @DELETE("deletexemay/{id}")
    Call<Response<Void>> deleteSanPham(@Path("id") String id);

    @GET("getxemayById/{id}")
    Call<com.example.testthi1.Response<XeMay>> getXeMayById(@Path("id") String id);


}
