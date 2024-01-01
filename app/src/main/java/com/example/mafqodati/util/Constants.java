package com.example.mafqodati.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Constants {


    public static String convertTimeMillsToDateString(long mills){
        Date currentDate = new Date(mills);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String dateString = dateFormat.format(currentDate);
        return dateString;
    }

    public static boolean isValidEmail(String email){
        // Define the email pattern
        String emailPattern = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

        // Create a Pattern object
        Pattern pattern = Pattern.compile(emailPattern);

        // Create a Matcher object
        Matcher matcher = pattern.matcher(email);

        // Check if the email matches the pattern
        return matcher.matches();
    }
    public static boolean isValidPassword(String password){
        return password.length() > 7 ;
    }

    public static boolean isPasswordMatches(String password , String confirmPassword){
        return password.equals(confirmPassword);
    }
}
