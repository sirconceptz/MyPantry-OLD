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

package com.hermanowicz.pantry.interfaces;

public interface AppSettingsView {

    void recreateNotifications();

    void onProductDatabaseClear();

    void showSelectedTheme(int selectedTheme);

    void showSelectedScanCamera(int selectedScanCamera);

    void showEmailAddress(String emailAddress);

    void showDaysToNotification(int daysToNotificaion);

    void showActiveUser(String activeUser);

    void showDatatabaseMode(String databaseLocation);

    void showVersionCode(String version);

    void setEmailPreferences();

    void showDialogBackupProductDb();

    void showDialogRestoreProductDb();

    void showDialogClearProductDb();

    void showDialogBackupCategoryDb();

    void showDialogRestoreCategoryDb();

    void showDialogClearCategoryDb();

    void showDialogBackupStorageLocationDb();

    void showDialogRestoreStorageLocationDb();

    void showDialogClearStorageLocationDb();

    void showDbBackupHasBeenMade();

    void showDbHasBeenRestored();

    void showDbHasBeenClear();

    void refreshActivity();

    void showInfoUserIsPremium();

    void enableDatabaseModeSelection(Boolean isEnabled);

    void buyPremiumFeatures();

    void showDialogImportDatabase();

    void enableImportDatabaseSelection(boolean isEnabled);

    void showBillingClientNotReady();
}