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
import com.hermanowicz.pantry.db.category.Category;

import java.util.List;

/**
 * <h1>CategoriesAdapter</h1>
 * View's adapter for categories view
 *
 * @author  Mateusz Hermanowicz
 */

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
        TextView name = binding.edittextName;
        TextView description = binding.edittextDescription;
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