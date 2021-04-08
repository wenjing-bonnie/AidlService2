// ISenderUserService.aidl
package com.j1;

// Declare any non-default types here with import statements
import com.j1.model.User;
import com.j1.IRemoteCallBack;

//一个进程中去给另外一个进程设置，然后从另外一个进程中读出用户信息
interface ISenderUserService {

     void setUserName(String name);
     String getUserName();

     void setUserAge(int age);
     int getUserAge();

     void setUserSex(String sex);
     String getUserSex();

     User getUser();

     void registerPushMessage(IRemoteCallBack callback);
}


