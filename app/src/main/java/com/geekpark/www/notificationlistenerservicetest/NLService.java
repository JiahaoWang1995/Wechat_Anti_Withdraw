package com.geekpark.www.notificationlistenerservicetest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

public class NLService extends NotificationListenerService {

    private String TAG = this.getClass().getSimpleName();
    private NLServiceReceiver nlservicereciver;
    @Override
    public void onCreate() {
        super.onCreate();
        nlservicereciver = new NLServiceReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.geekpark.www.notificationlistenerservicetest.NOTIFICATION_LISTENER_SERVICE_EXAMPLE");
        registerReceiver(nlservicereciver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(nlservicereciver);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
//        Log.i(TAG, "**********  onNotificationPosted");
//        Log.i(TAG, "ID :" + sbn.getId() + "t" + sbn.getNotification().tickerText + "t" + sbn.getPackageName());
        Intent i = new  Intent("com.geekpark.www.notificationlistenerservicetest.NOTIFICATION_LISTENER_EXAMPLE");
        if (sbn.getPackageName() != "android" && sbn.getNotification().tickerText != null){
//            if (sbn.getPackageName() == "com.tencent.mm"){
//                i.putExtra("notification_event",
//                        "微信消息\n");
//            }
//            else {
//                i.putExtra("notification_event",
//                        "非微信消息\n");
//            }
//            i.putExtra("notification_event",
//                    "监听到新消息： \n" +
//                            "来自： " + sbn.getPackageName() + "\n" +
//                            "内容为： " + sbn.getNotification().tickerText + "\n\n");
            switch (sbn.getPackageName()){
                case "com.tencent.mm":
                    i.putExtra("notification_event",
                            "监听到微信新消息: \n" +
                                    sbn.getNotification().tickerText + "\n\n");
                    break;
                case "com.tencent.mobileqq":
                    i.putExtra("notification_event",
                            "监听到QQ新消息: \n" +
                                    sbn.getNotification().tickerText + "\n\n");
                    break;
                default:
                    i.putExtra("notification_event",
                            "监听到新消息: \n" +
                                    "来自: " + sbn.getPackageName() + "\n" +
                                    sbn.getNotification().tickerText + "\n\n");
                    break;
            }
        } else {
            i.putExtra("notification_event", "消息被撤回！撤回内容为: ");
        }
        sendBroadcast(i);
    }

    @Override
         public void onNotificationRemoved(StatusBarNotification sbn) {
//        Log.i(TAG,"********** onNOtificationRemoved");
//        Log.i(TAG, "ID :" + sbn.getId() + "t" + sbn.getNotification().tickerText + "t" + sbn.getPackageName());
        Intent i = new  Intent("com.geekpark.www.notificationlistenerservicetest.NOTIFICATION_LISTENER_EXAMPLE");
        if (sbn.getPackageName() != "android" && sbn.getNotification().tickerText != null){
            i.putExtra("notification_event", "此消息被您阅读: \n" +
                    "来自: " + sbn.getPackageName() + "\n" +
                    sbn.getNotification().tickerText + "\n\n");
        }else {

        }
        sendBroadcast(i);
    }

    class NLServiceReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getStringExtra("command").equals("clearall")){
                NLService.this.cancelAllNotifications();
            }
            else if(intent.getStringExtra("command").equals("list")){
                Intent i1 = new  Intent("com.geekpark.www.notificationlistenerservicetest.NOTIFICATION_LISTENER_EXAMPLE");
                i1.putExtra("notification_event","=====================" + "\n\n");
                sendBroadcast(i1);
                int i=1;
                for (StatusBarNotification sbn : NLService.this.getActiveNotifications()) {
                    Intent i2 = new  Intent("com.geekpark.www.notificationlistenerservicetest.NOTIFICATION_LISTENER_EXAMPLE");
                    i2.putExtra("notification_event",i +" " + sbn.getPackageName() + "\n");
                    sendBroadcast(i2);
                    i++;
                }
                Intent i3 = new  Intent("com.geekpark.www.notificationlistenerservicetest.NOTIFICATION_LISTENER_EXAMPLE");
                i3.putExtra("notification_event","======= 消息列表 =======" + "\n");
                sendBroadcast(i3);

            }

        }
    }

}