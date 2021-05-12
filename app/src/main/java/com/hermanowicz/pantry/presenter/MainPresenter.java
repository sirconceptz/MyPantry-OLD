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

package com.hermanowicz.pantry.presenter;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hermanowicz.pantry.interfaces.AccountView;
import com.hermanowicz.pantry.interfaces.MainView;

public class MainPresenter {

    private final MainView view;
    private final AccountView accountView;

    public MainPresenter(@NonNull MainView view, @NonNull AccountView accountView) {
        this.view = view;
        this.accountView = accountView;
    }

    public void navigateToMyPantryActivity() {
        view.onNavigationToMyPantryActivity();
    }

    public void navigateToScanProductActivity() {
        view.onNavigationToScanProductActivity();
    }

    public void navigateToNewProductActivity() {
        view.onNavigationToNewProductActivity();
    }

    public void navigateToCategoriesActivity() { view.onNavigationToCategoriesActivity(); }

    public void navigateToStorageLocationsActivity() { view.onNavigationToStorageLocationsActivity(); }

    public void navigateToAppSettingsActivity() {
        view.onNavigationToAppSettingsActivity();
    }

    public void showAuthorInfoDialog() {
        view.showAuthorInfoDialog();
    }

    public void updateUserData(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null)
            accountView.updateUserData("Niezalogowany");
        else
            accountView.updateUserData(user.getEmail());
    }

    public void signInOrSignOut() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null)
            accountView.signIn();
        else {
            accountView.signOut();
            updateUserData();
        }
    }
}