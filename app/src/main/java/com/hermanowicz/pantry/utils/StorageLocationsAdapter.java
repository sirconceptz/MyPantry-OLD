package com.hermanowicz.pantry.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hermanowicz.pantry.R;
import com.hermanowicz.pantry.databinding.RvSingleStorageLocationBinding;
import com.hermanowicz.pantry.db.StorageLocation;

import java.util.List;

public class StorageLocationsAdapter extends RecyclerView.Adapter<StorageLocationsAdapter.ViewHolder> {

    private RvSingleStorageLocationBinding binding;
    private List<StorageLocation> storageLocationList;
    private int itemAnimPosition;

    public void setData(List<StorageLocation> storageLocations){
        this.storageLocationList = storageLocations;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public StorageLocationsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        binding = RvSingleStorageLocationBinding.inflate(layoutInflater, parent, false);

        return new ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        TextView name = binding.nameValue;
        TextView description = binding.descriptionValue;
        name.setText(storageLocationList.get(position).getName());
        description.setText(storageLocationList.get(position).getDescription());

        Context context = name.getContext();

        if(viewHolder.getAdapterPosition() > itemAnimPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_in);
            viewHolder.itemView.startAnimation(animation);
            itemAnimPosition = viewHolder.getAdapterPosition();
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ViewHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public int getItemCount() {
        return storageLocationList.size();
    }
}