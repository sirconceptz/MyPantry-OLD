/*
 * Copyright (c) 2019
 * Mateusz Hermanowicz - All rights reserved.
 * My Pantry
 * https://www.mypantry.eu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hermanowicz.pantry.services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.hermanowicz.pantry.ApplicationController;
import com.hermanowicz.pantry.MyPantryActivity;
import com.hermanowicz.pantry.R;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * <h1>NotificationService</h1>
 * A service to handling the notifications in application. User will be informed about
 * expiration dates of products. Notification can be showed by push or email.
 *
 * @author  Mateusz Hermanowicz
 * @version 1.0
 * @since   1.0
 */
public class NotificationService extends IntentService {

    private static final String PREFERENCES_EMAIL_ADDRESS = "EMAIL_ADDRESS";
    private static final String PREFERENCES_EMAIL_NOTIFICATIONS = "EMAIL_NOTIFICATIONS?";
    private static final String PREFERENCES_PUSH_NOTIFICATIONS = "PUSH_NOTIFICATIONS?";
    private static final String PREFERENCES_DAYS_TO_NOTIFICATIONS = "HOW_MANY_DAYS_BEFORE_EXPIRATION_DATE_SEND_A_NOTIFICATION?";
    static final String DAYS_TAG = "%DAYS%";
    static final String PRODUCT_NAME_TAG = "%PRODUCT_NAME%";
    static final String URL_API = "https://www.mypantry.eu/api/";
    static final String API_MAIL_FILE    = "mail.php";

    private JsonObjectRequest request_json;
    private String productName;
    private int daysToNotification;

    public NotificationService(){
        super("NotificationService");
    }

    private String createStatement(){
        String statement = getApplicationContext().getResources().getString(R.string.Notifications_statement);
        statement = statement.replace(DAYS_TAG, String.valueOf(daysToNotification));
        statement = statement.replace(PRODUCT_NAME_TAG, productName);
        return statement;
    }

    @Override
    protected void onHandleIntent(@NonNull Intent intent) {
        Context context                 = getApplicationContext();
        SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        productName                     = intent.getStringExtra("PRODUCT_NAME");
        int               productID     = intent.getIntExtra("PRODUCT_ID", 0);
        daysToNotification = 3;
        daysToNotification = myPreferences.getInt(
                PREFERENCES_DAYS_TO_NOTIFICATIONS, com.hermanowicz.pantry.Notification.NOTIFICATION_DEFAULT_DAYS);

        if (myPreferences.getBoolean(PREFERENCES_PUSH_NOTIFICATIONS,
                true)) {
            String channelId = "my_channel_" + productID;
            NotificationManager notificationManager = (NotificationManager)
                    context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                CharSequence name  = "my_channel";
                String Description = "Products expiration dates notification channel";
                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                NotificationChannel mChannel = new NotificationChannel(channelId, name, importance);
                mChannel.setDescription(Description);
                mChannel.enableLights(true);
                mChannel.setLightColor(Color.RED);
                mChannel.enableVibration(true);
                mChannel.setShowBadge(false);
                assert notificationManager != null;
                notificationManager.createNotificationChannel(mChannel);
            }
            String notificationStatement = createStatement();

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId);
            builder.setContentTitle(getApplicationContext().getResources().getString(R.string.Notifications_title));
            builder.setContentText(notificationStatement);
            builder.setStyle(new NotificationCompat.BigTextStyle().bigText(notificationStatement));
            builder.setSmallIcon(R.mipmap.ic_launcher_round);
            builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                    R.mipmap.ic_launcher_round));
            builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
            builder.setLights(getResources().getColor(R.color.color_primary), 500, 1000);
            builder.setAutoCancel(true);
            Intent notifyIntent = new Intent(context, MyPantryActivity.class);
            notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(
                    context, productID, notifyIntent, 0);
            builder.setContentIntent(pendingIntent);
            Notification notificationCompat = builder.build();
            assert notificationManager != null;
            notificationManager.notify(productID, notificationCompat);
        }
        if (myPreferences.getBoolean(PREFERENCES_EMAIL_NOTIFICATIONS,
                true)) {
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("to_email_address", myPreferences.getString(PREFERENCES_EMAIL_ADDRESS, ""));
            params.put("subject", context.getResources().getString(R.string.Notifications_title));
            params.put("message", createStatement());
            String url = URL_API + API_MAIL_FILE;

            request_json = new JsonObjectRequest(url, new JSONObject(params),
                    response -> {
                    }, error -> VolleyLog.e(getResources().getString(R.string.Errors_error), error.getMessage()));
            ApplicationController.getInstance().addToRequestQueue(request_json);
        }
    }
}