package com.finance.hytone;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

import com.finance.hytone.constants.Constants;

public class Helper {

    private static final String NAME = "user_data";

    public static void putToken(Activity activity, String token) {
        SharedPreferences.Editor spe = activity.getSharedPreferences(NAME, Context.MODE_PRIVATE).edit();
        spe.putString("token", token);
        spe.commit();
    }

    public static String getToken(Context activity) {
        SharedPreferences sp = activity.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        return sp.getString("token", "");

    }

    public static void showDialog(Activity activity, boolean b, String title, String s) {
        AlertDialog.Builder adb = new AlertDialog.Builder(activity);
        adb.setTitle(title);
        adb.setMessage(s);
        adb.setPositiveButton("OK", null);
        adb.setCancelable(b);
        adb.show();


    }

    public static void putLastLocationUpdateTime(Context cc) {
        SharedPreferences.Editor spe = cc.getSharedPreferences(NAME, Context.MODE_PRIVATE).edit();
        spe.putLong("lastLocUpdate", System.currentTimeMillis());
        spe.commit();
    }

    public static boolean getLastLocationUpdateTime(Context cc) {
        SharedPreferences sp = cc.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        long ll = sp.getLong("lastLocUpdate", 0);
        if (((System.currentTimeMillis() - ll) / 1000) < 55)
            return false;
        return true;

    }

    public static boolean checkPermissions(Activity ac) {
        boolean permissionAccessCoarseLocationApproved =
                ActivityCompat.checkSelfPermission(ac,
                        Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED;

        if (permissionAccessCoarseLocationApproved) {
            Log.e("has permission", "starting");
            // App has permission to access location in the foreground. Start your
            // foreground service that has a foreground service type of "location".
            //startService();
            return true;
        } else {
            Log.e("NO permission", "requesting");

            // Make a request for foreground-only location access.
            ActivityCompat.requestPermissions(ac, new String[]{
                            Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                    Constants.LOCATION_REQUEST_CODE);
        }
        return false;
    }

    public static boolean isLocationEnabled(Context context) {
        try {
            LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
// This is new method provided in API 28
                //lm.isProviderEnabled()
                if (!lm.isLocationEnabled())
                    return false;

            } else {
// This is Deprecated in API 28
                int mode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE,
                        Settings.Secure.LOCATION_MODE_OFF);
                if (mode == Settings.Secure.LOCATION_MODE_OFF)
                    return false;

            }
            if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER) && lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                //All location services are disabled
                return true;
            }
        } catch (Exception e) {
            Toast.makeText(context, "" + e, Toast.LENGTH_SHORT).show();

        }
        return false;

    }

    public static void putLoginStatus(Activity homeActivity, int status) {
        SharedPreferences.Editor spe = homeActivity.getSharedPreferences(NAME, Context.MODE_PRIVATE).edit();
        spe.putInt("login_status", status);
        spe.commit();
    }

    public static int getLoginStatus(Activity homeActivity) {
        SharedPreferences sp = homeActivity.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        return sp.getInt("login_status", 0);
    }


    /*public static void logout(Activity assignmentResultActivity) {
        //if (isConnected(assignmentResultActivity.getApplicationContext())) {
//        if (true) {
            //callLogoutApi(assignmentResultActivity);

            putLoginStatus(assignmentResultActivity,0);

            SharedPreferences sharedPreferences = assignmentResultActivity.getSharedPreferences(NAME, Context.MODE_PRIVATE);
            String token = "" + getToken(assignmentResultActivity.getApplicationContext());
            //Creating editor to store values to shared preferences
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.clear();

            editor.commit();
            //setToken(assignmentResultActivity.getApplicationContext(), token);


            //updateTracker(assignmentResultActivity.getApplicationContext(),"logout",null);
            Intent ii = new Intent(assignmentResultActivity.getApplicationContext(), .class);
            ii.putExtra("dontchng", "1");
            ii.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            try {
                assignmentResultActivity.startActivity(ii);
            } catch (Exception e) {
                Toast.makeText(assignmentResultActivity, "" + e, Toast.LENGTH_SHORT).show();
            }
//        } else {
//            android.app.AlertDialog.Builder adb = new android.app.AlertDialog.Builder(assignmentResultActivity);
//            adb.setMessage("Please connect to Internet");
//            adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//
//                }
//            });
//            adb.show();
//        }
    }*/
}
