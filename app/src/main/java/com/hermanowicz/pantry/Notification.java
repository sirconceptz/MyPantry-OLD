/******************************************************************************
 * Copyright (c) 2019.                                                        *
 * Mateusz Hermanowicz                                                        *
 * My Pantry                                                                  *
 * https://www.mypantry.eu                                                    *
 * Released under Apache License Version 2.0                                  *
 ******************************************************************************/

package com.hermanowicz.pantry;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;

import java.util.Calendar;
import java.util.List;

/**
 * <h1>Notification</h1>
 * Class to support the notification in the application.
 *
 * @author  Mateusz Hermanowicz
 * @version 1.0
 * @since   1.0
 */
class Notification {

    private static Calendar createCalendar(@NonNull Context context, @NonNull String expirationDate){
        String[] dateArray = expirationDate.split("-");
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.YEAR, Integer.valueOf(dateArray[0]));
        calendar.set(Calendar.MONTH, (Integer.valueOf(dateArray[1]))-1);
        calendar.set(Calendar.DAY_OF_MONTH, Integer.valueOf(dateArray[2]));
        calendar.set(Calendar.HOUR_OF_DAY, AppSettingsActivity.getHourOfNotifications(context));
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.add(Calendar.DAY_OF_MONTH, -(AppSettingsActivity.getDaysBeforeNotificationFromSettings(context)));

        return calendar;
    }

    static void createNotification(@NonNull Context context, @NonNull Product product){
        Intent intent = new Intent(context, NotificationBroadcastReceiver.class);
        intent.putExtra("PRODUCT_NAME", product.getName());
        intent.putExtra("PRODUCT_ID", product.getID());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, product.getID(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager)(context.getSystemService(Context.ALARM_SERVICE));

        Calendar calendar = createCalendar(context, product.getExpirationDate());

        if (Build.VERSION.SDK_INT >= 23) {
            assert alarmManager != null;
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(), pendingIntent);
        } else if (Build.VERSION.SDK_INT >= 19) {
            assert alarmManager != null;
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        } else {
            assert alarmManager != null;
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
    }

    static void cancelNotification(@NonNull Context context, @NonNull Product product){
        AlarmManager alarmManager = (AlarmManager)(context.getSystemService(Context.ALARM_SERVICE));
        Intent intent = new Intent(context, NotificationBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, product.getID(), intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        pendingIntent.cancel();
        alarmManager.cancel(pendingIntent);
    }

    static void cancelAllNotifications(@NonNull Context context, @NonNull List<Product> productsList){
        AlarmManager alarmManager = (AlarmManager)(context.getSystemService(Context.ALARM_SERVICE));
        Intent intent = new Intent(context, NotificationBroadcastReceiver.class);
        for(int counter = 0; counter < productsList.size(); counter++){
            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    context, productsList.get(counter).getID(), intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            pendingIntent.cancel();
            alarmManager.cancel(pendingIntent);
        }
    }
}