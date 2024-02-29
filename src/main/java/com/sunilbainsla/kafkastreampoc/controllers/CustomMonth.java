package com.sunilbainsla.kafkastreampoc.controllers;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class CustomMonth {
    public static void main(String[] args) {
        String dateString = "00JLY2024";
        String pattern = "ddMMMyyyy";

        if (isValidDate(dateString, pattern)) {
            System.out.println("The date is valid.");
        } else {
            System.out.println("The date is not valid.");
        }
    }

    public static boolean isValidDate(String dateString, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.ENGLISH);
        sdf.setLenient(false); // Set lenient mode to false to enforce strict date parsing

        // Define custom month abbreviations
        String[] customMonths = {"JAN", "FEB", "MCH", "APR", "MAY", "JUN", "JLY", "AUG", "SEP", "OCT", "NOV", "DEC"};

        // Create custom DateFormatSymbols
        DateFormatSymbols symbols = new DateFormatSymbols(Locale.ENGLISH);
        symbols.setShortMonths(customMonths);

        // Set custom DateFormatSymbols to SimpleDateFormat
        sdf.setDateFormatSymbols(symbols);

        try {
            // Attempt to parse the string into a date
            sdf.parse(dateString);
            return true; // Parsing successful, so the date is valid
        } catch (ParseException e) {
            return false; // Parsing failed, so the date is not valid
        }
    }
}