package com.example.mafqodati.util;

import com.example.mafqodati.models.Category;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Constants {
    public static ArrayList<Category> categories = new ArrayList<>();
    public final static int POST_TYPE_LOST = 1 ;
    public final static int POST_TYPE_FOUND = 1 ;

    public static String convertTimeMillsToDateString(long mills){
        Date currentDate = new Date(mills);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String dateString = dateFormat.format(currentDate);
        return dateString;
    }
}
