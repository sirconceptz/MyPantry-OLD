/******************************************************************************
 * Copyright (c) 2019.                                                        *
 * Mateusz Hermanowicz                                                        *
 * My Pantry                                                                  *
 * https://www.mypantry.eu                                                    *
 * Released under Apache License Version 2.0                                  *
 ******************************************************************************/

package com.hermanowicz.pantry;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hermanowicz.pantry.models.ProductEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductsAdapter extends
        RecyclerView.Adapter<ProductsAdapter.ViewHolder> {

    private static final String PREFERENCES_DAYS_TO_NOTIFICATIONS = "HOW_MANY_DAYS_BEFORE_EXPIRATION_DATE_SEND_A_NOTIFICATION?";

    private List<ProductEntity> productList;
    private SharedPreferences myPreferences;
    private final OnItemClickListener listener;

    ProductsAdapter(List<ProductEntity> productList, OnItemClickListener listener, SharedPreferences myPreferences) {
        this.productList = productList;
        this.listener = listener;
        this.myPreferences = myPreferences;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ProductsAdapter.ViewHolder viewHolder, int position) {

        TextView nameTv = viewHolder.nameTv;
        TextView volumeTv = viewHolder.volumeTv;
        TextView weightTv = viewHolder.weightTv;
        TextView expirationDateTv = viewHolder.expirationDateTv;

        Context context = nameTv.getContext();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Resources resources = context.getResources();
        final ProductEntity selectedProduct = productList.get(position);
        Calendar calendar = Calendar.getInstance();
        Date expirationDateDt = calendar.getTime();
        String volumeString = resources.getString(R.string.ProductDetailsActivity_volume) + ": " +  selectedProduct.getVolume() + resources.getString(R.string.ProductDetailsActivity_volume_unit);
        String weightString = resources.getString(R.string.ProductDetailsActivity_weight) + ": " +  selectedProduct.getWeight() + resources.getString(R.string.ProductDetailsActivity_weight_unit);
        String expirationDateString = selectedProduct.getExpirationDate();
        String[] dateArray = expirationDateString.split("-");
        if (dateArray.length > 1)
            expirationDateString = dateArray[2] + "." + dateArray[1] + "." + dateArray[0];

        if (selectedProduct.getName().length() > 25)
            nameTv.setText(selectedProduct.getName().substring(0, 24) + "...");
        else
            nameTv.setText(selectedProduct.getName());
        volumeTv.setText(volumeString);
        weightTv.setText(weightString);
        expirationDateTv.setText(expirationDateString);

        try {
            expirationDateDt = simpleDateFormat.parse(selectedProduct.getExpirationDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.add(Calendar.DAY_OF_MONTH, myPreferences.getInt(
                PREFERENCES_DAYS_TO_NOTIFICATIONS, Notification.NOTIFICATION_DEFAULT_DAYS));
        calendar.add(Calendar.DAY_OF_MONTH, 3);
        Date dayOfNotification = calendar.getTime();
        if (dayOfNotification.after(expirationDateDt))
            viewHolder.itemView.setBackgroundColor(resources.getColor(R.color.background_expired_products));

        viewHolder.itemView.setOnClickListener(v -> listener.onItemClick(selectedProduct));

        Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
        viewHolder.itemView.setAnimation(animation);
    }

    public interface OnItemClickListener {
        void onItemClick(ProductEntity product);
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

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text_productName)
        TextView nameTv;
        @BindView(R.id.text_productVolume)
        TextView volumeTv;
        @BindView(R.id.text_productWeight)
        TextView weightTv;
        @BindView(R.id.text_expirationDateValue)
        TextView expirationDateTv;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            nameTv = itemView.findViewById(R.id.text_productName);
            volumeTv = itemView.findViewById(R.id.text_productVolume);
            weightTv = itemView.findViewById(R.id.text_productWeight);
            expirationDateTv = itemView.findViewById(R.id.text_expirationDateValue);
        }

        public void bind(final ProductEntity product, final OnItemClickListener listener) {
        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}