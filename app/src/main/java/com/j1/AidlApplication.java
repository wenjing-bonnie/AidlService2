package com.j1;

import android.app.Application;
import android.os.Process;
import android.util.Log;

/**
 * Created by wenjing.liu on 18/10/9 in J1.
 *
 * @author wenjing.liu
 */
public class AidlApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("AidlApplication", "pid = " + Process.myPid());
    }
}
