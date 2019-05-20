/******************************************************************************
 * Copyright (c) 2019.                                                        *
 * Mateusz Hermanowicz                                                        *
 * My Pantry                                                                  *
 * https://www.mypantry.eu                                                    *
 * Released under Apache License Version 2.0                                  *
 ******************************************************************************/

package com.hermanowicz.pantry.models;

import android.content.res.Resources;

import com.hermanowicz.pantry.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NewProductActivityModel {

    private Resources resources;
    private String name = "";
    private String typeOfProduct = "";
    private String productFeatures = "";
    private String expirationDate = "";
    private String productionDate = "";
    private String composition = "";
    private String healingProperties = "";
    private String dosage = "";
    private String taste = "";
    private int idOfLastProductInDB, quantity, volume, weight;
    private boolean hasSugar, hasSalt, isSweet, isSour, isSweetAndSour, isBitter, isSalty;

    public NewProductActivityModel(Resources resources) {
        this.resources = resources;
    }

    public ArrayList<Product> buildProductsList() {
        setTaste();
        ArrayList<Product> productArrayList = new ArrayList<Product>() {
        };
        for (int counter = 1; counter <= quantity; counter++) {
            Product product = new Product.Builder()
                    .setID(idOfLastProductInDB + counter)
                    .setName(name)
                    .setHashCode("")
                    .setTypeOfProduct(typeOfProduct)
                    .setProductFeatures(productFeatures)
                    .setExpirationDate(expirationDate)
                    .setProductionDate(productionDate)
                    .setComposition(composition)
                    .setHealingProperties(healingProperties)
                    .setDosage(dosage)
                    .setVolume(volume)
                    .setWeight(weight)
                    .setHasSugar(Boolean.compare(hasSugar, false))
                    .setHasSalt(Boolean.compare(hasSalt, false))
                    .setTaste(taste)
                    .createProduct();
            productArrayList.add(product);
        }
        return productArrayList;
    }

    public void setIdOfLastProductInDb(int idOfLastProductInDB) {
        this.idOfLastProductInDB = idOfLastProductInDB;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTypeOfProduct(String typeOfProduct) {
        this.typeOfProduct = typeOfProduct;
    }

    public void setProductFeatures(String productFeatures) {
        this.productFeatures = productFeatures;
    }


    public void setExpirationDate(String expirationDate) {
        try {
            String[] expirationDateArray = expirationDate.split("\\.");
            int year = Integer.valueOf(expirationDateArray[2]);
            int month = Integer.valueOf(expirationDateArray[1]);
            int day = Integer.valueOf(expirationDateArray[0]);
            this.expirationDate = year + "-" + month + "-" + day;
        } catch (ArrayIndexOutOfBoundsException e) {
            this.expirationDate = "";
        }
    }


    public void setProductionDate(String productionDate) {
        try {
            String[] productionDateArray = productionDate.split("\\.");
            int year = Integer.valueOf(productionDateArray[2]);
            int month = Integer.valueOf(productionDateArray[1]);
            int day = Integer.valueOf(productionDateArray[0]);
            this.productionDate = year + "-" + month + "-" + day;
        } catch (ArrayIndexOutOfBoundsException e) {
            this.productionDate = "";
        }
    }

    public void setComposition(String composition) {
        this.composition = composition;
    }

    public void setHealingProperties(String healingProperties) {
        this.healingProperties = healingProperties;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public void setTaste(String taste) {
        this.taste = taste;
    }

    public void setHasSugar(Boolean hasSugar) {
        this.hasSugar = hasSugar;
    }

    public void setHasSalt(Boolean hasSalt) {
        this.hasSalt = hasSalt;
    }

    public void parseQuantityProducts(String quantity) {
        try {
            this.quantity = Integer.valueOf(quantity);
        } catch (NumberFormatException e) {
            this.quantity = 1;
        }
        if (this.quantity < 1)
            this.quantity = 1;
    }

    public void parseVolumeProduct(String volume) {
        try {
            this.volume = Integer.valueOf(volume);
        } catch (NumberFormatException e) {
            this.volume = 0;
        }
        if (this.quantity < 0)
            this.quantity = 0;
    }

    public void parseWeightProduct(String weight) {
        try {
            this.weight = Integer.valueOf(weight);
        } catch (NumberFormatException e) {
            this.weight = 0;
        }
        if (this.quantity < 0)
            this.quantity = 0;
    }

    public String[] getExpirationDateArrayList() {
        String[] expirationDateArray = expirationDate.split("\\-");
        return expirationDateArray;
    }


    public String[] getProductionDateArrayList() {
        String[] productionDateArray = productionDate.split("\\-");
        return productionDateArray;
    }

    public void setIsSweet(boolean isSweet) {
        this.isSweet = isSweet;
    }

    public void setIsSour(boolean isSour) {
        this.isSour = isSour;
    }

    public void setIsSweetAndSour(boolean isSweetAndSour) {
        this.isSweetAndSour = isSweetAndSour;
    }

    public void setIsBitter(boolean isBitter) {
        this.isBitter = isBitter;
    }

    public void setIsSalty(boolean isSalty) {
        this.isSalty = isSalty;
    }

    private void setTaste() {
        String[] filterTasteArray = resources.getStringArray(R.array.ProductDetailsActivity_taste_array);
        if (!isSweet && !isSour && !isSweetAndSour && !isBitter && !isSalty) {
            taste = resources.getString(R.string.ProductDetailsActivity_not_selected);
        } else if (isSweet) {
            taste = filterTasteArray[1];
        } else if (isSour) {
            taste = filterTasteArray[2];
        } else if (isSweetAndSour) {
            taste = filterTasteArray[3];
        } else if (isBitter) {
            taste = filterTasteArray[4];
        } else if (isSalty) {
            taste = filterTasteArray[5];
        }
    }

    public String getOnProductAddStatement() {
        String statementToShow;
        if (quantity < 2)
            statementToShow = resources.getString(R.string.NewProductActivity_product_added_successful);
        else
            statementToShow = resources.getString(R.string.NewProductActivity_products_added_successful);
        return statementToShow;
    }

    public boolean checkCorrectProductName() {
        boolean correctProductName = false;
        if (!name.equals(""))
            correctProductName = true;
        return correctProductName;
    }

    public boolean checkCorrectTypeOfProduct() {
        String[] typeOfProductsArray = resources.getStringArray(R.array.ProductDetailsActivity_type_of_product_array);
        boolean correctTypeOfProduct = false;
        if (!typeOfProduct.equals(typeOfProductsArray[0]))
            correctTypeOfProduct = true;
        return correctTypeOfProduct;
    }

    public boolean checkCorrectExpirationDate() {
        boolean correctExpirationDate = false;
        if (!expirationDate.equals(""))
            correctExpirationDate = true;
        return correctExpirationDate;
    }

    public ArrayList<String> getTextToQRCodeList(List<Product> productsList) {
        ArrayList<String> textToQRCodeList = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();

        for (int counter = 0; counter < productsList.size(); counter++) {
            try {
                jsonObject.put("product_id", productsList.get(counter).getID());
                jsonObject.put("hash_code", productsList.get(counter).hashCode());
                textToQRCodeList.add(jsonObject.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return textToQRCodeList;
    }

    public ArrayList<String> getNamesOfProductsList(List<Product> productsList) {
        ArrayList<String> namesOfProductsList = new ArrayList<>();
        String productName;

        for (int counter = 0; counter < productsList.size(); counter++) {
            productName = productsList.get(counter).getName();
            if (productName.length() > 15) {
                namesOfProductsList.add(productName.substring(0, 14) + ".");
            } else {
                namesOfProductsList.add(productName);
            }
        }
        return namesOfProductsList;
    }

    public ArrayList<String> getExpirationDatesList(List<Product> productsList) {
        ArrayList<String> expirationDatesList = new ArrayList<>();

        for (int counter = 0; counter < productsList.size(); counter++) {
            expirationDatesList.add(productsList.get(counter).getExpirationDate());
        }
        return expirationDatesList;
    }
}