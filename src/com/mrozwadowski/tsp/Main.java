package com.mrozwadowski.tsp;

import com.mrozwadowski.tsp.aco.AntColony;

import java.io.*;
import java.util.Scanner;

public class Main {
    Graph graph;
    PrintStream out;
    Scanner scanner;

    Main(InputStream in, PrintStream out) {
        scanner = new Scanner(in);
        this.out = out;
    }

    void readNumCities() throws Exception {
        int numCities = scanner.nextInt();
        if (numCities < 2) {
            throw new Exception("Należy podać co najmniej dwa punkty!");
        }
        graph = new Graph();
    }

    void readCities() {
        while (scanner.hasNextInt()) {
            graph.addCity(new City(scanner.nextInt(), scanner.nextInt(), scanner.nextInt()));
        }
    }

    void findAndPrintResults() {
        AntColony colony = new AntColony(graph);
        Solution solution = colony.findPath();

        System.out.println("Result: "+solution.getLength());

        out.println(colony.getNumCities());
        for (City city: solution.getCities()) {
            out.println(city.getNum()+" "+city.getX()+" "+city.getY());
        }
    }

    public static void main(String[] args) throws Exception {
        /*
        If you run the program with an argument, the instance will be loaded
        from a file in `instances` directory and a solution will be written
        to `solutions`. Otherwise, standard I/O will be used.
        */
        InputStream in = System.in;
        PrintStream out = System.out;

        if (args.length == 1) {
            String file = args[0];
            in = new FileInputStream("instances/"+file);
            out = new PrintStream("solutions/"+file);
        }

        Main app = new Main(in, out);

        app.readNumCities();
        app.readCities();
        app.findAndPrintResults();
    }
}
