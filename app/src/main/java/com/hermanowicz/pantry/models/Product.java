/******************************************************************************
 * Copyright (c) 2019.                                                        *
 * Mateusz Hermanowicz                                                        *
 * My Pantry                                                                  *
 * https://www.mypantry.eu                                                    *
 * Released under Apache License Version 2.0                                  *
 ******************************************************************************/

package com.hermanowicz.pantry.models;

/**
 * <h1>Product</h1>
 * Model of product. Products are stored in database.
 *
 * @author  Mateusz Hermanowicz
 * @version 1.0
 * @since   1.0
 */
public class Product {

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

    public int getID() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getHashCode() {
        return hashCode;
    }

    public String getTypeOfProduct() {
        return typeOfProduct;
    }

    public String getProductFeatures() {
        return productFeatures;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public String getProductionDate() {
        return productionDate;
    }

    public String getComposition() {
        return composition;
    }

    public String getHealingProperties() {
        return healingProperties;
    }

    public String getDosage() {
        return dosage;
    }

    public int getVolume() {
        return volume;
    }

    public int getWeight() {
        return weight;
    }

    public int getHasSugar() {
        return hasSugar;
    }

    public int getHasSalt() {
        return hasSalt;
    }

    public String getTaste() {
        return taste;
    }

    public Product(int id, String name, String hashCode, String typeOfProduct, String productFeatures, String expirationDate, String productionDate, String composition, String healingProperties, String dosage, int volume, int weight, int hasSugar, int hasSalt, String taste){

        this.id                = id;
        this.name              = name;
        this.hashCode          = hashCode;
        this.typeOfProduct     = typeOfProduct;
        this.productFeatures   = productFeatures;
        this.expirationDate    = expirationDate;
        this.productionDate    = productionDate;
        this.composition       = composition;
        this.healingProperties = healingProperties;
        this.dosage            = dosage;
        this.volume            = volume;
        this.weight            = weight;
        this.hasSugar          = hasSugar;
        this.hasSalt           = hasSalt;
        this.taste             = taste;
    }

    /**
     * <h1>Product.Builder</h1>
     * Builder for product.
     *
     * @author  Mateusz Hermanowicz
     * @version 1.0
     * @since   1.0
     */

    public static class Builder {
        private int     id;
        private String  name;
        private String  hashCode;
        private String  typeOfProduct;
        private String  productFeatures;
        private String  expirationDate;
        private String  productionDate;
        private String  composition;
        private String  healingProperties;
        private String  dosage;
        private int     volume;
        private int     weight;
        private int     hasSugar;
        private int     hasSalt;
        private String  taste;

        public Builder() {
        }

        public Builder setID(int id) {
            this.id = id;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setHashCode(String hashCode) {
            this.hashCode = hashCode;
            return this;
        }

        public Builder setTypeOfProduct(String categoryTypeOfProduct) {
            this.typeOfProduct = categoryTypeOfProduct;
            return this;
        }

        public Builder setProductFeatures(String productFeatures) {
            this.productFeatures = productFeatures;
            return this;
        }

        public Builder setExpirationDate(String expirationDate) {
            this.expirationDate = expirationDate;
            return this;
        }

        public Builder setProductionDate(String productionDate) {
            if(productionDate==null)
                this.productionDate = "";
            else
                this.productionDate = productionDate;
            return this;
        }

        public Builder setComposition(String composition) {
            if(composition==null)
                this.composition = "";
            else
                this.composition = composition;
            return this;
        }

        public Builder setHealingProperties(String healingProperties) {
            if(healingProperties==null)
                this.healingProperties = "";
            else
                this.healingProperties = healingProperties;
            return this;
        }

        public Builder setDosage(String dosage) {
            if(dosage==null)
                this.dosage = "";
            else
                this.dosage = dosage;
            return this;
        }

        public Builder setVolume(int volume) {
            this.volume = volume;
            return this;
        }

        public Builder setWeight(int weight) {
            this.weight = weight;
            return this;
        }

        public Builder setHasSugar(int hasSugar) {
            this.hasSugar = hasSugar;
            return this;
        }

        public Builder setHasSalt(int hasSalt) {
            this.hasSalt = hasSalt;
            return this;
        }

        public Builder setTaste(String taste) {
            this.taste = taste;
            return this;
        }

        public Product createProduct() {
            return new Product(id, name, hashCode, typeOfProduct, productFeatures, expirationDate, productionDate, composition, healingProperties, dosage, volume, weight, hasSugar, hasSalt, taste);
        }

    }
}