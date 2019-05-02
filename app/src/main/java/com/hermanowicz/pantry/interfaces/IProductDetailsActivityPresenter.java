/******************************************************************************
 * Copyright (c) 2019.                                                        *
 * Mateusz Hermanowicz                                                        *
 * My Pantry                                                                  *
 * https://www.mypantry.eu                                                    *
 * Released under Apache License Version 2.0                                  *
 ******************************************************************************/

package com.hermanowicz.pantry.interfaces;

import com.hermanowicz.pantry.models.Product;

public interface IProductDetailsActivityPresenter {
    void setProduct(Product product);

    void setHashCode(int hashCode);

    void showProductDetails();

    void deleteProduct(int productID);

    void printQRCode();

    void navigateToMyPantryActivity();
}