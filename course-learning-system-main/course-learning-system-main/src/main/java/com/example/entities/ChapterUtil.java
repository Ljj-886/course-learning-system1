package com.example.entities;

public class ChapterUtil {
    public static String getSerial(String code) {
        if (code == null || code.length() != 6) {
            return "";
        }
        int part1 = Integer.parseInt(code.substring(0, 2));
        int part2 = Integer.parseInt(code.substring(2, 4));
        int part3 = Integer.parseInt(code.substring(4, 6));

        if (part2 == 0 && part3 == 0) {
            return "第" + part1 + "章";
        } else {
            return part1 + "." + part2;
        }
    }
}