package com.sunilbainsla.kafkastreampoc.controllers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ReadValidator {

    public static void main(String[] args) {
        String filePath = "/Users/sunkumar2/Desktop/d/FileRead2.txt";
        long startTime = System.nanoTime();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            // Read the first line (header)
            String header = br.readLine();

            // Validate the header
            if (isValidHeader(header)) {
                System.out.println("Header is valid: " + header);
            } else {
                System.out.println("Invalid header: " + header);
                return; // Exit if header is not valid
            }

            // Read the rest of the file until the last line (footer)
            String line;
            String footer = null;
            while ((line = br.readLine()) != null) {
                footer = line; // Keep updating the footer until the last line
            }

            // Validate the footer
            if (isValidFooter(footer)) {
                System.out.println("Footer is valid: " + footer);
                long endTime = System.nanoTime();
                double processingTimeSeconds = (endTime - startTime) / 1_000_000_000.0;
                System.out.println("Processing time: " +processingTimeSeconds+ " Seconds");
            } else {
                System.out.println("Invalid footer: " + footer);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean isValidHeader(String header) {
        System.out.println("Header" +header);
        // Implement your header validation logic here
        // For example, check if the header matches a specific format or contains certain information
        return header != null && header.startsWith("HDRECW37");
    }

    private static boolean isValidFooter(String footer) {
        // Implement your footer validation logic here
        // For example, check if the footer matches a specific format or contains certain information
        return footer != null && footer.startsWith("TLR");
    }
}
