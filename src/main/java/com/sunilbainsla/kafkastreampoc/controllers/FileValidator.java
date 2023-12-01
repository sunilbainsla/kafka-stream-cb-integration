package com.sunilbainsla.kafkastreampoc.controllers;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileValidator {

    public static void main(String[] args) {
        String filePath = "/Users/sunkumar2/Desktop/d/FileRead2.txt";
        Path path = Paths.get(filePath);

        try {
            // Read the header
            String header = Files.lines(path).findFirst().orElse(null);

            // Validate the header
            if (isValidHeader(header)) {
                System.out.println("Header is valid: " + header);
            } else {
                System.out.println("Invalid header: " + header);
                return; // Exit if header is not valid
            }

            // Read the footer
            String footer = Files.lines(path).reduce((first, second) -> second).orElse(null);

            // Validate the footer
            if (isValidFooter(footer)) {
                System.out.println("Footer is valid: " + footer);
            } else {
                System.out.println("Invalid footer: " + footer);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean isValidHeader(String header) {
        System.out.println("Header: " + header);
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
