/******************************************************************************
 * Copyright (c) 2019.                                                        *
 * Mateusz Hermanowicz                                                        *
 * My Pantry                                                                  *
 * https://www.mypantry.eu                                                    *
 * Released under Apache License Version 2.0                                  *
 ******************************************************************************/

package com.hermanowicz.pantry;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity (tableName = "products")
public class ProductEntity {

    @PrimaryKey(autoGenerate = true)
    private int     id;
    private String  name;                   //required
    private String  hashCode;               //automatic
    private String  typeOfProduct;          //required
    private String  productFeatures;        //required
    private String  expirationDate;         //required
    private String  productionDate;         //optional
    private String  composition;            //optional, only for homemade products
    private String  healingProperties;      //optional, only for tincture
    private String  dosage;                 //optional, only for tincture
    private int     volume;                 //optional
    private int     weight;                 //optional
    private int     hasSugar;               //optional
    private int     hasSalt;                //optional
    private String  taste;                  //required

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHashCode() {
        return hashCode;
    }

    public void setHashCode(String hashCode) {
        this.hashCode = hashCode;
    }

    public String getTypeOfProduct() {
        return typeOfProduct;
    }

    public void setTypeOfProduct(String typeOfProduct) {
        this.typeOfProduct = typeOfProduct;
    }

    public String getProductFeatures() {
        return productFeatures;
    }

    public void setProductFeatures(String productFeatures) {
        this.productFeatures = productFeatures;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getProductionDate() {
        return productionDate;
    }

    public void setProductionDate(String productionDate) {
        this.productionDate = productionDate;
    }

    public String getComposition() {
        return composition;
    }

    public void setComposition(String composition) {
        this.composition = composition;
    }

    public String getHealingProperties() {
        return healingProperties;
    }

    public void setHealingProperties(String healingProperties) {
        this.healingProperties = healingProperties;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getHasSugar() {
        return hasSugar;
    }

    public void setHasSugar(int hasSugar) {
        this.hasSugar = hasSugar;
    }

    public int getHasSalt() {
        return hasSalt;
    }

    public void setHasSalt(int hasSalt) {
        this.hasSalt = hasSalt;
    }

    public String getTaste() {
        return taste;
    }

    public void setTaste(String taste) {
        this.taste = taste;
    }
}