/******************************************************************************
 * Copyright (c) 2019.                                                        *
 * Mateusz Hermanowicz                                                        *
 * My Pantry                                                                  *
 * https://www.mypantry.eu                                                    *
 * Released under Apache License Version 2.0                                  *
 ******************************************************************************/

package com.hermanowicz.pantry.models;

public class MyPantryActivityModel {

    private String fltrName, fltrExpirationDateSince, fltrExpirationDateFor,
            fltrProductionDateSince, fltrProductionDateFor, fltrTypeOfProduct,
            fltrProductFeatures, fltrTaste, type_of_dialog;
    private int fltrWeightSince = -1, fltrWeightFor = -1, fltrVolumeSince = -1,
            fltrVolumeFor = -1, fltrHasSugar = -1, fltrHasSalt = -1;

    public MyPantryActivityModel() {

    }

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

}