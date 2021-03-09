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

package com.hermanowicz.pantry.util;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.hermanowicz.pantry.interfaces.PermissionHandler;

public class PermissionsHandler implements PermissionHandler {
    @Override
    public boolean checkHasPermission(@NonNull AppCompatActivity activity, @NonNull String permission){
        return ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void requestPermission(@NonNull AppCompatActivity activity, @NonNull String[] permissions, int requestCode){
        ActivityCompat.requestPermissions(activity, permissions, requestCode);
    }

    public static boolean isWritePermission(AppCompatActivity activity){
        PermissionsHandler permissionHandler = new PermissionsHandler();
        return permissionHandler.checkHasPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                || permissionHandler.checkHasPermission(activity, Manifest.permission.ACCESS_MEDIA_LOCATION);
    }

    public static void requestWritePermission(AppCompatActivity activity){
        PermissionsHandler permissionHandler = new PermissionsHandler();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            permissionHandler.requestPermission(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_MEDIA_LOCATION},
                    23);
        }
    }

    public static boolean isCameraPermission(AppCompatActivity activity){
        PermissionsHandler permissionHandler = new PermissionsHandler();
        return permissionHandler.checkHasPermission(activity, Manifest.permission.CAMERA);
    }

    public static void requestCameraPermission(AppCompatActivity activity){
        PermissionsHandler permissionHandler = new PermissionsHandler();
        permissionHandler.requestPermission(activity, new String[]{Manifest.permission.CAMERA},
                23);
    }
}