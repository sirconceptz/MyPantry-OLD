package com.hermanowicz.pantry.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.hermanowicz.pantry.R;
import com.hermanowicz.pantry.databinding.DialogNewStorageLocationBinding;
import com.hermanowicz.pantry.db.StorageLocation;
import com.hermanowicz.pantry.interfaces.DialogStorageLocationListener;
import com.hermanowicz.pantry.interfaces.NewStorageLocationView;
import com.hermanowicz.pantry.models.DatabaseOperations;
import com.hermanowicz.pantry.models.StorageLocationModel;
import com.hermanowicz.pantry.presenters.NewStorageLocationPresenter;

import org.jetbrains.annotations.NotNull;

public class NewStorageLocationDialog extends AppCompatDialogFragment implements NewStorageLocationView {

    private DialogNewStorageLocationBinding binding;
    private NewStorageLocationPresenter presenter;
    private Activity activity;
    private View view;
    private DialogStorageLocationListener dialogListener;
    private EditText storageLocationName, storageLocationDescription;
    private TextView nameCharCounter, descriptionCharCounter;

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        initView();
        initListeners();

        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.AppThemeDialog);
        builder.setView(view)
                .setTitle(getString(R.string.StorageLocationsActivity_new_storage_location))
                .setNegativeButton(getString(R.string.General_cancel), (dialog, which) -> {
                })
                .setPositiveButton(getString(R.string.General_save), (dialog, which) -> {
                    StorageLocation storageLocation = new StorageLocation();
                    storageLocation.setName(storageLocationName.getText().toString());
                    storageLocation.setDescription(storageLocationDescription.getText().toString());
                    presenter.onPressAddNewStorageLocation(storageLocation);
                });
        return builder.create();
    }

    private void initView() {
        activity = getActivity();
        binding = DialogNewStorageLocationBinding.inflate(activity.getLayoutInflater());

        storageLocationName = binding.nameValue;
        storageLocationDescription = binding.descriptionValue;
        nameCharCounter = binding.nameCharCounter;
        descriptionCharCounter = binding.descriptionCharCounter;

        DatabaseOperations databaseOperations = new DatabaseOperations(activity.getApplicationContext());
        presenter = new NewStorageLocationPresenter(this, new StorageLocationModel(databaseOperations));
        presenter.initCharCounters();

        view = binding.getRoot();
    }

    private void initListeners(){
        storageLocationName.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                presenter.isStorageLocationNameCorrect(storageLocationName.getText().toString());
            }
        });

        storageLocationDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                presenter.isStorageLocationDescriptionCorrect(storageLocationDescription.getText().toString());
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            dialogListener = (DialogStorageLocationListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString());
        }
    }

    @Override
    public void onAddStorageLocation(StorageLocation storageLocation) {
        dialogListener.onAddStorageLocation(storageLocation);
    }

    @Override
    public void updateNameCharCounter(int charCounter, int maxChar) {
        nameCharCounter.setText(String.format("%s: %d/%d", getText(R.string.General_char_counter).toString(), charCounter, maxChar));
    }

    @Override
    public void updateDescriptionCharCounter(int charCounter, int maxChar) {
        descriptionCharCounter.setText(String.format("%s: %d/%d", getText(R.string.General_char_counter).toString(), charCounter, maxChar));
    }

    @Override
    public void showNameFieldError() {
        storageLocationName.setError(getText(R.string.Error_char_counter));
    }

    @Override
    public void showDescriptionFieldError() {
        storageLocationDescription.setError(getText(R.string.Error_char_counter));
    }
}