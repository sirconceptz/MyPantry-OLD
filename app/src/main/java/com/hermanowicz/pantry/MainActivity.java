/******************************************************************************
 * Copyright (c) 2019.                                                        *
 * Mateusz Hermanowicz                                                        *
 * My Pantry                                                                  *
 * https://www.mypantry.eu                                                    *
 * Released under Apache License Version 2.0                                  *
 ******************************************************************************/

package com.hermanowicz.pantry;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


/**
 * <h1>MainActivity</h1>
 * Main activity - main window of the application. First window after launching this application.
 * In main activity is only main menu with buttons to the other activities.
 *
 * @author  Mateusz Hermanowicz
 * @version 1.0
 * @since   1.0
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
}