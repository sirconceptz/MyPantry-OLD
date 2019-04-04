/******************************************************************************
 * Copyright (c) 2019.                                                        *
 * Mateusz Hermanowicz                                                        *
 * My Pantry                                                                  *
 * https://www.mypantry.eu                                                    *
 * Released under Apache License Version 2.0                                  *
 ******************************************************************************/

package com.hermanowicz.pantry.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.hermanowicz.pantry.services.NotificationService;

import androidx.annotation.NonNull;

/**
 * <h1>NotificationBroadcastReceiver</h1>
 * Broadcast receiver for notifications. Sending details needed to prepare notification.
 *
 * @author  Mateusz Hermanowicz
 * @version 1.0
 * @since   1.0
 */
public class NotificationBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(@NonNull Context context, @NonNull Intent intent) {
        Intent intentNotificationService = new Intent(context, NotificationService.class);
        intentNotificationService.putExtra("PRODUCT_NAME",
                intent.getStringExtra("PRODUCT_NAME"));
        intentNotificationService.putExtra("PRODUCT_ID",
                intent.getStringExtra("PRODUCT_ID"));
        context.startService(intentNotificationService);
    }
}