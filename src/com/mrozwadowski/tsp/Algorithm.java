package com.mrozwadowski.tsp;

/**
 * Any class with the ability to solve an instance
 * of the TSP should implement this interface.
 *
 * Created by rozwad on 05.01.17.
 */
public interface Algorithm {
    Solution findPath();
}
