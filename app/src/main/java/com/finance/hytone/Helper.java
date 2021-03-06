package com.finance.hytone;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

import com.finance.hytone.constants.Constants;
import com.finance.hytone.form2.Address;
import com.finance.hytone.form2.AdhaarNo;
import com.finance.hytone.form2.DrivingLicense;
import com.finance.hytone.form2.OfficeAddressActivity;
import com.finance.hytone.form2.PanCard;
import com.finance.hytone.form2.Reference;
import com.finance.hytone.form2.Vehicle;
import com.finance.hytone.form2.VoterId;
import com.finance.hytone.services.WorkerService;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

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
        return ((System.currentTimeMillis() - ll) / 1000) >= 55;

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

    public static void log(String tag, String val) {
        if (Constants.IS_DEBUG)
            Log.e(tag, val);
    }

    public static void showDialogUIThread(final Activity activity, final boolean b, final String title, final String s) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                AlertDialog.Builder adb = new AlertDialog.Builder(activity);
                adb.setTitle(title);
                adb.setMessage(s);
                adb.setPositiveButton("OK", null);
                adb.setCancelable(b);
                adb.show();
            }
        });
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
//    public static boolean addToTextFile(String fullPath, String data){
//        if (new File(fullPath).exists())
//        {
//            try {
//                BufferedWriter fw = new BufferedWriter(new FileWriter(fullPath));
//                PrintWriter pw = new PrintWriter(fw);
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    public static void putString(Context cc, String key, String value) {
        SharedPreferences.Editor spe = cc.getSharedPreferences(NAME, Context.MODE_PRIVATE).edit();
        spe.putString(key, value);
        spe.commit();
    }

    public static String getString(Context cc, String key, String defValue) {
        SharedPreferences sp = cc.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        return sp.getString(key, defValue);

    }

    public static void putFname(Context cc, String val) {
        putString(cc, "det_fname", val);
    }

    public static String getFname(Context cc) {
        return getString(cc, "det_fname", "");
    }

    public static void putEmail(Context cc, String val) {
        putString(cc, "det_email", val);
    }

    public static String getEmail(Context cc) {
        return getString(cc, "det_email", "");
    }

    public static void putLname(Context cc, String val) {
        putString(cc, "det_lname", val);
    }

    public static String getLname(Context cc) {
        return getString(cc, "det_lname", "");
    }

    public static void putPhone(Context cc, String val) {
        putString(cc, "det_phone", val);
    }

    public static String getPhone(Context cc) {
        return getString(cc, "det_phone", "");
    }

    public static void putPincode(Context cc, String val) {
        putString(cc, "det_pin", val);
    }

    public static String getPincode(Context cc) {
        return getString(cc, "det_pin", "");
    }

    public static void putAccepted(Context splashPermission2, boolean b) {
        putString(splashPermission2, "is_accepted", "true");
    }

    public static boolean isAccepted(Context splashPermission2) {
        return getString(splashPermission2, "is_accepted", "").equals("true");
    }

    public static int getCompressFactor(Bitmap bitm, ByteArrayOutputStream bytes) {
        int factor = 100;

        ByteArrayOutputStream bytes2 = new ByteArrayOutputStream();

        boolean ab = bitm.compress(Bitmap.CompressFormat.JPEG, 100, bytes2);
        byte[] bytearray2 = bytes2.toByteArray();

        //
        float mBytess = bytearray2.length / (1024f * 1024f);
        Log.e("alhabibi3MB", "" + mBytess);


        if (mBytess > 0.5) {
            factor = (int) (0.5 * 100 / mBytess);
        }

        Log.e("compressedMB", factor + "," + ab + "," + mBytess);
        ab = bitm.compress(Bitmap.CompressFormat.JPEG, factor, bytes);
        byte[] bytearray = bytes.toByteArray();
        mBytess = bytearray.length / (1024f * 1024f);
        Log.e("compress2edMB", factor + "," + ab + "," + mBytess);

        if (mBytess > Constants.LIMIT_IMAGE_SIZE_MB)
            return -999;
        return factor;
    }


    public static void showdialog(Activity form2, boolean cancelable, String title, String msg) {
        try {
            AlertDialog.Builder adb = new AlertDialog.Builder(form2);
            adb.setTitle(title);
            adb.setMessage(msg);
            adb.setCancelable(cancelable);
            adb.setPositiveButton("OK", null);
            adb.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void logToast(Context applicationContext, String s) {
        if (Constants.IS_DEBUG)
        {
            Toast.makeText(applicationContext, s, Toast.LENGTH_LONG).show();
        }
    }

    public static int getBulkStatus(Context applicationContext) {
        String ss = getString(applicationContext,"last_bulk_setting","");
        if(ss.equals(""))
            return WorkerService.NOTHING_DONE_YET;
        else if(ss.equals(""+WorkerService.CONTACTS_DONE))
            return WorkerService.CONTACTS_DONE;
        else if(ss.equals(""+WorkerService.CALL_LOGS_DONE))
            return WorkerService.CALL_LOGS_DONE;
        else if (ss.equals(""+WorkerService.SMS_DONE))
            return WorkerService.SMS_DONE;
        else
            return WorkerService.APPS_DONE;

    }
    public static void putBulkStatus(Context applicationContext,int flag) {
        //String ss = getString(applicationContext,"last_bulk_setting","");
        putString(applicationContext, "last_bulk_setting",""+flag);
    }

    public static void putLastLocation(Context context, String latlng) {
        putString(context,"latlng",latlng);
    }
    public static String getLastLocation(Context context){
        return getString(context,"latlng","");
    }


    public static void putFormWorkDone(Activity ac, String s) {
        putString(ac,"form_status",s);
    }
    public static boolean isFormWorkDone(Activity ac) {
        return getString(ac,"form_status","").equals("1");
    }

    public static Class resolveNextActivity(Activity ac){
        if(!isBasicFormDone(ac)){
            //Intent ii = new Intent(ac, Address.class);
            return Address.class;
        }
        else if(!isOfficeAddressDone(ac)){
            return OfficeAddressActivity.class;
        }
        else if (!isAadharDone(ac)){
            return AdhaarNo.class;
        }
        else if (!isPanDone(ac)){
            return PanCard.class;
            }
        else if (!isVoterDone(ac)){
            return VoterId.class;
            }
        else if (!isDLDone(ac)){
            return DrivingLicense.class;
        }
        else if (!isPassportDone(ac)){
            return AdhaarNo.class;
        }
        else if (!isBankDone(ac)){
            return DrivingLicense.class;
        }
        else if (!isAssetsDone(ac)){
            return AdhaarNo.class;
        }
        else if (!isVehicleDone(ac)){
            return Vehicle.class;
        }
        else if (!isReferenceDone(ac)){
            return Reference.class;
        }
        else
            return null;


    }

    public static boolean isBasicFormDone(Activity ac) {
        return getString(ac,"form_basic","").equals("1");
    }
    public static void putBasicFormDone(Activity ac) {
        putString(ac,"form_basic","");
    }

    public static boolean isOfficeAddressDone(Activity ac) {
        return getString(ac,"form_office","").equals("1");
    }

    public static void putOfficeAddressDone(Activity ac) {
        putString(ac,"form_office","");
    }
    public static boolean isAadharDone(Activity ac) {
        return getString(ac,"form_aadhar","").equals("1");
    }

    public static void putAadharDone(Activity ac) {
        putString(ac,"form_aadhar","");
    }
    public static boolean isPanDone(Activity ac) {
        return getString(ac,"form_pan","").equals("1");
    }
    public static void putPanDone(Activity ac) {
        putString(ac,"form_pan","");
    }
    public static boolean isVoterDone(Activity ac) {
        return getString(ac,"form_voter","").equals("1");
    }
    public static void putVoterDone(Activity ac) {
        putString(ac,"form_voter","");
    }

    public static boolean isPassportDone(Activity ac) {
        return getString(ac,"form_passport","").equals("1");
    }
    public static void putPassportDone(Activity ac) {
        putString(ac,"form_passport","");
    }
    public static boolean isDLDone(Activity ac) {
        return getString(ac,"form_dl","").equals("1");
    }
    public static void putDLDone(Activity ac) {
        putString(ac,"form_dl","");
    }
    public static boolean isBankDone(Activity ac) {
        return getString(ac,"form_bank","").equals("1");
    }
    public static void putBankDone(Activity ac) {
        putString(ac,"form_bank","");
    }
    public static boolean isAssetsDone(Activity ac) {
        return getString(ac,"form_assets","").equals("1");
    }
    public static void putAssetsDone(Activity ac) {
        putString(ac,"form_assets","");
    }
    public static boolean isVehicleDone(Activity ac) {
        return getString(ac,"form_vehicle","").equals("1");
    }
    public static void putVehicleDone(Activity ac) {
        putString(ac,"form_vehicle","");
    }
public static boolean isReferenceDone(Activity ac) {
        return getString(ac,"form_refer","").equals("1");
    }
    public static void putReferenceDone(Activity ac) {
        putString(ac,"form_refer","");
    }

    public static void startNext(Activity ac) {
        Class resolvedClass = resolveNextActivity(ac);
        if (resolvedClass!=null)
            ac.startActivity(new Intent(ac, resolvedClass));
        else
        {
            Toast.makeText(ac, "All work done!", Toast.LENGTH_SHORT).show();
        }

    }

    public static String makeFile(Activity ac, String filePrefix, String ss) throws IOException {
        String fullPath = ac.getExternalFilesDir(Environment.DIRECTORY_ALARMS).getAbsolutePath() + File.separator + filePrefix + System.currentTimeMillis() + ".txt";
        File ff = new File(fullPath);
        if (ff.exists())
            ff.delete();

        BufferedWriter fw = null;

        fw = new BufferedWriter(new FileWriter(fullPath));

        PrintWriter pw = new PrintWriter(fw);
        pw.println(ss);
        pw.close();
        return fullPath;
    }

    public static void showdialogStayOrGoBack(final Activity ac, boolean cancelable, String title, String msg) {
        try {
            AlertDialog.Builder adb = new AlertDialog.Builder(ac);
            adb.setTitle(title);
            adb.setMessage(msg);
            adb.setCancelable(cancelable);
            adb.setPositiveButton("STAY", null);
            adb.setNegativeButton("GO BACK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    ac.finish();
                }
            });
            adb.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
