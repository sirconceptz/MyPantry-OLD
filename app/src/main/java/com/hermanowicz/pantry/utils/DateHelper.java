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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateHelper {

    private Calendar calendar = Calendar.getInstance();
    private final DateFormat localDateFormat = DateFormat.getDateInstance();
    private final SimpleDateFormat sqlDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private String[] dateArray;

    public DateHelper(String date){
        dateArray = date.split("\\.");
        if(dateArray.length < 2)
            dateArray = date.split("-");
    }

    public int getDayFromDate(){
        return Integer.valueOf(dateArray[2]);
    }

    public int getMonthFromDate(){
        return Integer.valueOf(dateArray[1]);
    }

    public int getYearFromDate(){
        return Integer.valueOf(dateArray[0]);
    }

    public String getDateInLocalFormat() {
        if (dateArray.length >= 2){
            calendar.set(Integer.parseInt(dateArray[0]), Integer.parseInt(dateArray[1])-1, Integer.parseInt(dateArray[2]));
            Date date = calendar.getTime();
            return localDateFormat.format(date);
        }
        else
            return "-";
    }

    public String getDateInSqlFormat() {
        if (dateArray.length >= 2){
            calendar.set(Integer.parseInt(dateArray[0]), Integer.parseInt(dateArray[1])-1, Integer.parseInt(dateArray[2]));
            Date date = calendar.getTime();
            return sqlDateFormat.format(date);
        }
        else
            return "-";
    }

    public static int getActualDay(int addDayToDate){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, addDayToDate);
        int date = calendar.get(Calendar.DAY_OF_MONTH);
        return date;
    }

    public static int getActualMonth(){
        Calendar calendar = Calendar.getInstance();
        int date = calendar.get(Calendar.MONTH);
        return date;
    }

    public static int getActualYear(){
        Calendar calendar = Calendar.getInstance();
        int date = calendar.get(Calendar.YEAR);
        return date;
    }
}