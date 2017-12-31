package com.example.bitjedi.quiph_assignment;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;
import java.util.regex.Pattern;

public class MyAccessibiltyService extends AccessibilityService {

    private  static final int Notification_ID = 88080;

    @Override
    public void onCreate(){
        super.onCreate();
        Log.d("MyAccessibilityservice","onCreate");
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        final int eventType = accessibilityEvent.getEventType();
        Log.d("Browser", "textChanged"+accessibilityEvent.getBeforeText()+"->"+accessibilityEvent.getText());
        String eventText = null;

        String matchRegex = ".*"+"quiph.com"+".*";
            if (!accessibilityEvent.getText().isEmpty()) {

                String current_text = accessibilityEvent.getText().get(0).toString();
                String before_text = accessibilityEvent.getBeforeText().toString();


                boolean beforeQuiphOpen = Pattern.compile(matchRegex).matcher(before_text).matches();
                boolean currentQuiphOpen = Pattern.compile(matchRegex).matcher(current_text).matches();

                if (!beforeQuiphOpen && currentQuiphOpen) {
                    //showNotification//
                    Log.i("show Notification", "true");
                    removeNotification();//if previous events is there
                    showNotification();
                } else if (beforeQuiphOpen && !currentQuiphOpen) {
                    //removeNotification
                    Log.i("show Notification", "false");
                    removeNotification();
                }

            }else if(accessibilityEvent.getBeforeText() == null){
                //removeNotification
                Log.i("show Notification", "false");
               removeNotification();
            }


    }

    @Override
    public void onServiceConnected() {
        super.onServiceConnected();
        Log.i("accessibilityService","Connected");
        AccessibilityServiceInfo serviceInfo = new AccessibilityServiceInfo();

        serviceInfo.eventTypes = AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED;
        serviceInfo.packageNames = new String[]{"com.android.chrome"/** others browser package name..*/};
        serviceInfo.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
        serviceInfo.flags = AccessibilityServiceInfo.DEFAULT|AccessibilityServiceInfo.FLAG_INCLUDE_NOT_IMPORTANT_VIEWS|AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS | AccessibilityServiceInfo.FLAG_REQUEST_ENHANCED_WEB_ACCESSIBILITY |AccessibilityServiceInfo.FLAG_RETRIEVE_INTERACTIVE_WINDOWS;
        serviceInfo.notificationTimeout = 100;
        this.setServiceInfo(serviceInfo);
    }

    @Override
    public void onInterrupt() {
        Log.w("AccessService","Interrupted");
    }

    @Override
    public void onDestroy() {
        Log.w("AccessService","Destroyed");
    }

    public void showNotification(){
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.download)
                        .setContentTitle("Quiph_Notification")
                        .setAutoCancel(true)
                        .setContentText("Open Quiph Application...");

        final PackageManager pm = getPackageManager();
        Intent notificationIntent = null;
        final String quiph_Package_name = getPackageName(); // please change package name here as I don't have the package name of quiph so I put this temp package name;

        try{
            pm.getPackageInfo(quiph_Package_name,0);
            notificationIntent = new Intent(getApplication(),MainActivity.class);
        }catch (PackageManager.NameNotFoundException nme){
            try {
              notificationIntent =  new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + "in.co.nerdoo.com.chittichat.chittichat"));
            } catch (android.content.ActivityNotFoundException anfe) {
                notificationIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + "in.co.nerdoo.com.chittichat.chittichat"));
            }

        }

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);

        stackBuilder.addNextIntent(notificationIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(getApplicationContext().NOTIFICATION_SERVICE);

        mNotificationManager.notify(Notification_ID, mBuilder.build());


    }
    public void removeNotification(){
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(Notification_ID);
    }
}
