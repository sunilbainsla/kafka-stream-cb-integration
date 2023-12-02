package com.sunilbainsla.kafkastreampoc.controllers;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FilterRecords {

    public static void main(String[] args) {
        String filePath = "/Users/sunkumar2/Desktop/d/FileRead2.txt";

        try (Stream<String> stream = Files.lines(Paths.get(filePath), StandardCharsets.UTF_8)) {
            // Filter records containing the word "sunil", not containing the value "3000000",
            // and where the substring from index 7 to 10 is not equal to "LL"
            List<String> filteredLines = stream.filter(line -> {
                return line.contains("sunil") &&
                        !line.contains("3000000") &&
                        !getSubstringAtIndex(line, 7, 10).equals("LL");
            }).collect(Collectors.toList());

            // Print the filtered lines
            filteredLines.forEach(System.out::println);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getSubstringAtIndex(String line, int startIndex, int endIndex) {
        if (startIndex >= 0 && startIndex < line.length() && endIndex >= 0 && endIndex <= line.length()) {
            return line.substring(startIndex, endIndex);
        } else {
            return "";
        }
    }
}
