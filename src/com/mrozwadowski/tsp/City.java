package com.mrozwadowski.tsp;

public class City {
    private int num, x, y;

    public City(int num, int x, int y) {
        this.num = num;
        this.x = x;
        this.y = y;
    }

    public String toString() {
        return "<City #"+num+" ("+x+","+y+")>";
    }

    public int getNum() {
        return num;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public double distance(City other) {
        return Math.hypot(x-other.x, y-other.y);
    }
}
