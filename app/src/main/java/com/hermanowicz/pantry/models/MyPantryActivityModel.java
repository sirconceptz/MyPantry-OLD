/******************************************************************************
 * Copyright (c) 2019.                                                        *
 * Mateusz Hermanowicz                                                        *
 * My Pantry                                                                  *
 * https://www.mypantry.eu                                                    *
 * Released under Apache License Version 2.0                                  *
 ******************************************************************************/

package com.hermanowicz.pantry.models;

public class MyPantryActivityModel {

    private String fltrName = null;
    private String fltrExpirationDateSince = null;
    private String fltrExpirationDateFor = null;
    private String fltrProductionDateSince = null;
    private String fltrProductionDateFor = null;
    private String fltrTypeOfProduct = null;
    private String fltrProductFeatures = null;
    private String fltrTaste = null;
    private int fltrWeightSince = -1;
    private int fltrWeightFor = -1;
    private int fltrVolumeSince = -1;
    private int fltrVolumeFor = -1;
    private int fltrHasSugar = -1;
    private int fltrHasSalt = -1;

    public void clearFilters() {
        fltrName = null;
        fltrTypeOfProduct = null;
        fltrProductFeatures = null;
        fltrExpirationDateSince = null;
        fltrExpirationDateFor = null;
        fltrProductionDateSince = null;
        fltrProductionDateFor = null;
        fltrVolumeSince = -1;
        fltrVolumeFor = -1;
        fltrWeightSince = -1;
        fltrWeightFor = -1;
        fltrHasSugar = -1;
        fltrHasSalt = -1;
        fltrTaste = null;
    }

    public String getFilterName() {
        return this.fltrName;
    }

    public String getFilterExpirationDateSince() {
        return this.fltrExpirationDateSince;
    }

    public void setFilterExpirationDateSince(String expirationDateSince) {
        this.fltrExpirationDateSince = expirationDateSince;
    }

    public String getFilterExpirationDateFor() {
        return this.fltrExpirationDateFor;
    }

    public void setFilterExpirationDateFor(String expirationDateFor) {
        this.fltrExpirationDateFor = expirationDateFor;
    }

    public String getFilterProductionDateSince() {
        return this.fltrProductionDateSince;
    }

    public void setFilterProductionDateSince(String productionDateSince) {
        this.fltrProductionDateSince = productionDateSince;
    }

    public String getFilterProductionDateFor() {
        return this.fltrProductionDateFor;
    }

    public void setFilterProductionDateFor(String productionDateFor) {
        this.fltrProductionDateFor = productionDateFor;
    }

    public String getFilterTypeOfProduct() {
        return this.fltrTypeOfProduct;
    }

    public void setFilterTypeOfProduct(String typeOfProduct) {
        this.fltrTypeOfProduct = typeOfProduct;
    }

    public String getFilterProductFeatures() {
        return this.fltrProductFeatures;
    }

    public void setFilterProductFeatures(String productFeatures) {
        this.fltrProductFeatures = productFeatures;
    }

    public int getFilterVolumeSince() {
        return fltrVolumeSince;
    }

    public String buildPantryQuery() {
        String selectQuery = "SELECT * FROM 'products'";

        if (fltrName != null) {
            selectQuery = selectQuery + " WHERE name LIKE '%" + fltrName + "%'";
        }
        if (fltrExpirationDateSince != null && fltrExpirationDateFor == null) {
            if (fltrName == null) {
                selectQuery = selectQuery + " WHERE ";
            } else {
                selectQuery = selectQuery + " AND ";
            }
            selectQuery = selectQuery + "expiration_date >= '" + fltrExpirationDateSince + "'";
        }
        if (fltrExpirationDateSince == null && fltrExpirationDateFor != null) {
            if (fltrName == null) {
                selectQuery = selectQuery + " WHERE ";
            } else {
                selectQuery = selectQuery + " AND ";
            }
            selectQuery = selectQuery + "expiration_date <= '" + fltrExpirationDateFor + "'";
        }
        if (fltrExpirationDateSince != null && fltrExpirationDateFor != null) {
            if (fltrName == null) {
                selectQuery = selectQuery + " WHERE ";
            } else {
                selectQuery = selectQuery + " AND ";
            }
            selectQuery = selectQuery + "expiration_date BETWEEN '" + fltrExpirationDateSince + "' AND '" + fltrExpirationDateFor + "'";
        }
        if (fltrProductionDateSince != null && fltrProductionDateFor == null) {
            if (fltrName == null && fltrExpirationDateSince == null && fltrExpirationDateFor == null) {
                selectQuery = selectQuery + " WHERE ";
            } else {
                selectQuery = selectQuery + " AND ";
            }
            selectQuery = selectQuery + "production_date >= '" + fltrProductionDateSince + "'";
        }
        if (fltrProductionDateSince == null && fltrProductionDateFor != null) {
            if (fltrName == null && fltrExpirationDateSince == null && fltrExpirationDateFor == null) {
                selectQuery = selectQuery + " WHERE ";
            } else {
                selectQuery = selectQuery + " AND ";
            }
            selectQuery = selectQuery + "production_date <= '" + fltrProductionDateFor + "'";
        }
        if (fltrProductionDateSince != null && fltrProductionDateFor != null) {
            if (fltrName == null && fltrExpirationDateSince == null && fltrExpirationDateFor == null) {
                selectQuery = selectQuery + " WHERE ";
            } else {
                selectQuery = selectQuery + " AND ";
            }
            selectQuery = selectQuery + "production_date BETWEEN'" + fltrProductionDateSince + "' AND '" + fltrProductionDateFor + "'";
        }
        if (fltrTypeOfProduct != null && fltrProductFeatures == null) {
            if (fltrName == null && fltrExpirationDateSince == null && fltrExpirationDateFor == null && fltrProductionDateSince == null
                    && fltrProductionDateFor == null) {
                selectQuery = selectQuery + " WHERE ";
            } else {
                selectQuery = selectQuery + " AND ";
            }
            selectQuery = selectQuery + "type_of_product LIKE '%" + fltrTypeOfProduct + "%'";
        }
        if (fltrTypeOfProduct == null && fltrProductFeatures != null) {
            if (fltrName == null && fltrExpirationDateSince == null && fltrExpirationDateFor == null && fltrProductionDateSince == null
                    && fltrProductionDateFor == null) {
                selectQuery = selectQuery + " WHERE ";
            } else {
                selectQuery = selectQuery + " AND ";
            }
            selectQuery = selectQuery + "product_features LIKE '%" + fltrProductFeatures + "%'";
        }
        if (fltrTypeOfProduct != null && fltrProductFeatures != null) {
            if (fltrName == null && fltrExpirationDateSince == null && fltrExpirationDateFor == null && fltrProductionDateSince == null
                    && fltrProductionDateFor == null) {
                selectQuery = selectQuery + " WHERE ";
            } else {
                selectQuery = selectQuery + " AND ";
            }
            selectQuery = selectQuery + "type_of_product LIKE '%" + fltrTypeOfProduct + "%' AND product_features LIKE '%" + fltrProductFeatures + "%'";
        }
        if (fltrVolumeSince >= 0 && fltrVolumeFor == -1) {
            if (fltrName == null && fltrExpirationDateSince == null && fltrExpirationDateFor == null && fltrProductionDateSince == null
                    && fltrProductionDateFor == null && fltrTypeOfProduct == null && fltrProductFeatures == null) {
                selectQuery = selectQuery + " WHERE ";
            } else {
                selectQuery = selectQuery + " AND ";
            }
            selectQuery = selectQuery + "volume >= '" + fltrVolumeSince + "'";
        }
        if (fltrVolumeSince == -1 && fltrVolumeFor >= 0) {
            if (fltrName == null && fltrExpirationDateSince == null && fltrExpirationDateFor == null && fltrProductionDateSince == null
                    && fltrProductionDateFor == null && fltrTypeOfProduct == null && fltrProductFeatures == null) {
                selectQuery = selectQuery + " WHERE ";
            } else {
                selectQuery = selectQuery + " AND ";
            }
            selectQuery = selectQuery + "volume <= '" + fltrVolumeFor + "'";
        }
        if (fltrVolumeSince >= 0 && fltrVolumeFor >= 0) {
            if (fltrName == null && fltrExpirationDateSince == null && fltrExpirationDateFor == null && fltrProductionDateSince == null
                    && fltrProductionDateFor == null && fltrTypeOfProduct == null && fltrProductFeatures == null) {
                selectQuery = selectQuery + " WHERE ";
            } else {
                selectQuery = selectQuery + " AND ";
            }
            selectQuery = selectQuery + "volume BETWEEN '" + fltrVolumeSince + "' AND '" + fltrVolumeFor + "'";
        }
        if (fltrWeightSince >= 0 && fltrWeightFor == -1) {
            if (fltrName == null && fltrExpirationDateSince == null && fltrExpirationDateFor == null && fltrProductionDateSince == null
                    && fltrProductionDateFor == null && fltrTypeOfProduct == null && fltrProductFeatures == null
                    && fltrVolumeSince == -1 && fltrVolumeFor == -1) {
                selectQuery = selectQuery + " WHERE ";
            } else {
                selectQuery = selectQuery + " AND ";
            }
            selectQuery = selectQuery + "weight >= '" + fltrWeightSince + "'";
        }
        if (fltrWeightSince == -1 && fltrWeightFor >= 0) {
            if (fltrName == null && fltrExpirationDateSince == null && fltrExpirationDateFor == null && fltrProductionDateSince == null
                    && fltrProductionDateFor == null && fltrTypeOfProduct == null && fltrProductFeatures == null
                    && fltrVolumeSince == -1 && fltrVolumeFor == -1) {
                selectQuery = selectQuery + " WHERE ";
            } else {
                selectQuery = selectQuery + " AND ";
            }
            selectQuery = selectQuery + "weight <= '" + fltrWeightFor + "'";
        }
        if (fltrWeightSince >= 0 && fltrWeightFor >= 0) {
            if (fltrName == null && fltrExpirationDateSince == null && fltrExpirationDateFor == null && fltrProductionDateSince == null
                    && fltrProductionDateFor == null && fltrTypeOfProduct == null && fltrProductFeatures == null
                    && fltrVolumeSince == -1 && fltrVolumeFor == -1) {
                selectQuery = selectQuery + " WHERE ";
            } else {
                selectQuery = selectQuery + " AND ";
            }
            selectQuery = selectQuery + "weight BETWEEN '" + fltrWeightSince + "' AND '" + fltrWeightFor + "'";
        }
        if (fltrHasSugar >= 0) {
            if (fltrName == null && fltrExpirationDateSince == null && fltrExpirationDateFor == null && fltrProductionDateSince == null
                    && fltrProductionDateFor == null && fltrTypeOfProduct == null && fltrProductFeatures == null
                    && fltrVolumeSince == -1 && fltrVolumeFor == -1 && fltrWeightSince == -1 && fltrWeightFor == -1) {
                selectQuery = selectQuery + " WHERE ";
            } else {
                selectQuery = selectQuery + " AND ";
            }
            selectQuery = selectQuery + "has_sugar='" + fltrHasSugar + "'";
        }
        if (fltrHasSalt >= 0) {
            if (fltrName == null && fltrExpirationDateSince == null && fltrExpirationDateFor == null && fltrProductionDateSince == null
                    && fltrProductionDateFor == null && fltrTypeOfProduct == null && fltrProductFeatures == null
                    && fltrVolumeSince == -1 && fltrVolumeFor == -1 && fltrWeightSince == -1 && fltrWeightFor == -1
                    && fltrHasSugar == -1) {
                selectQuery = selectQuery + " WHERE ";
            } else {
                selectQuery = selectQuery + " AND ";
            }
            selectQuery = selectQuery + "has_salt='" + fltrHasSalt + "'";
        }
        if (fltrTaste != null) {
            if (fltrName == null && fltrExpirationDateSince == null && fltrExpirationDateFor == null && fltrProductionDateSince == null
                    && fltrProductionDateFor == null && fltrTypeOfProduct == null && fltrProductFeatures == null
                    && fltrVolumeSince == -1 && fltrVolumeFor == -1 && fltrWeightSince == -1 && fltrWeightFor == -1
                    && fltrHasSugar == -1 && fltrHasSalt == -1) {
                selectQuery = selectQuery + " WHERE ";
            } else {
                selectQuery = selectQuery + " AND ";
            }
            selectQuery = selectQuery + "taste='" + fltrTaste + "'";
        }
        selectQuery = selectQuery + " ORDER BY expiration_date ASC";
        return selectQuery;
    }

    public void setFilterVolumeSince(int volumeSince) {
        this.fltrVolumeSince = volumeSince;
    }

    public int getFilterVolumeFor() {
        return fltrVolumeFor;
    }

    public void setFilterVolumeFor(int volumeFor) {
        this.fltrVolumeFor = volumeFor;
    }

    public int getFilterWeightSince() {
        return fltrWeightSince;
    }

    public void setFilterWeightSince(int weightSince) {
        this.fltrWeightSince = weightSince;
    }

    public int getFilterWeightFor() {
        return fltrWeightFor;
    }

    public void setFilterWeightFor(int weightFor) {
        this.fltrWeightFor = weightFor;
    }

    public int getFilterHasSugar() {
        return fltrHasSugar;
    }

    public void setFilterHasSugar(int hasSugar) {
        this.fltrHasSugar = hasSugar;
    }

    public int getFilterHasSalt() {
        return fltrHasSalt;
    }

    public void setFilterHasSalt(int hasSalt) {
        this.fltrHasSalt = hasSalt;
    }

    public String getFilterTaste() {
        return fltrTaste;

    }

    public void setFilterTaste(String taste) {
        this.fltrTaste = taste;
    }

    public void setFilterNameOfProduct(String nameOfProduct) {
        this.fltrName = nameOfProduct;
    }
}