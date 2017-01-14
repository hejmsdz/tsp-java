package com.mrozwadowski.tsp.aco;

import com.mrozwadowski.tsp.City;
import com.mrozwadowski.tsp.Graph;
import com.mrozwadowski.tsp.Solution;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AntColony {
    // metaheuristic parameters
    double alpha = 1; // pheromone importance
    double beta = 5; // distance importance
    double tau0 = 1; // initial pheromone amount
    double rho = 0.5; // evaporation coefficient
    double Q = 500; // global update
    double antsRatio = 0.8;
    int numIterations = 100;

    private Graph graph;
    private double phero[][];

    private ProgressListener listener = null;

    public AntColony(Graph graph) {
        this.graph = graph;
        this.phero = new double[getNumCities()][getNumCities()];

        // initialize the pheromone matrix
        for (int i = 0; i < getNumCities(); i++) {
            for (int j = 0; j < getNumCities(); j++) {
                setPhero(i, j, tau0);
            }
        }
    }

    public void setParams(double alpha, double beta, double tau0, double rho, double Q, double antsRatio, int numIterations) {
        this.alpha = alpha;
        this.beta = beta;
        this.tau0 = tau0;
        this.rho = rho;
        this.Q = Q;
        this.antsRatio = antsRatio;
        this.numIterations = numIterations;
    }

    public void setListener(ProgressListener listener) {
        this.listener = listener;
    }

    public Graph getGraph() {
        return graph;
    }

    public int getNumCities() {
        return graph.getNumCities();
    }

    /**
     * Display the pheromone matrix for debugging purposes.
     */
    public void showPhero() {
        for (int i=0; i<getNumCities(); i++) {
            for (int j=0; j<getNumCities(); j++) {
                System.out.printf("%.3f\t", getPhero(i, j));
            }
            System.out.println();
        }
    }

    /**
     * Decrease the pheromone amount on all edges by rho.
     */
    public void evaporate() {
        for (int i = 0; i < getNumCities(); i++) {
            for (int j = i; j < getNumCities(); j++) {
                double value = getPhero(i, j);
                value *= 1 - rho;
                setPhero(i, j, value);
            }
        }
    }

    public double getPhero(int i, int j) {
        return phero[i][j];
    }

    public double getPhero(City i, City j) {
        return getPhero(i.getNum() - 1, j.getNum() - 1);
    }

    public void setPhero(int i, int j, double value) {
        phero[i][j] = phero[j][i] = value;
    }

    public void setPhero(City i, City j, double value) {
        setPhero(i.getNum() - 1, j.getNum() - 1, value);
    }

    /**
     * For a given list of ants, update the pheromone matrix
     * according to the lengths of the ants' routes
     * and return the one whose path was the shortest.
     */
    private Ant globalUpdate(List<Ant> ants) {
        double shortest = Double.POSITIVE_INFINITY;
        Ant bestAnt = null;

        for (Ant ant: ants) {
            double add = Q / ant.getRouteLength();
            City previous = null;
            for (City city : ant.getRoute()) {
                if (previous == null) {
                    previous = city;
                    continue;
                }

                double value = getPhero(previous, city);
                value += add;
                setPhero(previous, city, value);
                previous = city;
            }

            if (ant.getRouteLength() < shortest) {
                shortest = ant.getRouteLength();
                bestAnt = ant;
            }
        }

        return bestAnt;
    }

    /**
     * Compute the shortest path using Ant Colony Optimization metaheuristic.
     */
    public Solution findPath() {
        Random rand = new Random();

        List<Ant> ants;
        Ant best = null;

        for (int k = 1; k <= numIterations; k++) {
            // generate ants
            ants = new ArrayList<>();
            int numAnts = (int)(antsRatio * getNumCities());
            for (int i = 0; i < numAnts; i++) {
                City starting = graph.randomCity(rand);
                ants.add(new Ant(this, starting));
            }

            // every ant builds a partial solution
            for (int i = 0; i < getNumCities(); i++) {
                if (i % 10 == 0) {
                    // display this information every 100 steps,
                    // because too much output would be a mess
                    if (listener != null) listener.iterationProgress(i, getNumCities());
                }

                evaporate();
                for (Ant ant: ants) {
                    ant.proceed();
                }
            }

            Ant iterationBest = globalUpdate(ants);
            Solution solution = new Solution(iterationBest.getRouteLength(), iterationBest.getRoute());
            if (listener != null) listener.iterationEnd(k, numIterations, solution);

            if (best == null || iterationBest.getRouteLength() < best.getRouteLength()) {
                best = iterationBest;
            }
        }

        return new Solution(best.getRouteLength(), best.getRoute());
    }
}
