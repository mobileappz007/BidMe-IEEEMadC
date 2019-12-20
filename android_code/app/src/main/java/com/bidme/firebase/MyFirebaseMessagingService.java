package com.bidme.firebase;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.bidme.R;
import com.bidme.activity.SupportAcitivity;
import com.bidme.activity.dashbord.Dashboard;
import com.bidme.interfaces.Const;
import com.bidme.utils.ProjectUtils;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    String refreshedToken;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.e("Aashu", "From: " + remoteMessage.getData());
        if (remoteMessage.getData() != null) {
            if (remoteMessage.getData().size() > 0) {
                Log.e(TAG, "Message data payload: " + remoteMessage.getData());

                if (remoteMessage.getData().get("type").equalsIgnoreCase(Const.CHAT_TYPE)) {
                    Intent broadcastIntent = new Intent();
                    broadcastIntent.setAction(Const.BROADCAST);
                    broadcastIntent.setAction(Const.ALL_CHAT);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
                    sendNotification(getValue(remoteMessage.getData(), "title"),
                            getValue(remoteMessage.getData(), "body"),
                            ProjectUtils.indexOfNotification( getValue(remoteMessage.getData(), "type")),getValue(remoteMessage.getData(),Const.Pro_pub_id));
                }else if(remoteMessage.getData().get("type").equalsIgnoreCase(Const.BID_FAV_MESSAGE)){
                    sendNotification(getValue(remoteMessage.getData(), "title"),
                            getValue(remoteMessage.getData(), "body"),
                            ProjectUtils.indexOfNotification( getValue(remoteMessage.getData(), "type")),getValue(remoteMessage.getData(),Const.Pro_pub_id));
                }else if(remoteMessage.getData().get("type").equalsIgnoreCase(Const.RESOLVED_ISSUE)){
                    sendSupportNotification(getValue(remoteMessage.getData(), "title"),
                            getValue(remoteMessage.getData(), "body"));
                } else {
                    sendNotification(getValue(remoteMessage.getData(), "title"),
                            getValue(remoteMessage.getData(), "body"),
                            ProjectUtils.indexOfNotification( getValue(remoteMessage.getData(), "type")),getValue(remoteMessage.getData(),Const.Pro_pub_id));
                }
            }

        }



    }

    public String getValue(Map<String, String> data, String key) {
        try {
            if (data.containsKey(key))
                return data.get(key);
            else
                return getString(R.string.app_name);
        } catch (Exception ex) {
            ex.printStackTrace();
            return getString(R.string.app_name);
        }
    }


    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(Const.DEVICE_TOKEN, token);
        editor.commit();


        sendRegistrationToServer(token);
        SharedPreferences userDetails = MyFirebaseMessagingService.this.getSharedPreferences("MyPrefs", MODE_PRIVATE);
        Log.e(TAG, "my token: " + userDetails.getString(Const.DEVICE_TOKEN, ""));

    }

    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
    }

    private void sendNotification(String title,String message,int index,String pro_pub_id) {
        Log.e("1", "From: " +message);

        Intent intent = new Intent(this, Dashboard.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("index",index);
        intent.putExtra(Const.Pro_pub_id,pro_pub_id);


        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = "fcm_default_channel";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
    private void sendSupportNotification(String title,String message) {
        Log.e("1", "From: " +message);

        Intent intent = new Intent(this, SupportAcitivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = "fcm_default_channel";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }


    /*

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
*/

}