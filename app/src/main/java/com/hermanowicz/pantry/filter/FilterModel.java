package com.hermanowicz.pantry.filter;

public class FilterModel {

    private String name = null;
    private String typeOfProduct = null;
    private String productFeatures = null;
    private String expirationDateSince = null;
    private String expirationDateFor = null;
    private String productionDateSince = null;
    private String productionDateFor = null;
    private String composition = null;
    private String healingProperties = null;
    private String dosage = null;
    private int volumeSince = -1;
    private int volumeFor = -1;
    private int weightSince = -1;
    private int weightFor = -1;
    private int hasSugar = -1;
    private int hasSalt = -1;
    private String taste = null;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getExpirationDateSince() {
        return expirationDateSince;
    }

    public void setExpirationDateSince(String expirationDateSince) {
        this.expirationDateSince = expirationDateSince;
    }

    public String getExpirationDateFor() {
        return expirationDateFor;
    }

    public void setExpirationDateFor(String expirationDateFor) {
        this.expirationDateFor = expirationDateFor;
    }

    public String getProductionDateSince() {
        return productionDateSince;
    }

    public void setProductionDateSince(String productionDateSince) {
        this.productionDateSince = productionDateSince;
    }

    public String getProductionDateFor() {
        return productionDateFor;
    }

    public void setProductionDateFor(String productionDateFor) {
        this.productionDateFor = productionDateFor;
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

    public int getVolumeSince() {
        return volumeSince;
    }

    public void setVolumeSince(int volumeSince) {
        this.volumeSince = volumeSince;
    }

    public int getVolumeFor() {
        return volumeFor;
    }

    public void setVolumeFor(int volumeFor) {
        this.volumeFor = volumeFor;
    }

    public int getWeightSince() {
        return weightSince;
    }

    public void setWeightSince(int weightSince) {
        this.weightSince = weightSince;
    }

    public int getWeightFor() {
        return weightFor;
    }

    public void setWeightFor(int weightFor) {
        this.weightFor = weightFor;
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