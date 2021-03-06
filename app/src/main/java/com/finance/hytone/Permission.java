package com.finance.hytone;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.core.app.ActivityCompat;

public class Permission {
    public static void permission(Activity activity) {
        if (!checkAllPermissions(activity)) {//not granted, this case can arise only for >=23(Marshmallow)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Log.e("log_case", "requseting");
                ActivityCompat.requestPermissions(activity,
                        getPermissions(),
                        1);

            }
        } else {
            //permission is granted, continue with regular flow
            activity.startActivity(new Intent());
            Log.e("log_case", "permission granted already!!!!");


        }
    }

    private static String[] getPermissions() {
        return new String[]{Manifest.permission.READ_CALL_LOG,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_BACKGROUND_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_CONTACTS, Manifest.permission.READ_SMS, Manifest.permission.READ_PHONE_NUMBERS,
                Manifest.permission.CAMERA, Manifest.permission.RECEIVE_SMS, Manifest.permission.FOREGROUND_SERVICE};
    }

    public static boolean checkAllPermissions(Activity activity) {
        //returns true if all permissions granted
        int c = 0;
        boolean b = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        Log.e("c" + (c++), "" + b);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            b = b && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED;
        }
        Log.e("c" + (c++), "" + b);
        b = b && ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED;
        Log.e("c" + (c++), "" + b);
        b = b && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        Log.e("c" + (c++), "" + b);

        b = b && ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED;
        Log.e("c" + (c++), "" + b);

        b = b && ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED;
        Log.e("c" + (c++), "" + b);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            b = b && ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_NUMBERS) == PackageManager.PERMISSION_GRANTED;
        }
        Log.e("c" + (c++), "" + b);

        b = b && ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        Log.e("c" + (c++), "" + b);

        b = b && ActivityCompat.checkSelfPermission(activity, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED;
        Log.e("c" + (c++), "" + b);

//        b = b && ActivityCompat.checkSelfPermission(activity, Manifest.permission.MEDIA_CONTENT_CONTROL) == PackageManager.PERMISSION_GRANTED;
//        Log.e("c"+(c++),""+b);

        b = b && ActivityCompat.checkSelfPermission(activity, Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED;
        Log.e("c" + (c++), "" + b);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            b = b && ActivityCompat.checkSelfPermission(activity, Manifest.permission.FOREGROUND_SERVICE) == PackageManager.PERMISSION_GRANTED;
        }
        Log.e("c" + (c++), "" + b);

        b = b && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        Log.e("c" + (c), "" + b);

b = b && ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        Log.e("c" + (c), "" + b);


        return b;

    }

    /* @Override public void onRequestPermissionsResult(int requestCode,
    String permissions[], int[] grantResults) {
    switch (requestCode) {
    case 1: {

    // If request is cancelled, the result arrays are empty.
    if (grantResults.length > 0
    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

    // permission was granted, yay! Do the
    // contacts-related task you need to do.
    Toast.makeText(MainActivity.this, "Permission GRANTED", Toast.LENGTH_SHORT).show();
    Log.e("log_case2","GRANTED!");

    } else {

    // permission denied, boo! Disable the
    // functionality that depends on this permission.
    Toast.makeText(MainActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
    Log.e("log_case3","NOT GRANTED!");

    }
    return;
    }

    // other 'case' lines to check for other
    // permissions this app might request
    }
    }*/
}

