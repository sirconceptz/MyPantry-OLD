/******************************************************************************
 * Copyright (c) 2019.                                                        *
 * Mateusz Hermanowicz                                                        *
 * My Pantry                                                                  *
 * https://www.mypantry.eu                                                    *
 * Released under Apache License Version 2.0                                  *
 ******************************************************************************/

package com.hermanowicz.pantry;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

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

        Button myPantryButton    = findViewById(R.id.MyPantryButton);
        Button scanProductButton = findViewById(R.id.ScanProductButton);
        Button newProductButton  = findViewById(R.id.NewProductButton);
        Button appSettingsButton = findViewById(R.id.AppSettingsButton);
        Button closeAppButton    = findViewById(R.id.CloseAppButton);

        myPantryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myPantryActivityIntent = new Intent(MainActivity.this, MyPantryActivity.class);
                startActivity(myPantryActivityIntent);
                finish();
            }
        });

        scanProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent scanProductActivityIntent = new Intent(MainActivity.this, ScanProductActivity.class);
                startActivity(scanProductActivityIntent);
                finish();
            }
        });

        newProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newProductActivityIntent = new Intent(MainActivity.this, NewProductActivity.class);
                startActivity(newProductActivityIntent);
                finish();
            }
        });

        appSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent appSettingsActivityIntent = new Intent(MainActivity.this, AppSettingsActivity.class);
                startActivity(appSettingsActivityIntent);
                finish();
            }
        });

        closeAppButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }
        });
    }
}