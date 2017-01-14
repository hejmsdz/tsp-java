package com.mrozwadowski.tsp;

import com.mrozwadowski.tsp.aco.AntColony;
import com.mrozwadowski.tsp.aco.ProgressListener;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

/**
 * Created by rozwad on 13.01.17.
 */
public class Controller implements ProgressListener {

    @FXML
    Canvas citiesCanvas, routeCanvas, pheroCanvas;

    @FXML
    Button run;

    @FXML
    ProgressBar iterationsProgress, stepsProgress;

    @FXML
    Spinner<Double> alpha, beta, phero0, evap, q, ants;

    @FXML
    Spinner<Integer> iterations;

    @FXML
    ListView<Label> itBests;

    private Stage mainStage;

    private double scale = 0.5;

    Graph graph = null;
    AntColony antColony = null;

    public void initialize() {
        itBests.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            drawPath((Solution)newValue.getUserData());
        });
    }

    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    @Override
    public void iterationEnd(int iterationNumber, int totalIterations, Solution iterationBest) {
        double frac = (double)iterationNumber/totalIterations;
        Label label = new Label(""+(int)iterationBest.getLength());

        label.setUserData(iterationBest);

        Platform.runLater(() -> {
            iterationsProgress.setProgress(frac);
            itBests.getItems().add(label);
            drawPheromones();
        });
    }

    @Override
    public void iterationProgress(int stepNumber, int totalSteps) {
        Platform.runLater(() -> {
            stepsProgress.setProgress((double)stepNumber/totalSteps);
        });
    }

    private void resetProgress() {
        Platform.runLater(() -> {
            iterationsProgress.setProgress(0);
            stepsProgress.setProgress(0);
        });
    }

    private void setParamsDisable(boolean val) {
        alpha.setDisable(val);
        beta.setDisable(val);
        phero0.setDisable(val);
        evap.setDisable(val);
        q.setDisable(val);
        ants.setDisable(val);
        iterations.setDisable(val);
    }

    private void highlightBestLabel() {
        Label best = null;
        double shortest = Double.POSITIVE_INFINITY;
        for (Label label: itBests.getItems()) {
            Solution solution = (Solution)label.getUserData();
            if (solution.getLength() < shortest) {
                best = label;
            }
        }

        final Label best1 = best;

        Platform.runLater(() -> {
            best1.setStyle("-fx-text-fill: #5FAD56");
            itBests.scrollTo(best1);
            itBests.getSelectionModel().select(best1);
        });
    }

    private void drawCities() {
        GraphicsContext gc = citiesCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, citiesCanvas.getWidth(), citiesCanvas.getHeight());
        gc.setFill(Color.BLACK);

        Platform.runLater(() -> {
            for (City city : graph.getCities()) {
                gc.fillArc(city.getX() * scale, city.getY() * scale, 3, 3, 0, 360, ArcType.OPEN);
            }
        });
    }

    private void drawPheromones() {
        GraphicsContext gc = pheroCanvas.getGraphicsContext2D();

        Platform.runLater(() -> {
            gc.clearRect(0, 0, pheroCanvas.getWidth(), pheroCanvas.getHeight());

            for (City city1 : graph.getCities()) {
                for (City city2 : graph.getCities()) {
                    if (city1.getNum() <= city2.getNum()) continue;

                    double phero = antColony.getPhero(city1, city2);
                    double opacity = phero / (2 * (1+phero));
                    gc.setStroke(Color.color(0.13, 0.52, 0.65, opacity));
                    gc.strokeLine(city1.getX()*scale, city1.getY()*scale, city2.getX()*scale, city2.getY()*scale);
                }
            }
        });
    }

    private void drawPath(Solution solution) {
        GraphicsContext gc = routeCanvas.getGraphicsContext2D();

        Platform.runLater(() -> {
            gc.clearRect(0, 0, routeCanvas.getWidth(), routeCanvas.getHeight());
            gc.setFill(Color.color(0.37, 0.68, 0.34));

            double lastX = -1, lastY = -1;
            double firstX = -1, firstY = -1;
            boolean first = true;
            for (City city : solution.getCities()) {
                double x = city.getX() * scale;
                double y = city.getY() * scale;
                if (first) {
                    firstX = x;
                    firstY = y;
                    first = false;
                } else {
                    gc.strokeLine(lastX, lastY, x, y);
                }
                lastX = x;
                lastY = y;
            }
            gc.strokeLine(firstX, firstY, lastX, lastY);
        });
    }

    public void openInstance() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File selectedFile = fileChooser.showOpenDialog(mainStage);

        if (selectedFile != null) {
            try {
                graph = Graph.fromFile(selectedFile);
                int maxCoord = 0;
                for (City city: graph.getCities()) {
                    int coord = Math.max(city.getX(), city.getY());
                    if (coord > maxCoord) {
                        maxCoord = coord;
                    }
                }
                scale = 500.0/maxCoord;

            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            drawCities();
            pheroCanvas.getGraphicsContext2D().clearRect(0, 0, pheroCanvas.getWidth(), pheroCanvas.getHeight());
            routeCanvas.getGraphicsContext2D().clearRect(0, 0, routeCanvas.getWidth(), routeCanvas.getHeight());

            run.setDisable(false);
        }
    }

    public void runAlgorithm() {
        run.setDisable(true);
        setParamsDisable(true);
        itBests.getItems().clear();

        antColony = new AntColony(graph);
        antColony.setListener(this);

        antColony.setParams(alpha.getValue(), beta.getValue(), phero0.getValue(), evap.getValue(), q.getValue(), ants.getValue(), iterations.getValue());

        Thread t = new Thread(() -> {
            Solution solution = antColony.findPath();

            System.out.println((int)solution.getLength());
            drawPath(solution);

            run.setDisable(false);
            setParamsDisable(false);
            resetProgress();
            highlightBestLabel();
        });
        t.start();
    }
}
