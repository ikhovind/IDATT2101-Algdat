import com.sothawo.mapjfx.*;
import com.sothawo.mapjfx.event.MapViewEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.stream.Collectors;
import javafx.fxml.FXML;
import javafx.scene.paint.Color;

public class Controller {
    @FXML
    private MapView mapView;
    private CoordinateLine plottedDijkstra;
    private CoordinateLine plottedAStar;
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
        ArrayList<Coordinate>[] pathToCodes = new ArrayList[Graph.pathsToCodes.length];
        //går gjennom hver av de linkede listene med korteste vei
        for (int i = 0; i < Graph.pathsToCodes.length; i++) {
            pathToCodes[i] = new ArrayList<>();
            //korteste vei til én enkelt stasjon
            ArrayList<Node> nodesToSingleStation = Graph.pathsToCodes[i];
            //Går gjennom veien til denne stasjonen
            for (int j = 0; j < nodesToSingleStation.size(); j++) {
                Node node = nodesToSingleStation.get(j);
                Coordinate coordinate = new Coordinate(node.lat,node.longitude);
                //legger til koordinatene i arrayet vårt
                pathToCodes[i].add(coordinate);
            }
        }

        pathToStations = new CoordinateLine[Graph.pathsToCodes.length];
        for (int i = 0; i < pathToCodes.length; i++) {
            pathToStations[i] = new CoordinateLine(pathToCodes[i]);
            mapView.addCoordinateLine(pathToStations[i]);
            pathToStations[i].visibleProperty().set(true);
        }
        plottedDijkstra =
            new CoordinateLine(Graph.shortestDijkstra.stream().map(node -> new Coordinate(node.lat,
                node.longitude)).collect(Collectors.toList()));
        plottedAStar = new CoordinateLine(Graph.shortestDijkstra.stream().map(node -> new Coordinate(node.lat,
            node.longitude)).collect(Collectors.toList()));

        plottedDijkstra.setColor(Color.CHOCOLATE);
        plottedAStar.setColor(Color.CRIMSON);

        //ca midten av kartet ish
        mapView.setCenter(new Coordinate(62.016667, 14.533333));
        mapView.setZoom(5.0);

        mapView.addCoordinateLine(plottedDijkstra);
        mapView.addCoordinateLine(plottedAStar);

        plottedAStar.visibleProperty().set(true);
        plottedDijkstra.visibleProperty().set(true);
    }
}
