package com.mrozwadowski.tsp;

import java.util.List;

/**
 * Represents a found path.
 *
 * Created by rozwad on 05.01.17.
 */
public class Solution {
    private double length;
    private List<City> cities;

    public Solution(double length, List<City> cities) {
        this.length = length;
        this.cities = cities;
    }

    public double getLength() {
        return length;
    }

    public List<City> getCities() {
        return cities;
    }
}
