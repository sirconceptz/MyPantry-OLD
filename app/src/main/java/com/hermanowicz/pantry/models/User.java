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

package com.hermanowicz.pantry.models;

import android.content.SharedPreferences;

public class User {

    private String emailAddress;
    private String password;

    private SharedPreferences preferences;

    public User(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAdress) {
        this.emailAddress = emailAdress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean comparePasswords(String password, String passwordConfirmation){
        return password.equals(passwordConfirmation);
    }

    public boolean userIsLogged(){
        return preferences.getBoolean(
                "USER_IS_LOGGED", false);
    }

    public boolean offlineDbMode(){
        return preferences.getBoolean(
                "OFFLINE_DB_MODE", false);
    }
}