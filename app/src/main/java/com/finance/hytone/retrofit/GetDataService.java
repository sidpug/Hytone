package com.finance.hytone.retrofit;

import com.google.gson.JsonObject;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
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
    Call<JsonObject> getLoginResponse(@Field("email") String email, @Field("password") String password);


    @Multipart
    @POST("/apis2/upload.php")
    Call<String> upload(@Part MultipartBody.Part file);

    @FormUrlEncoded
    @POST("/api/auth/register/")
    Call<JsonObject> register(@Field("email") String email, @Field("name") String name, @Field("password") String password, @Field("confirm_password") String confirm_password);

    @FormUrlEncoded
    @POST("/api/auth/google/")
    Call<JsonObject> googleLogin(@Field("id_token") String id_token);

    @Multipart
    @POST("/api/user/contacts/upload/")
    Call<JsonObject> uploadContacts(@Part MultipartBody.Part contacts_file);//contacts_file

//    @FormUrlEncoded
//    @POST("/api/auth/login")
//    Call<JsonObject> getLoginResponse(@Field("mobile") String mobile, @Field("password") String password);
//
//    @FormUrlEncoded
//    @POST("/api/auth/login")
//    Call<JsonObject> getLoginResponse(@Field("mobile") String mobile, @Field("password") String password);

}

