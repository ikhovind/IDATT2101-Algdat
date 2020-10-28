import com.sothawo.mapjfx.*;
import javafx.fxml.FXML;
import javafx.scene.paint.Color;

import java.io.IOException;

public class Controller {
    @FXML
    private MapView mapView;
    private CoordinateLine shortestPath;
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
        shortestPath = new CoordinateLine(Graph.result);
        shortestPath.setColor(Color.CHOCOLATE);
        mapView.setCenter(new Coordinate(0.0, 0.0));
        mapView.setZoom(1.0);
        mapView.addCoordinateLine(shortestPath);
        shortestPath.visibleProperty().set(true);
    }
}
