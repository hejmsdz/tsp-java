package com.mrozwadowski.tsp;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Graph {
    private List<City> cities;

    Graph() {
        cities = new ArrayList<>();
    }

    public int getNumCities() {
        return cities.size();
    }

    public List<City> getCities() {
        return cities;
    }

    public void addCity(City city) {
        cities.add(city);
    }

    public City randomCity(Random rand) {
        int randomIndex = rand.nextInt(getNumCities());
        return cities.get(randomIndex);
    }
}
