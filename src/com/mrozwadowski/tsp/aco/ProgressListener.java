package com.mrozwadowski.tsp.aco;

import com.mrozwadowski.tsp.Solution;

/**
 * Created by rozwad on 13.01.17.
 */

public interface ProgressListener {
    void iterationEnd(int iterationNumber, int totalIterations, Solution iterationBest);
    void iterationProgress(int stepNumber, int totalSteps);
}
