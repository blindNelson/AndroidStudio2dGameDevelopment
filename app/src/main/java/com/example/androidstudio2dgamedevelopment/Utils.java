package com.example.androidstudio2dgamedevelopment;

public class Utils {

    public static double getDistanceBeetwenObjects(double p1x, double p1y, double p2x, double p2y) {
            return pitagoras(p1x-p2x,p1y-p2y);
    }

    public static double pitagoras(double a, double b) {
        return Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));
    }
}
