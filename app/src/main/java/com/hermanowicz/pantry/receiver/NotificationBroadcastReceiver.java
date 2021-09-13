/*
 * Copyright (c) 2019-2021
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

package com.hermanowicz.pantry.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.hermanowicz.pantry.service.NotificationService;

/**
 * <h1>NotificationBroadcastReceiver</h1>
 * Broadcast receiver for notifications. Sending details needed to prepare notification.
 *
 * @author  Mateusz Hermanowicz
 */

public class NotificationBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(@NonNull Context context, @NonNull Intent intent) {
        Intent intentNotificationService = new Intent(context, NotificationService.class);
        intentNotificationService.putExtra("product_name",
                intent.getStringExtra("product_name"));
        intentNotificationService.putExtra("product_id",
                intent.getStringExtra("product_id"));
        context.startService(intentNotificationService);
    }
}