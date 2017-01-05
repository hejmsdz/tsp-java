package com.mrozwadowski.tsp.aco;

import com.mrozwadowski.tsp.City;

import java.util.*;

public class Ant {
    private AntColony colony;
    private City starting;
    private City location = null;
    private List<City> route;
    private Set<City> remaining;
    private double routeLength;
    private Random rand;

    public Ant(AntColony colony, City starting) {
        this.colony = colony;
        route = new LinkedList<>();
        remaining = new HashSet<>();
        this.starting = starting;
        routeLength = 0;
        rand = new Random();

        remaining.addAll(colony.getGraph().getCities());
        visit(starting);
    }

    public double getRouteLength() {
        return routeLength;
    }

    public List<City> getRoute() {
        return route;
    }

    /**
     * Mark a city as visited.
     */
    private void visit(City city) {
        if (location != null) {
            routeLength += location.distance(city);
            double value = colony.getPhero(location, city);
            value += colony.tau0;
            colony.setPhero(location, city, value);
        }

        location = city;
        route.add(city);
        remaining.remove(city);
    }

    /**
     * Generate a list of visitable cities with corresponding probabilities.
     */
    public List<CityProb> nextPossibilities() {
        List<CityProb> citiesProbs = new ArrayList<>();
        double totalProb = 0;
        for (City city: remaining) {
            double pheroFactor = colony.getPhero(location, city);
            double distanceFactor = 1/location.distance(city);
            double prob = Math.pow(pheroFactor, colony.alpha) * Math.pow(distanceFactor, colony.beta);

            citiesProbs.add(new CityProb(city, prob));
            totalProb += prob;
        }

        for (CityProb cityProb: citiesProbs) {
            cityProb.prob /= totalProb;
        }

        return citiesProbs;
    }

    /**
     * Select a random city with respect to probabilities.
     */
    private City randomPick(List<CityProb> citiesProbs) {
        double x = rand.nextDouble();
        for (CityProb cityProb: citiesProbs) {
            if (x < cityProb.prob) {
                return cityProb.city;
            } else {
                x -= cityProb.prob;
            }
        }

        return citiesProbs.iterator().next().city;
    }

    /**
     * Find a next city to go to.
     */
    public void proceed() {
        City nextCity;

        if (remaining.isEmpty()) {
            // if the set of unvisited cities is empty,
            // the ant has to go back to the starting point
            nextCity = starting;
        } else {
            nextCity = randomPick(nextPossibilities());
        }

        visit(nextCity);
    }

    /**
     * This class stores data about a city that will be potentially visited
     * and associated probability.
     */
    private class CityProb {
        City city;
        double prob;

        CityProb(City city, double prob) {
            this.city = city;
            this.prob = prob;
        }
    }
}
