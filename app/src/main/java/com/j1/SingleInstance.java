package com.j1;

import android.os.Process;
import android.util.Log;

/**
 * Created by wenjing.liu on 18/10/9 in J1.
 *
 * @author wenjing.liu
 */
public class SingleInstance {
    private static SingleInstance instance;

    private SingleInstance() {

    }

    public static synchronized SingleInstance getInstance() {
        if (instance == null) {
            Log.d("SingleInstance", "pid = " + Process.myPid() + " , instance = " + instance);
            instance = new SingleInstance();
            return instance;
        }
        Log.d("SingleInstance", "pid = " + Process.myPid() + " , instance = " + instance);
        return instance;
    }
}
