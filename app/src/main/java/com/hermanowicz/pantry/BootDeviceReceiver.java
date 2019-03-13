/******************************************************************************
 * Copyright (c) 2019.                                                        *
 * Mateusz Hermanowicz                                                        *
 * My Pantry                                                                  *
 * https://www.mypantry.eu                                                    *
 * Released under Apache License Version 2.0                                  *
 ******************************************************************************/

package com.hermanowicz.pantry;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import java.util.List;

/**
 * <h1>BootDeviceReceiver</h1>
 * A receiver for boot device. This class is needed to renew a notifications on the device.
 * Notifications are set for all products from database with parameters from application
 * settings.
 *
 * @author  Mateusz Hermanowicz
 * @version 1.0
 * @since   1.0
 */
public class BootDeviceReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(@NonNull Context context, @NonNull Intent intent) {

        DatabaseManager db           = new DatabaseManager(context);
        List<Product> productsFromDB = db.getProductsFromDB("SELECT * FROM 'products' DESC");
        for(int counter=0; counter <= productsFromDB.size(); counter++){
            Product selectedProduct = productsFromDB.get(counter);
            Notification.createNotification(context, selectedProduct);
        }
    }
}