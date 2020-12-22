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
import com.hermanowicz.pantry.databinding.RvSingleCategoryBinding;
import com.hermanowicz.pantry.db.Category;

import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> {

    private RvSingleCategoryBinding binding;

    private List<Category> categoryList;
    private int itemAnimPosition;

    public void setData(List<Category> categoryList){
        this.categoryList = categoryList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoriesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        binding = RvSingleCategoryBinding.inflate(layoutInflater, parent, false);

        return new ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        TextView name = binding.nameValue;
        TextView description = binding.descriptionValue;
        name.setText(categoryList.get(position).getName());
        description.setText(categoryList.get(position).getDescription());

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
        return categoryList.size();
    }
}