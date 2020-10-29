import com.sothawo.mapjfx.*;
import com.sothawo.mapjfx.event.MapViewEvent;
import com.sothawo.mapjfx.event.MarkerEvent;
import java.util.EventListener;
import java.util.LinkedList;
import javafx.fxml.FXML;
import javafx.scene.paint.Color;

import java.io.IOException;
import javax.sound.sampled.LineEvent;

public class Controller {
    @FXML
    private MapView mapView;
    private CoordinateLine shortestPath;
    private CoordinateLine[] pathToStations;
    private boolean allHidden = false;
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
        mapView.addEventHandler(MapViewEvent.MAP_CLICKED,event -> {
                event.consume();
                for(int i = 0; i < pathToStations.length; i++){
                    //skjuler nodene
                    if(!allHidden && pathToStations[i].visibleProperty().get()){
                        pathToStations[i].visibleProperty().set(false);
                        if(i == pathToStations.length-1) allHidden = true;
                        return;
                    }
                    //viser nodene igjen
                    else if(allHidden && !pathToStations[i].visibleProperty().get()){
                        pathToStations[i].visibleProperty().set(true);
                        if(i == pathToStations.length-1) allHidden = false;
                        return;
                    }
                }
            }
            );
        shortestPath = new CoordinateLine(Graph.result);
        shortestPath.setColor(Color.CHOCOLATE);
        //ca midten av kartet ish
        mapView.setCenter(new Coordinate(62.016667, 14.533333));
        mapView.setZoom(5.0);
        mapView.addCoordinateLine(shortestPath);
        shortestPath.visibleProperty().set(true);
    }
}