package com.mrozwadowski.tsp;

import com.mrozwadowski.tsp.aco.AntColony;

import java.io.*;
import java.util.Scanner;

/**
 *
 * @author Jakub
 */
public class Tester {
    double values[] = new double[6];
    int iters = 50;
    
    static int loop = 5;
    
    static double alpha[] = {1.0, 2.0};
    static double beta[] = {2.0, 6.0};
    static double tau0[] = {0.1, 1.0};
    static double rho[] = {0.1, 0.8};
    static double Q[] = {100, 500};
    static double ants[] = {0.2, 0.9};

    public static void main(String[] args) throws Exception {

        Graph graph = Graph.fromFile(new File("C:\\Users\\Jakub\\Documents\\GitHub\\tsp-java\\instances\\berlin52.txt")); //app.readNumCities();
        //app.readCities();
        for (double a = alpha[0]; a <= alpha[1]; a += (alpha[1]-alpha[0])/loop){
            for (double b = beta[0]; b <= beta[1]; b += (beta[1]-beta[0])/loop){
                for (double t = tau0[0]; t <= tau0[1]; t += (tau0[1]-tau0[0])/loop){
                for (double r = rho[0]; r <= rho[1]; r += (rho[1]-rho[0])/loop){
                for (double q = Q[0]; q <= Q[1]; q += (Q[1]-Q[0])/loop){
                for (double an = ants[0]; an <= ants[1]; an += (ants[1]-ants[0])/loop){
                    AntColony aco = new AntColony(graph);

                    aco.setParams(a, b, t, r, q, an, loop);
                    Solution sol = aco.findPath();
                    System.out.println(sol.getLength() + " a=" + a + " b=" + b + " t=" + 
                            t + " r=" + r + " q=" + q + " ants=" + an);
                }
            }
            }
            }
            }
        }
    }
}
