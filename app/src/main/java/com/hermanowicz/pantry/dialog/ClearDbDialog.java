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

package com.hermanowicz.pantry.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.hermanowicz.pantry.R;
import com.hermanowicz.pantry.interfaces.IAppSettingsDialogListener;

public class ClearDbDialog extends AppCompatDialogFragment {

    private IAppSettingsDialogListener dialogListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Activity activity = getActivity();
        assert activity != null;
        Context context = activity.getApplicationContext();
        Resources resources = context.getResources();

        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.AppThemeDialog);

        LayoutInflater layoutInflater = activity.getLayoutInflater();

        View view = layoutInflater.inflate(R.layout.dialog_clear_database, null);

        builder.setView(view)
                .setTitle(resources.getString(R.string.AppSettingsActivity_clear_database))
                .setNegativeButton(resources.getString(R.string.MyPantryActivity_cancel), (dialog, which) -> {
                })
                .setPositiveButton(resources.getString(R.string.AppSettingsActivity_clear_database), (dialog, which) -> {
                    dialogListener.clearDatabaseConfirmation();
                });
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            dialogListener = (IAppSettingsDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString());
        }
    }
}