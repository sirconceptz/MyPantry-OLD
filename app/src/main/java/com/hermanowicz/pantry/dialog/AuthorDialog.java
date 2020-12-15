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

import com.hermanowicz.pantry.R;
import com.hermanowicz.pantry.databinding.DialogAuthorBinding;

import org.jetbrains.annotations.NotNull;

public class AuthorDialog extends AppCompatDialogFragment {

    private DialogAuthorBinding binding;
    private Activity activity;
    private View view;
    private ImageView linkedInProfile, facebookProfile;

    public static AuthorDialog newInstance(int title) {
        AuthorDialog frag = new AuthorDialog();
        Bundle args = new Bundle();
        args.putInt("title", title);
        frag.setArguments(args);
        return frag;
    }

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
        binding = DialogAuthorBinding.inflate(activity.getLayoutInflater());
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