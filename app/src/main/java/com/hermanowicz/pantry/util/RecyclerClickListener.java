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
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.annotations.NotNull;

/**
 * <h1>RecyclerClickListener</h1>
 * Class to detect long click in products view
 *
 * @author  Mateusz Hermanowicz
 */

public class RecyclerClickListener implements RecyclerView.OnItemTouchListener {

    public interface OnItemClickListener {
            void onItemClick(View view, int position);
            void onItemLongClick(View view, int position);
    }

    private final OnItemClickListener listener;
    private final GestureDetector gestureDetector;

    public RecyclerClickListener(Context context, final RecyclerView recyclerView, OnItemClickListener listener) {
        this.listener = listener;

        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e)
                {
                    return true;
                }

            @Override
            public void onLongPress(MotionEvent e) {
                View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());

                if(childView != null && RecyclerClickListener.this.listener != null) {
                    RecyclerClickListener.this.listener.onItemLongClick(childView, recyclerView.getChildAdapterPosition(childView));
                }
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
        View childView = view.findChildViewUnder(e.getX(), e.getY());

        if(childView != null && listener != null && gestureDetector.onTouchEvent(e)) {
            listener.onItemClick(childView, view.getChildAdapterPosition(childView));
        }
        return false;
    }

    @Override
    public void onTouchEvent(@NotNull RecyclerView view, @NotNull MotionEvent motionEvent){}

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) { }
}