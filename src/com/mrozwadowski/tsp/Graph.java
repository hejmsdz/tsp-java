package com.mrozwadowski.tsp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Graph {
    private List<City> cities;

    Graph() {
        cities = new ArrayList<>();
    }

    public static Graph fromFile(File file) throws IOException {
        Scanner scanner = new Scanner(file);
        Graph graph = new Graph();

        int numCities = scanner.nextInt();
        if (numCities < 2) {
            throw new IOException("Expected at least two points!");
        }

        while (scanner.hasNextInt()) {
            int num = scanner.nextInt();
            int x = scanner.nextInt();
            int y = scanner.nextInt();

            graph.addCity(new City(num, x, y));
        }

        return graph;
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
