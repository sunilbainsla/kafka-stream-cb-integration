package com.sunilbainsla.kafkastreampoc.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Test {
    public static void main(String[] args) {
        // Sample lists
        List<SortCodeVal> list1 = prepareList1();

        List<SortCodeDBVal> list2 = prepareList2();

        // Find common values using Java Streams
        List<SortCodeVal> commonValues = list1.stream()
                .filter(val1 -> list2.stream().anyMatch(val2 -> val2.getSortCode().equals(val1.getSortCode()) && val2.getAccountNumber().equals(val1.getAccountNumber())))
                .collect(Collectors.toList());

        // Print common values
        System.out.println("Common values: " + commonValues);
    }

    private static List<SortCodeVal> prepareList1() {
        List<SortCodeVal> sortCodeList =new ArrayList<>();

        sortCodeList.add(new SortCodeVal("123456","12345678","A"));
        sortCodeList.add(new SortCodeVal("789012","90123456","B"));
        sortCodeList.add(new SortCodeVal("345678","78901234","C"));
        return sortCodeList;
    }
    private static List<SortCodeDBVal> prepareList2() {
        List<SortCodeDBVal> sortCodeList =new ArrayList<>();

        sortCodeList.add(new SortCodeDBVal("123456","12345678"));
        sortCodeList.add(new SortCodeDBVal("345678","78901234"));
        return sortCodeList;
    }
}
