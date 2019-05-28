/******************************************************************************
 * Copyright (c) 2019.                                                        *
 * Mateusz Hermanowicz                                                        *
 * My Pantry                                                                  *
 * https://www.mypantry.eu                                                    *
 * Released under Apache License Version 2.0                                  *
 ******************************************************************************/

package com.hermanowicz.pantry.interfaces;

import androidx.appcompat.app.AppCompatActivity;

public interface IPermissionHandler {
    boolean checkHasPermission(AppCompatActivity activity, String permission);

    void requestPermission(AppCompatActivity activity, String[] permissions, int requestCode);
}
