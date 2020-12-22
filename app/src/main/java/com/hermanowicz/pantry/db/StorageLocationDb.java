package com.hermanowicz.pantry.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {StorageLocation.class}, version = 1)
public abstract class StorageLocationDb extends RoomDatabase {

    public abstract StorageLocationDao storageLocationDao();

    private static StorageLocationDb INSTANCE;
    private static final Object sLock = new Object();

    public static StorageLocationDb getInstance(Context context) {
        synchronized (sLock) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        StorageLocationDb.class, "StorageLocationDb.db")
                        .allowMainThreadQueries()
                        .addCallback(new Callback() {
                            @Override
                            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                super.onCreate(db);
                            }
                        })
                        .build();
            }
            return INSTANCE;
        }
    }
}