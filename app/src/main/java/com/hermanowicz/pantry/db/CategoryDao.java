package com.hermanowicz.pantry.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CategoryDao {
    @Query("SELECT * FROM categories WHERE id = (:id)")
    Category getCategory(int id);

    @Query("SELECT * FROM categories")
    List<Category> getAllOwnCategories();

    @Query("SELECT name FROM categories")
    String[] getAllCategoriesArray();

    @Insert
    void addCategory(Category category);

    @Delete
    void deleteCategory(Category category);

    @Update
    void updateCategory(Category category);
}