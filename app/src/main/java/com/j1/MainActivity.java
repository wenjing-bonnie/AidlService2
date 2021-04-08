package com.j1;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.j1.service.SenderUserService;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainActivity";
    private Button btnStartService;
    private Button btnStopService;
    private TextView tvUser;
    private TextView tvCallBack;
    private ISenderUserService userService;
    private boolean isBinderService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnStartService = (Button) findViewById(R.id.btn_start_service);
        btnStopService = (Button) findViewById(R.id.btn_stop_service);
        tvUser = (TextView) findViewById(R.id.tv_user);
        tvCallBack = (TextView) findViewById(R.id.tv_callback);
        btnStartService.setOnClickListener(clickListener);
        btnStopService.setOnClickListener(clickListener);
        SingleInstance.getInstance();
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_start_service:
                    SingleInstance.getInstance();
                    startService();
                    break;
                case R.id.btn_stop_service:
                    stopService();
                    break;
                default:
            }
        }
    };
    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.v(TAG, " onServiceConnected pid = " + Process.myPid());
            //获得接口类
            userService = ISenderUserService.Stub.asInterface(service);
            //com.j1进程去设置userservice进程中的相关内容
            try {
                userService.registerPushMessage(callBack);
                userService.setUserName("张三");
                userService.setUserAge(20);
                userService.setUserSex("male");
                //经过userservice进程进行计算，得到User对象
                tvUser.setText(userService.getUser().toString());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    /**
     * userservice进程主动向com.j1发送信息
     */
    private IRemoteCallBack.Stub callBack = new IRemoteCallBack.Stub() {
        @Override
        public void pushCallBack(String hello) throws RemoteException {
            tvCallBack.setText(hello);
            Log.v(TAG, " IRemoteCallBack pid = " + Process.myPid());
        }
    };

    private void startService() {


        Intent intent = new Intent(MainActivity.this, SenderUserService.class);
        //intent.setAction("com.j1.SenderUserService");
        isBinderService = bindService(intent, connection, Context.BIND_AUTO_CREATE);


    }

    private void stopService() {
        if (connection == null || !isBinderService) {
            return;
        }
        unbindService(connection);
        //connection = null;
        isBinderService = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService();
    }
}
