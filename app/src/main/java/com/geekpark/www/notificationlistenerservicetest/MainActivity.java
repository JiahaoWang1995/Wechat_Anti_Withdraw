package com.geekpark.www.notificationlistenerservicetest;

        import android.app.Activity;
        import android.app.NotificationManager;
        import android.content.BroadcastReceiver;
        import android.content.Context;
        import android.content.Intent;
        import android.content.IntentFilter;
        import android.os.Bundle;
        import android.support.v4.app.NotificationCompat;
        import android.view.View;
        import android.widget.TextView;
        import android.widget.Toast;

public class MainActivity extends Activity {

    private TextView txtView;
    private NotificationReceiver nReceiver;
    private long lastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtView = (TextView) findViewById(R.id.textView);
        nReceiver = new NotificationReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.geekpark.www.notificationlistenerservicetest.NOTIFICATION_LISTENER_EXAMPLE");
        registerReceiver(nReceiver,filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(nReceiver);
    }

    public void buttonClicked(View v){

        if(v.getId() == R.id.btnCreateNotify){
            NotificationManager nManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            NotificationCompat.Builder ncomp = new NotificationCompat.Builder(this);
            ncomp.setContentTitle("My Notification");
            ncomp.setContentText("Notification Listener Service Example");
            ncomp.setTicker("Notification Listener Service Example");
            ncomp.setSmallIcon(R.mipmap.ic_launcher);
            ncomp.setAutoCancel(true);
            nManager.notify((int)System.currentTimeMillis(),ncomp.build());
        }
        else if(v.getId() == R.id.btnClearNotify){
            Intent i = new Intent("com.geekpark.www.notificationlistenerservicetest.NOTIFICATION_LISTENER_SERVICE_EXAMPLE");
            i.putExtra("command","clearall");
            sendBroadcast(i);
        }
        else if(v.getId() == R.id.btnListNotify){
            Intent i = new Intent("com.geekpark.www.notificationlistenerservicetest.NOTIFICATION_LISTENER_SERVICE_EXAMPLE");
            i.putExtra("command","list");
            sendBroadcast(i);
        }

    }

    class NotificationReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String temp = intent.getStringExtra("notification_event") + txtView.getText();
            txtView.setText(temp);
        }
    }

    @Override
    public void onBackPressed() {
        if (lastClickTime == 0){
            Toast.makeText(this, "使用HOME键退出以保证监听，再按一次结束程序", Toast.LENGTH_SHORT).show();
            lastClickTime = System.currentTimeMillis();
        }
        else {
            if (System.currentTimeMillis() - lastClickTime <= 1000){
                Toast.makeText(this, "监听终止", Toast.LENGTH_SHORT).show();
                super.onBackPressed();
            }
            else {
                Toast.makeText(this, "使用HOME键退出以保证监听，再按一次结束程序", Toast.LENGTH_SHORT).show();
                lastClickTime = System.currentTimeMillis();
            }
        }
    }
}