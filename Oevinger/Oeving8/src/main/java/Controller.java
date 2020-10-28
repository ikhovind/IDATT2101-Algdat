import com.sothawo.mapjfx.*;
import javafx.fxml.FXML;
import javafx.scene.paint.Color;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Controller {
    @FXML
    private MapView mapView;
    private ArrayList<Marker> markers = new ArrayList<>();
    private CoordinateLine shortestPath;
    public Controller() {
    }

    public void initMapAndControls(Projection projection) throws IOException {
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
        shortestPath = loadCoordinateLine(getClass().getResource("/shortestPath.csv")).orElse(new CoordinateLine
                ()).setColor(Color.MAGENTA);

        mapView.setCenter(new Coordinate(0.0, 0.0));
        mapView.setZoom(1.0);
        mapView.addCoordinateLine(shortestPath);
        shortestPath.visibleProperty().set(true);
    }

    private Optional<CoordinateLine> loadCoordinateLine(URL url) {
        try (
                Stream<String> lines = new BufferedReader(
                        new InputStreamReader(url.openStream(), StandardCharsets.UTF_8)).lines()
        ) {
            return Optional.of(new CoordinateLine(
                    lines.map(line -> line.split(";")).filter(array -> array.length == 2)
                            .map(values -> new Coordinate(Double.valueOf(values[0]), Double.valueOf(values[1])))
                            .collect(Collectors.toList())));
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
