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
    @POST("/api/auth/register")//http://ekl.edudigm.com/api/auth/login
    Call<JsonObject> register(@Field("name") String name, @Field("phone") String phone, @Field("password") String pass, @Field("class") String str_class, @Field("email") String email);

    @FormUrlEncoded
    @POST("/api/auth/register/verify-otp")
    Call<JsonObject> verifyOtpRegister(@Field("phone") String phone, @Field("otp") String otp);

    @FormUrlEncoded
    @POST("/api/auth/register/resend-otp")
    Call<JsonObject> resendOtpRegister(@Field("phone") String phone);

    @Multipart
    @POST("/api/auth/update-profile")
    Call<JsonObject> updateProfile(@Header("Content-Type") String contentType, @Header("Accept") String accept, @Header("Authorization") String auth, @Part("name") RequestBody name,
                                   @Part("phone") RequestBody phone,
                                   @Part("email") RequestBody email,
                                   @Part("class") RequestBody class_,
                                   @Part MultipartBody.Part image);
    @FormUrlEncoded
    @POST("/api/auth/update-profile")
    Call<JsonObject> updateProfile2(@Header("Authorization") String auth, @Field("class") String class_);

//    @Headers({
//                   "Content-Type: application/json",
//                 "Accept: application/json",
//        "User-Agent: EklavyaApp",
//        "Cache-Control: max-age=640000",
//        "Connection: keep-alive"
//
//    })
    @FormUrlEncoded
    @POST("/api/auth/reset-password")
    Call<JsonObject> resetPass(@Field("phone") String phone);//,@Field("password") String password);

    @FormUrlEncoded
    @POST("/api/auth/reset-password/verify-otp")
    Call<JsonObject> verifyOtpResetPass(@Field("phone") String phone, @Field("otp") String otp, @Field("password") String password);

    @FormUrlEncoded
    @POST("/api/content/get-dashboard-details")
    Call<JsonObject> getDashboardDetails(@Header("Authorization") String auth, @Field("subscription_id") String subscription_id);

    @FormUrlEncoded
    @POST("/api/content/get-subject-details")
    Call<JsonObject> getSubjectDetails(@Header("Authorization") String auth, @Field("subscription_id") String subscription_id, @Field("subject_id") String subject_id);

    @FormUrlEncoded
    @POST("/api/content/get-chapter-products")
    Call<JsonObject> getChapterProducts(@Header("Authorization") String auth, @Field("subscription_id") String subscription_id, @Field("chapter_id") String chapter_id);

    @FormUrlEncoded
    @POST("/api/assessment/get-assessment-question-list")
    Call<JsonObject> getQuestionList(@Header("Authorization") String auth, @Field("course_content_id") String course_content_id, @Field("device_type") String device_type);

    @FormUrlEncoded
    @POST("/api/assessment/get-question-detail")
    Call<JsonObject> getQuestionDetail(@Header("Authorization") String auth, @Field("course_content_id") String course_content_id, @Field("section_question_id") String section_question_id);

    @FormUrlEncoded
    @POST("/api/assessment/submit-exam")
    Call<JsonObject> submitExam(@Header("Authorization") String token, @Field("course_content_id") String course_content_id, @Field("subscription_id") String subscription_id, @Field("overall_time_spent") String overall_time_spent);

    @FormUrlEncoded
    @POST("/api/assessment/submit-exam")
    Call<JsonObject> submitBubblesTypeExam(@Header("Authorization") String token, @Field("course_content_id") String course_content_id, @Field("subscription_id") String subscription_id, @Field("overall_time_spent") String overall_time_spent, @Field("responses") String responses);

    @FormUrlEncoded
    @POST("/api/assessment/get-solution-list")
    Call<JsonObject> getResultData(@Header("Authorization") String token, @Field("result_id") String result_id);

    @FormUrlEncoded
    @POST("/api/assessment/get-mocks-list")
    Call<JsonObject> getMocksList(@Header("Authorization") String token, @Field("subscription_id") String subscription_id);


    @FormUrlEncoded
    @POST("/api/assessment/start-exam")
    Call<JsonObject> startExam(@Header("Authorization") String token, @Field("course_content_id") String course_content_id, @Field("subscription_id") String subscription_id);

    @FormUrlEncoded
    @POST("/api/assessment/submit-question")
    Call<JsonObject> saveSingleAnswer(@Header("Authorization") String token, @Field("section_question_id") String section_question_id, @Field("course_content_id") String course_content_id, @Field("marked") String marked, @Field("time_spent") String time_spent);

    @FormUrlEncoded
    @POST("/api/assessment/get-solution-list")
    Call<JsonObject> getSolutionList(@Header("Authorization") String token, @Field("result_id") String result_id);

    @FormUrlEncoded
    @POST("/api/assessment/get-question-solution")
    Call<JsonObject> getQuestionSolution(@Header("Authorization") String token, @Field("question_id") String question_id, @Field("course_content_id") String course_content_id);

    @FormUrlEncoded
    @POST("/api/assessment/save-error-reason")
    Call<JsonObject> saveErrorReason(@Header("Authorization") String token, @Field("course_content_id") String course_content_id, @Field("question_id") String question_id, @Field("error_type_id") String error_type_id);

    @POST("/api/queries/get-faqs")
    Call<JsonObject> getFaqs(@Header("Authorization") String token);

    @GET("/api/queries/get-topics-and-history")
    Call<JsonObject> getTopicsAndHistory(@Header("Authorization") String token);

    @FormUrlEncoded
    @POST("/api/queries/get-items")
    Call<JsonObject> getItems(@Header("Authorization") String token, @Field("subscription_id") String subscription_id, @Field("child_type") String child_type, @Field("parent_id") String parent_id);

    @FormUrlEncoded
    @POST("/api/queries/create-query")
    Call<JsonObject> createQuery(@Header("Authorization") String token, @Field("root_topic_id") String root_topic_id, @Field("description") String description);

    @FormUrlEncoded
    @POST("/api/queries/get-query-details")
    Call<JsonObject> getQueryDetails(@Header("Authorization") String token, @Field("query_id") String query_id);

    @FormUrlEncoded
    @POST("/api/queries/create-reply")
    Call<JsonObject> createQueryReply(@Header("Authorization") String token, @Field("query_id") String query_id, @Field("is_resolved") String is_resolved, @Field("description") String description);

    @GET("/api/forum/get-categories")
    Call<JsonObject> getForumCategories(@Header("Authorization") String token);

    @FormUrlEncoded
    @POST("/api/forum/save-category")
    Call<JsonObject> saveSingleCategory(@Header("Authorization") String token, @Field("category_id") String category_id);

    @GET("/api/east/get-east-home")
    Call<JsonObject> getEastHome(@Header("Authorization") String token);

    @FormUrlEncoded
    @POST("/api/east/start-exam")
    Call<JsonObject> startEASTExam(@Header("Authorization") String token, @Field("east_id") String east_id, @Field("school_code") String school_code);

    @FormUrlEncoded
    @POST("/api/east/get-question-list")
    Call<JsonObject> getEASTQuestionList(@Header("Authorization") String auth, @Field("east_id") String east_id, @Field("device_type") String device_type);

    @FormUrlEncoded
    @POST("/api/east/get-question-detail")
    Call<JsonObject> getEASTQuestionDetail(@Header("Authorization") String auth, @Field("east_id") String east_id, @Field("section_question_id") String section_question_id);

    @FormUrlEncoded
    @POST("/api/east/submit-question")
    Call<JsonObject> saveEASTSingleAnswer(@Header("Authorization") String token, @Field("section_question_id") String section_question_id, @Field("east_id") String east_id, @Field("marked") String marked, @Field("time_spent") String time_spent);

    @FormUrlEncoded
    @POST("/api/east/submit-exam")
    Call<JsonObject> submitEASTExam(@Header("Authorization") String token, @Field("east_id") String east_id, @Field("overall_time_spent") String overall_time_spent);

    //Call<String> getLoginResponse(@Body String body);
//Call<JSONObject> getLoginResponse( @Body RequestBody body);
//Call<JSONObject> getLoginResponse( );
}

