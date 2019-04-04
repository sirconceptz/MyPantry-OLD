/******************************************************************************
 * Copyright (c) 2019.                                                        *
 * Mateusz Hermanowicz                                                        *
 * My Pantry                                                                  *
 * https://www.mypantry.eu                                                    *
 * Released under Apache License Version 2.0                                  *
 ******************************************************************************/

package com.hermanowicz.pantry;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static com.hermanowicz.pantry.MyPantryActivity.convertDate;

public class ProductsAdapter extends
        RecyclerView.Adapter<ProductsAdapter.ViewHolder> {

    private       List<Product>       productList;
    private final OnItemClickListener listener;

    public ProductsAdapter(List<Product> productList, OnItemClickListener listener) {
        this.productList = productList;
        this.listener    = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(Product product);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView      nameTv, volumeTv, weightTv, expirationDateTv;

        public ViewHolder(View itemView) {
            super(itemView);

            nameTv           = itemView.findViewById(R.id.text_productName);
            volumeTv         = itemView.findViewById(R.id.text_productVolume);
            weightTv         = itemView.findViewById(R.id.text_productWeight);
            expirationDateTv = itemView.findViewById(R.id.text_expirationDate);
        }

        public void bind(final Product product, final OnItemClickListener listener){

        }
    }

    @NonNull
    @Override
    public ProductsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View productView = inflater.inflate(R.layout.recycler_view_products, parent, false);
        ViewHolder holder = new ViewHolder(productView);

        return holder;
    }

    @Override
    public void onBindViewHolder(ProductsAdapter.ViewHolder viewHolder, int position) {

        TextView nameTv = viewHolder.nameTv;
        TextView volumeTv = viewHolder.volumeTv;
        TextView weightTv = viewHolder.weightTv;
        TextView expirationDateTv = viewHolder.expirationDateTv;

        Context context = nameTv.getContext();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Resources resources = context.getResources();
        final Product selectedProduct = productList.get(position);
        Calendar calendar = Calendar.getInstance();
        Date expirationDateDt = calendar.getTime();
        String volumeString = resources.getString(R.string.ProductDetailsActivity_volume) + ": " +  selectedProduct.getVolume() + resources.getString(R.string.ProductDetailsActivity_volume_unit);
        String weightString = resources.getString(R.string.ProductDetailsActivity_weight) + ": " +  selectedProduct.getWeight() + resources.getString(R.string.ProductDetailsActivity_weight_unit);
        String expirationDateString = convertDate(selectedProduct.getExpirationDate());

        nameTv.setText(selectedProduct.getName());
        volumeTv.setText(volumeString);
        weightTv.setText(weightString);
        expirationDateTv.setText(expirationDateString);

        try {
            expirationDateDt = simpleDateFormat.parse(selectedProduct.getExpirationDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.add(Calendar.DAY_OF_MONTH, AppSettingsActivity.getDaysBeforeNotificationFromSettings(nameTv.getContext()));
        Date dayOfNotification = calendar.getTime();
        if (dayOfNotification.after(expirationDateDt))
            viewHolder.itemView.setBackgroundColor(resources.getColor(R.color.background_expired_products));

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                listener.onItemClick(selectedProduct);
            }
        });

        Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
        viewHolder.itemView.setAnimation(animation);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}