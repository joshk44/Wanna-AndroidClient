package com.wanna.gcm;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.wanna.client.R;
import com.wanna.client.ui.CommentActivity;
import com.wanna.client.ui.DetailsActivity;
import com.wanna.client.ui.SplashActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;


/**
 * Created by fede on 04/05/15.
 */
public class GCMNotificationIntentService extends IntentService {
    // Sets an ID for the notification, so it can be updated
    public static final int notifyID = 9001;
    NotificationCompat.Builder builder;
    private String name,idActivity;
    private String activity;
    private String actionType="";
    Intent resultIntent;


    public GCMNotificationIntentService() {
        super("GcmIntentService");
    }

    public static final String TAG = "GCMNotificationIntentService";

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR
                .equals(messageType)) {
                sendNotification("Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED
                .equals(messageType)) {
                sendNotification("Deleted messages on server: "
                                 + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE
                .equals(messageType)) {

                sendNotification("" + extras.get(
                    ApplicationConstants.MSG_KEY)); //When Message is received normally from GCM Cloud Server
            }
        }
        GcmBroadcastReceive.completeWakefulIntent(intent);

    }

    private void sendNotification(String wannaMsg) {

        try {
            actionType = DevuelParse(wannaMsg, "action_type");
            name = DevuelParse(wannaMsg, "user_name");
            activity = DevuelParse(wannaMsg, "activity_name");
            if(actionType.equals("COMMENT")){
                idActivity=DevuelParse(wannaMsg, "id_activity");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        resultIntent = new Intent(this, DetailsActivity.class);
        resultIntent.putExtra("wannaMsg", wannaMsg);
        resultIntent.setAction(Intent.ACTION_MAIN);
        resultIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);


        if(actionType.equals("COMMENT")) {
            resultIntent = new Intent(this, CommentActivity.class);
            resultIntent.putExtra("idactivity", idActivity);
            resultIntent.putExtra("nameActivity", activity);
            resultIntent.setAction(Intent.ACTION_MAIN);
            resultIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        }else{
            resultIntent = new Intent(this, SplashActivity.class);
            resultIntent.setAction(Intent.ACTION_MAIN);
            resultIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        }

        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0,
                resultIntent,
                PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder mNotifyBuilder;
        NotificationManager mNotificationManager;

        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        String notificationText = "";
        switch (actionType) {
            case "LIKE": {
            notificationText = "Likes your activity " + activity;
            break;
            }
            case "JOINED": {
            notificationText = "Joins to your activity " + activity;
            break;
            }
            case "INVITE": {
            notificationText = "Invites you to join " + activity;
            break;
            }
            case "COMMENT": {
            notificationText = "Commented activity " + activity;
            break;
            }
            case "CANCEL": {
                notificationText = "Cancel the activity" +"/t"+ activity;
                break;
            }

        }

        mNotifyBuilder = new NotificationCompat.Builder(this)
            // Set the content for Notification
            //Lo agrego para parsear datos el metodo DevuelParse, le paso el json lo convierto en jsonObjen y obtengo los datos
            .setContentTitle(name)
            .setContentText(notificationText)
            .setSmallIcon(R.drawable.ic_notifications_w)
            .setColor(getResources().getColor(R.color.primary));

        // Set pending intent
        if (actionType.equals("INVITE")) {
            mNotifyBuilder.setContentIntent(resultPendingIntent);
            SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(this.getApplicationContext());
            Set<String> notifications = preferences
                .getStringSet("notifications", new HashSet<String>());
            SharedPreferences.Editor editor = preferences.edit();
            Set<String> in = new HashSet<String>(notifications);
            in.add(wannaMsg);
            editor.putStringSet("notifications", in).commit();
        }else if (actionType.equals("COMMENT")) {
            mNotifyBuilder.setContentIntent(resultPendingIntent);
        }else{
            mNotifyBuilder.setContentIntent(resultPendingIntent);
        }

        // Set Vibrate, Sound and Light
        int defaults = 0;
        defaults = defaults | Notification.DEFAULT_LIGHTS;
        defaults = defaults | Notification.DEFAULT_VIBRATE;
        defaults = defaults | Notification.DEFAULT_SOUND;

        mNotifyBuilder.setDefaults(defaults);
        // Set the content for Notification

        // Set autocancel
        mNotifyBuilder.setAutoCancel(true);
        // Post a notification
        mNotificationManager
            .notify(new Random().nextInt(9999 - 1000) + 1000, mNotifyBuilder.build());
    }

    //Lo agrego para parsear datos el metodo DevuelParse, le paso el json lo convierto en jsonObjen y obtengo los datos
    public String DevuelParse(String TojsonObj, String buscar) throws JSONException {
        String text = "";

        JSONObject jsonObj = new JSONObject(TojsonObj);
        text = jsonObj.getString(buscar);

        return text;
    }
}
