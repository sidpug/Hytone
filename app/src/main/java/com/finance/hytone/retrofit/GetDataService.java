package com.finance.hytone.retrofit;

import com.google.gson.JsonObject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface GetDataService {

//    @GET("/photos")
//    Call<List<RetroPhoto>> getAllPhotos();
//@Headers({
 //       "Content-Type: application/json",
   //     "Accept: application/json"
//        "User-Agent: EklavyaApp",
//        "Cache-Control: max-age=640000",
//        "Connection: keep-alive"

//})
    @FormUrlEncoded
    @POST("/api/auth/login")
    Call<JsonObject> getLoginResponse(@Field("mobile") String mobile, @Field("password") String password);


    @FormUrlEncoded
    @POST("/api/some-endpoint")
    Call<JsonObject> getQuestionList(@Header("Authorization") String auth, @Field("id") String east_id, @Field("device_type") String device_type);


}

