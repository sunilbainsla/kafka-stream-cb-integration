package com.sunilbainsla.kafkastreampoc.controllers;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class LastLineSubstring {

    public static void main(String[] args) {
        String filePath = "/Users/sunkumar2/Desktop/d/FileRead2.txt";
        Path path = Path.of(filePath);

        try {
            String lastLine = Files.lines(path, StandardCharsets.UTF_8)
                    .reduce((first, second) -> second)
                    .orElse(null);

            if (lastLine != null) {
                // Extract a substring from the last line (footer), e.g., from index 7 to 10
                String substring = getSubstringAtIndex(lastLine, 7, 10);
                System.out.println("Substring from last line: " + substring);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getSubstringAtIndex(String line, int startIndex, int endIndex) {
        return (startIndex >= 0 && startIndex < line.length() && endIndex >= 0 && endIndex <= line.length()) ?
                line.substring(startIndex, endIndex) : "";
    }
}