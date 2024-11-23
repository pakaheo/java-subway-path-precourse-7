package subway.domain.search;

import java.util.List;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import subway.domain.StationRepository;
import subway.domain.TimeRepository;

public class ShortestTimePathSearch implements PathSearch {
    @Override
    public List<String> search(String departure, String arrival) {
        WeightedMultigraph<String, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        setStationVertex(graph);
        setTimeWeightEdge(graph);
        return findOptimizePath(graph, departure, arrival);
    }

    private List<String> findOptimizePath(WeightedMultigraph<String, DefaultWeightedEdge> graph, String departure,
                                          String arrival) {
        DijkstraShortestPath<String, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        return dijkstraShortestPath.getPath(departure, arrival).getVertexList();
    }

    private void setStationVertex(WeightedMultigraph<String, DefaultWeightedEdge> graph) {
        StationRepository.stations().forEach(station -> graph.addVertex(station.getName()));
    }

    private void setTimeWeightEdge(WeightedMultigraph<String, DefaultWeightedEdge> graph) {
        TimeRepository.times().forEach(
                time -> graph.setEdgeWeight(graph.addEdge(time.getDeparture(), time.getArrival()),
                        time.getMinute())
        );
    }
}
