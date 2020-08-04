package com.finance.hytone.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.widget.Toast;

import com.finance.hytone.Helper;
import com.finance.hytone.constants.HttpResponseUtils;
import com.finance.hytone.form2.Address;
import com.finance.hytone.retrofit.GetDataService;
import com.finance.hytone.retrofit.RetrofitClientInstance;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiUtils {

    public static void uploadDetails(final Activity ac, final String form_name, String fullPath, final ProgressDialog pd){
        GetDataService service = RetrofitClientInstance.getRetrofitInstanceForFile().create(GetDataService.class);
        Call<String> call = null;
        try {

            File file = new File(fullPath);

            RequestBody requestFile =
                    RequestBody.create(
                            // MediaType.parse("image/jpg"),
                            MediaType.parse("text/plain"),
                            file
                    );

            //String items = "[1,2,4]";

            MultipartBody.Part body =
                    MultipartBody.Part.createFormData("fileToUpload", file.getName(), requestFile);

            //RequestBody items = RequestBody.create(MediaType.parse("application/json"), items);
            //RequestBody stringValue = RequestBody.create(MediaType.parse("text/plain"), stringValue);


            call = service.upload(body);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    //findViewById(R.id.loadingContainer).setVisibility(GONE);
                    pd.dismiss();
                    int rescode = response.code();
                    if (rescode != 200) {
                        HttpResponseUtils.showBasicResponseLogsString(response);
                        HttpResponseUtils.processHeaderCode(ac, rescode);
                        return;
                    }
                    Helper.log("passsed7", "here");
                    HttpResponseUtils.showBasicResponseLogsString(response);
                    try {
                        String resBody = response.body();
                        Toast.makeText(ac, "Done!", Toast.LENGTH_LONG).show();
                        Helper.putBasicFormDone(ac);

                        Helper.putString(ac,"form_"+form_name,"1");
                        Helper.startNext(ac);


                    } catch (Exception e) {
                        Helper.showDialog(ac, false, "Something went wrong. " + e.getMessage(), "" + e.toString());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    //findViewById(R.id.loadingContainer).setVisibility(GONE);
                    //HttpResponseUtils.handleFailure(ac, call, t);
                    //Helper.log("failcase",t.toString());
                    pd.dismiss();
                    t.printStackTrace();
                    Helper.log("agog111", "" + call.request().toString());
                    Helper.log("agog1111", "1" + call.request().headers());

                    //to be activated when api goes live
                    //Helper.showDialog(ac, false, "Network failure!", "Please check your internet!" );

                    //to be removed when api goes live
                    Helper.putString(ac,"form_"+form_name,"1");
                    Helper.startNext(ac);
                }
            });

        } catch (Exception e) {
            Helper.showDialog(ac, false, "Error occurred", "" + e);
            e.printStackTrace();
        }
    }
}
