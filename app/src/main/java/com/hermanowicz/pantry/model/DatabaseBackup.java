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

package com.hermanowicz.pantry.model;

import android.content.Context;

import com.ebner.roomdatabasebackup.core.RoomBackup;
import com.hermanowicz.pantry.R;
import com.hermanowicz.pantry.db.category.CategoryDb;
import com.hermanowicz.pantry.db.product.ProductDb;
import com.hermanowicz.pantry.db.storagelocation.StorageLocationDb;
import com.hermanowicz.pantry.util.DateHelper;

public class DatabaseBackup {

    public static void backupProductDb(Context context) {
        final RoomBackup roomBackup = new RoomBackup();
        String currentTime = DateHelper.getTimeStamp();
        roomBackup.context(context);
        roomBackup.database(ProductDb.getInstance(context));
        roomBackup.customBackupFileName("ProductDbBackup:" + currentTime);
        roomBackup.customEncryptPassword(context.getString(R.string.database_secretcode));
        roomBackup.enableLogDebug(true);
        roomBackup.backupIsEncrypted(true);
        roomBackup.useExternalStorage(true);
        roomBackup.maxFileCount(15);
        roomBackup.backup();
    }

    public static void backupCategoryDb(Context context) {
        final RoomBackup roomBackup = new RoomBackup();
        String currentTime = DateHelper.getTimeStamp();
        roomBackup.context(context);
        roomBackup.database(CategoryDb.getInstance(context));
        roomBackup.customBackupFileName("CategoryDbBackup:" + currentTime);
        roomBackup.customEncryptPassword(context.getString(R.string.database_secretcode));
        roomBackup.enableLogDebug(true);
        roomBackup.backupIsEncrypted(true);
        roomBackup.useExternalStorage(true);
        roomBackup.maxFileCount(15);
        roomBackup.backup();
    }

    public static void backupStorageLocationDb(Context context) {
        final RoomBackup roomBackup = new RoomBackup();
        String currentTime = DateHelper.getTimeStamp();
        roomBackup.context(context);
        roomBackup.database(StorageLocationDb.getInstance(context));
        roomBackup.customBackupFileName("StorageLocationDbBackup:" + currentTime);
        roomBackup.customEncryptPassword(context.getString(R.string.database_secretcode));
        roomBackup.enableLogDebug(true);
        roomBackup.backupIsEncrypted(true);
        roomBackup.useExternalStorage(true);
        roomBackup.maxFileCount(15);
        roomBackup.backup();
    }

    public static void restoreProductDb(Context context){
        final RoomBackup roomBackup = new RoomBackup();
        roomBackup.context(context);
        roomBackup.database(ProductDb.getInstance(context));
        roomBackup.customEncryptPassword(context.getString(R.string.database_secretcode));
        roomBackup.customRestoreDialogTitle(context.getString(R.string.AppSettingsActivity_choose_file_to_restore));
        roomBackup.enableLogDebug(true);
        roomBackup.backupIsEncrypted(true);
        roomBackup.useExternalStorage(true);
        roomBackup.restore();
    }

    public static void restoreCategoryDb(Context context){
        final RoomBackup roomBackup = new RoomBackup();
        roomBackup.context(context);
        roomBackup.database(CategoryDb.getInstance(context));
        roomBackup.customEncryptPassword(context.getString(R.string.database_secretcode));
        roomBackup.customRestoreDialogTitle(context.getString(R.string.AppSettingsActivity_choose_file_to_restore));
        roomBackup.enableLogDebug(true);
        roomBackup.backupIsEncrypted(true);
        roomBackup.useExternalStorage(true);
        roomBackup.restore();
    }

    public static void restoreStorageLocationDb(Context context){
        final RoomBackup roomBackup = new RoomBackup();
        roomBackup.context(context);
        roomBackup.database(StorageLocationDb.getInstance(context));
        roomBackup.customEncryptPassword(context.getString(R.string.database_secretcode));
        roomBackup.customRestoreDialogTitle(context.getString(R.string.AppSettingsActivity_choose_file_to_restore));
        roomBackup.enableLogDebug(true);
        roomBackup.backupIsEncrypted(true);
        roomBackup.useExternalStorage(true);
        roomBackup.restore();
    }

    public static void clearProductDb (Context context){
        ProductDb.getInstance(context).productsDao().clearDb();
    }

    public static void clearCategoryDb (Context context){
        CategoryDb.getInstance(context).categoryDao().clearDb();
    }

    public static void clearStorageLocationDb (Context context){
        StorageLocationDb.getInstance(context).storageLocationDao().clearDb();
    }
}