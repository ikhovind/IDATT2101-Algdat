import com.sothawo.mapjfx.*;
import java.util.LinkedList;
import javafx.fxml.FXML;
import javafx.scene.paint.Color;

import java.io.IOException;

public class Controller {
    @FXML
    private MapView mapView;
    private CoordinateLine shortestPath;
    private CoordinateLine[] pathToStations;
    public Controller() {
    }

    public void initMapAndControls(Projection projection) {
        mapView.initializedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                afterMapIsInitialized();
            }
        });

        mapView.initialize(Configuration.builder()
                .projection(projection)
                .showZoomControls(false)
                .build());
    }

    private void afterMapIsInitialized() {
        pathToStations = new CoordinateLine[Graph.pathsToCodes.length];
        for (int i = 0; i < Graph.pathsToCodes.length; i++) {
            LinkedList<Coordinate> pathsToCode = Graph.pathsToCodes[i];
            pathToStations[i] = new CoordinateLine(pathsToCode);
            mapView.addCoordinateLine(pathToStations[i]);
            pathToStations[i].visibleProperty().set(true);
        }
        shortestPath = new CoordinateLine(Graph.result);
        shortestPath.setColor(Color.CHOCOLATE);
        mapView.setCenter(new Coordinate(0.0, 0.0));
        mapView.setZoom(1.0);
        mapView.addCoordinateLine(shortestPath);
        shortestPath.visibleProperty().set(true);
    }
}
