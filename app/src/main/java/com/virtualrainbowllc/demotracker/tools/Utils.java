/*
 * Copyright 2016 Virtual Rainbow, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.virtualrainbowllc.demotracker.tools;

import android.widget.EditText;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import timber.log.Timber;

public class Utils {

    private Utils() {
    } // prevents instantiation

    public static String getStringFromET(EditText editText) {
        return editText.getText().toString().trim();
    }

    public static Integer getIntFromET(EditText editText) {
        try {
            return Integer.parseInt(editText.getText().toString().trim());
        } catch (NumberFormatException e) {
            Timber.e(e.getMessage());
        }
        return 0;
    }

    public static String buildString(List<String> stringList) {
        StringBuilder sb = new StringBuilder();
        for (String s : stringList) {
            sb.append(s);
            sb.append(" ");
        }
        return sb.toString();
    }

    public static String getDateStringFromDate(Date date) {
        DateFormat formatter = new SimpleDateFormat("MM/dd/yy", Locale.US);
        return formatter.format(date);
    }

    public static Date getDateFromString(String date) {
        DateFormat formatter = new SimpleDateFormat("MM/dd/yy", Locale.US);
        Date dateObject = new Date();
        try {
            dateObject = formatter.parse(date);
        } catch (ParseException e) {
            Timber.e(e.getMessage());
        }
        return dateObject;
    }

    public static String getFormattedDateFromPicker(int month, int day, int year) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day);
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.US);
        return sdf.format(cal.getTime());
    }
}
