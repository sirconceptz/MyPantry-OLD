/******************************************************************************
 * Copyright (c) 2019.                                                        *
 * Mateusz Hermanowicz                                                        *
 * My Pantry                                                                  *
 * https://www.mypantry.eu                                                    *
 * Released under Apache License Version 2.0                                  *
 ******************************************************************************/

package com.hermanowicz.pantry;

/**
 * <h1>Const</h1>
 * Class to store a const values from application.
 *
 * @author  Mateusz Hermanowicz
 * @version 1.0
 * @since   1.0
 */
class Const {
    static final String DB_FILE_NAME                      = "database.db";
    static final String DB_TABLE_NAME                     = "products";
    static final String PDF_FILENAME                      = "qrcodes-mypantry.pdf";
    static final int    NOTIFICATION_DEFAULT_HOUR         = 12;
    static final int    NOTIFICATION_DEFAULT_DAYS         = 3;
    static final int    VIBRATE_DURATION                  = 1000;
    static final int    QR_CODE_WIDTH                     = 100;
    static final int    QR_CODE_HEIGHT                    = 100;
    static final String DAYS_TAG                          = "%DAYS%";
    static final String PRODUCT_NAME_TAG                  = "%PRODUCT_NAME%";
    static final String URL_API                           = "https://www.mypantry.eu/api";
    static final String API_MAIL_FILE                     = "mail.php";
    static final String PREFERENCES_EMAIL_ADDRESS         = "EMAIL_ADDRESS";
    static final String PREFERENCES_EMAIL_NOTIFICATIONS   = "EMAIL_NOTIFICATIONS?";
    static final String PREFERENCES_PUSH_NOTIFICATIONS    = "PUSH_NOTIFICATIONS?";
    static final String PREFERENCES_DAYS_TO_NOTIFICATIONS = "HOW_MANY_DAYS_BEFORE_EXPIRATION_DATE_SEND_A_NOTIFICATION?";
    static final String PREFERENCES_HOUR_OF_NOTIFICATIONS = "HOUR_OF_NOTIFICATIONS?";
    static final int    APP_PERMISSIONS_EXTERNAL_STORAGE  = 23;
}