package com.j1.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;
import android.util.Log;

import com.j1.IRemoteCallBack;
import com.j1.ISenderUserService;
import com.j1.SingleInstance;
import com.j1.model.User;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by wenjing.liu on 18/10/8 in J1.
 *
 * @author wenjing.liu
 */
public class SenderUserService extends Service {

    ISenderUserBinder binder;
    private ScheduledThreadPoolExecutor executorExecutor;

    @Override
    public void onCreate() {
        super.onCreate();
        if (binder == null) {
            binder = new ISenderUserBinder();
        }
        //为了回调的时候开启一个定时器
        if (executorExecutor == null) {
            executorExecutor = new ScheduledThreadPoolExecutor(1);

        }
        SingleInstance.getInstance();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        if (executorExecutor == null) {
            return super.onUnbind(intent);
        }
        executorExecutor.shutdown();
        return super.onUnbind(intent);
    }

    public class ISenderUserBinder extends ISenderUserService.Stub {
        String name = null;
        int age = -1;
        String sex = null;

        @Override
        public void setUserName(String name) throws RemoteException {
            this.name = name;
            Log.w("SenderUserService", "setUserName pid = " + Process.myPid());
        }

        @Override
        public String getUserName() throws RemoteException {
            return name;
        }

        @Override
        public void setUserAge(int age) throws RemoteException {
            SingleInstance.getInstance();
            this.age = age;
        }

        @Override
        public int getUserAge() throws RemoteException {
            return age;
        }

        @Override
        public void setUserSex(String sex) throws RemoteException {
            this.sex = sex;
        }

        @Override
        public String getUserSex() throws RemoteException {
            return sex;
        }

        @Override
        public User getUser() throws RemoteException {
            if (name == null || age == -1 || sex == null) {
                throw new IllegalArgumentException("信息不完整");
            }
            return new User(name, age, sex);
        }

        @Override
        public void registerPushMessage(final IRemoteCallBack callback) throws RemoteException {
            if (executorExecutor == null) {
                return;
            }
            Log.w("SenderUserService", " registerPushMessage pid = " + Process.myPid());
            //延时10s之后，将"hello com.j1"返回
            executorExecutor.schedule(new Runnable() {
                @Override
                public void run() {
                    try {
                        callback.pushCallBack("hello com.j1");
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }, 10, TimeUnit.SECONDS);
        }
    }
}
