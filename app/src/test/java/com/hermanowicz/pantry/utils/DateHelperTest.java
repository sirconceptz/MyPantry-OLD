/*
 * Copyright (c) 2019
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

package com.hermanowicz.pantry.utils;

import org.junit.jupiter.api.Test;

import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DateHelperTest {

    private DateHelper dateHelper = new DateHelper("2020-01-03");

    @Test
    void getDayFromDate() {
        assertEquals(3, dateHelper.getDayFromDate());
    }

    @Test
    void getMonthFromDate() {
        assertEquals(1, dateHelper.getMonthFromDate());
    }

    @Test
    void getYearFromDate() {
        assertEquals(2020, dateHelper.getYearFromDate());
    }

    @Test
    void getDateInLocalFormat() {
        assertEquals("2020-01-03", dateHelper.getDateInLocalFormat());
    }

    @Test
    void getDateInSqlFormat() {
        assertEquals("2020-01-03", dateHelper.getDateInSqlFormat());
    }

    @Test
    void getActualDay() {
        Calendar calendar = Calendar.getInstance();
        assertEquals(calendar.get(Calendar.DAY_OF_MONTH), DateHelper.getActualDay(0));
    }

    @Test
    void getActualMonth() {
        Calendar calendar = Calendar.getInstance();
        assertEquals(calendar.get(Calendar.MONTH), DateHelper.getActualMonth());
    }

    @Test
    void getActualYear() {
        Calendar calendar = Calendar.getInstance();
        assertEquals(calendar.get(Calendar.YEAR), DateHelper.getActualYear());
    }
}