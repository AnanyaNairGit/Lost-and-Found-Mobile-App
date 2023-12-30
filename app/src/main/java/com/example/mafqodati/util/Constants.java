package com.example.mafqodati.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Constants {


    public static String convertTimeMillsToDateString(long mills){
        Date currentDate = new Date(mills);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String dateString = dateFormat.format(currentDate);
        return dateString;
    }
}
