package com.finance.hytone.constants;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.finance.hytone.Helper;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Response;

public class HttpResponseUtils {


    public static void processHeaderCode(Activity ac, int rescode) {
        if (rescode == 401)
            Helper.showDialog(ac, true, "Request not authorized", "Please provide a valid phone and password");
        else {
            Helper.showDialog(ac, true, "", "Some error occurred " + rescode);
        }
    }

    public static void showBasicResponseLogsString(Response<String> response) {
        Helper.log("agog", "" + response.body());//.body());
        Helper.log("agog1", "" + response.raw());
        Helper.log("agog11", "" + response.message());
        Helper.log("agog111", "" + response.toString());
        Helper.log("agog1111", "" + response.headers());
        String errorBody = null;
        try {
            errorBody = response.errorBody().string();
            Helper.log("agog1", "" + errorBody);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Helper.log("agog23", "" + response.headers());

    }

    public static void showBasicResponseLogs(Response<JsonObject> response) {
        Helper.log("agog", "" + response.body());//.body());
        Helper.log("agog1", "" + response.raw());
        Helper.log("agog11", "" + response.message());
        Helper.log("agog111", "" + response.toString());
        Helper.log("agog1111", "" + response.headers());
        String errorBody = null;
        try {
            errorBody = response.errorBody().string();
            Helper.log("agog1", "" + errorBody);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Helper.log("agog23", "" + response.headers());

    }

    public static void processBodyResponseCode(Activity ac, int response_code, JSONObject jo) {
        if (jo != null && jo.has("response_msg")) {
            Helper.showDialog(ac, true, "Oops", jo.optString("response_msg"));
        } else {
            Toast.makeText(ac, "Non 200 code!", Toast.LENGTH_SHORT).show();
        }
    }

    public static void handleFailure(Activity ac, Call<JsonObject> call, Throwable t) {
        t.printStackTrace();
        Helper.log("agog111", "" + call.request().toString());
        Helper.log("agog1111", "1" + call.request().headers());
        Helper.showDialog(ac, false, "Network failure!", "" + t.getMessage());
    }

    public static void handleFailureRetry(Activity ac, Call<JsonObject> call, Throwable t, final Runnable runnableForApiCall) {
        t.printStackTrace();
        Helper.log("agog111", "" + call.request().toString());
        Helper.log("agog1111", "1" + call.request().headers());
        try {
            new AlertDialog.Builder(ac)
                    .setTitle("Network failure!")
                    .setMessage("" + t.getMessage())
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {


                        }
                    })
                    .setNegativeButton("RETRY", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new Handler().post(runnableForApiCall);
                        }
                    })
                    .show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
