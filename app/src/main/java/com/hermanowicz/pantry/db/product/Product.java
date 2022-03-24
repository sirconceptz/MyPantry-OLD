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

package com.hermanowicz.pantry.db.product;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;

/**
 * <h1>Product/h1>
 * Product model.
 *
 * @author Mateusz Hermanowicz
 */

@Keep
@Entity(tableName = "products")
public class Product implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name = "";
    private String hashCode = "";
    private String typeOfProduct = "";
    private String productFeatures = "";
    private String storageLocation = "";
    private String expirationDate = "";
    private String productionDate = "";
    private String composition = "";
    private String healingProperties = "";
    private String dosage = "";
    private int volume;
    private int weight;
    private boolean hasSugar;
    private boolean hasSalt;
    private boolean isVege;
    private boolean isBio;
    private String taste = "";
    private String photoName = "";
    private String photoDescription = "";
    private String barcode = "";

    public Product(Parcel in) {
        id = in.readInt();
        name = in.readString();
        hashCode = in.readString();
        typeOfProduct = in.readString();
        productFeatures = in.readString();
        storageLocation = in.readString();
        expirationDate = in.readString();
        productionDate = in.readString();
        composition = in.readString();
        healingProperties = in.readString();
        dosage = in.readString();
        volume = in.readInt();
        weight = in.readInt();
        hasSugar = in.readByte() != 0;
        hasSalt = in.readByte() != 0;
        isVege = in.readByte() != 0;
        isBio = in.readByte() != 0;
        taste = in.readString();
        photoName = in.readString();
        photoDescription = in.readString();
        barcode = in.readString();
    }

    public Product() {}

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getShortName(){
        if(name.length()>18)
            return name.substring(0, 17) + "...";
        else
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

    public String getStorageLocation() {
        return storageLocation;
    }

    public void setStorageLocation(String storageLocation) {
        this.storageLocation = storageLocation;
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

    public boolean getHasSugar() {
        return hasSugar;
    }

    public void setHasSugar(boolean hasSugar) {
        this.hasSugar = hasSugar;
    }

    public boolean getHasSalt() {
        return hasSalt;
    }

    public void setHasSalt(boolean hasSalt) {
        this.hasSalt = hasSalt;
    }

    public boolean getIsVege() {
        return isVege;
    }

    public void setIsVege(boolean vege) {
        isVege = vege;
    }

    public boolean getIsBio() {
        return isBio;
    }

    public void setIsBio(boolean bio) {
        isBio = bio;
    }

    public String getTaste() {
        return taste;
    }

    public void setTaste(String taste) {
        this.taste = taste;
    }

    public String getPhotoName() {
        return photoName;
    }

    public void setPhotoName(String photoName) {
        this.photoName = photoName;
    }

    public String getPhotoDescription() {
        return photoDescription;
    }

    public void setPhotoDescription(String photoDescription) {
        this.photoDescription = photoDescription;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public static ArrayList<Product> getSimilarProductsList(@NonNull Product testedProduct, @NonNull List<Product> productList){
        ArrayList<Product> similarProductList = new ArrayList<>();
        for(Product singleProduct : productList){
            if(singleProduct.getName().equals(testedProduct.getName())
                    && singleProduct.getTypeOfProduct().equals(testedProduct.getTypeOfProduct())
                    && singleProduct.getProductFeatures().equals(testedProduct.getProductFeatures())
                    && singleProduct.getExpirationDate().equals(testedProduct.getExpirationDate())
                    && singleProduct.getProductionDate().equals(testedProduct.getProductionDate())
                    && singleProduct.getHealingProperties().equals(testedProduct.getHealingProperties())
                    && singleProduct.getComposition().equals(testedProduct.getComposition())
                    && singleProduct.getDosage().equals(testedProduct.getDosage())
                    && singleProduct.getBarcode().equals(testedProduct.getBarcode())
                    && singleProduct.getWeight() == testedProduct.getWeight()
                    && singleProduct.getVolume() == testedProduct.getVolume()
                    && singleProduct.getHasSugar() == testedProduct.getHasSugar()
                    && singleProduct.getHasSalt() == testedProduct.getHasSalt()
                    && singleProduct.getIsVege() == testedProduct.getIsVege()
                    && singleProduct.getIsBio() == testedProduct.getIsBio()
                    && singleProduct.getTaste().equals(testedProduct.getTaste())
                    && singleProduct.getStorageLocation().equals(testedProduct.getStorageLocation()))
                similarProductList.add(singleProduct);
        }
        return similarProductList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(hashCode);
        parcel.writeString(typeOfProduct);
        parcel.writeString(productFeatures);
        parcel.writeString(storageLocation);
        parcel.writeString(expirationDate);
        parcel.writeString(productionDate);
        parcel.writeString(composition);
        parcel.writeString(healingProperties);
        parcel.writeString(dosage);
        parcel.writeInt(volume);
        parcel.writeInt(weight);
        parcel.writeByte((byte) (hasSugar ? 1 : 0));
        parcel.writeByte((byte) (hasSalt ? 1 : 0));
        parcel.writeByte((byte) (isVege ? 1 : 0));
        parcel.writeByte((byte) (isBio ? 1 : 0));
        parcel.writeString(taste);
        parcel.writeString(photoName);
        parcel.writeString(photoDescription);
        parcel.writeString(barcode);
    }
}