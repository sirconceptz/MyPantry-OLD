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

package com.hermanowicz.pantry.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.firebase.database.annotations.NotNull;
import com.hermanowicz.pantry.R;
import com.hermanowicz.pantry.databinding.DialogAuthorBinding;

/**
 * <h1>AuthorDialog</h1>
 * The dialog window showing information about the application's author.
 *
 * @author Mateusz Hermanowicz
 */

public class AuthorDialog extends AppCompatDialogFragment {

    private Activity activity;

    private View view;
    private ImageView linkedInProfile;
    private ImageView facebookProfile;

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        initView();
        setListeners();

        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.AppThemeDialog);

        builder.setView(view)
                .setTitle(getString(R.string.General_author_label))
                .setPositiveButton(getString(R.string.General_cancel), (dialog, which) -> {
                });

        return builder.create();
    }

    private void initView() {
        activity = getActivity();
        assert activity != null;
        com.hermanowicz.pantry.databinding.DialogAuthorBinding binding = DialogAuthorBinding.inflate(activity.getLayoutInflater());
        view = binding.getRoot();
        linkedInProfile = binding.linkedIn;
        facebookProfile = binding.facebook;
    }

    private void setListeners() {
        linkedInProfile.setOnClickListener(view1 -> {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.addCategory(Intent.CATEGORY_BROWSABLE);
            intent.setData(Uri.parse(getString(R.string.Author_linkedin_profile)));
            startActivity(intent);
        });

        facebookProfile.setOnClickListener(view1 -> {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.addCategory(Intent.CATEGORY_BROWSABLE);
            intent.setData(Uri.parse(getString(R.string.Author_facebook_profile)));
            startActivity(intent);
        });
    }
}