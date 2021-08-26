import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

public class Utilidades {

    public Grafo calculateShortestPathFromSource(Grafo graph, Nodo source) {
        source.setDist(0);

        Set<Nodo> settledNodes = new HashSet<>();
        Set<Nodo> unsettledNodes = new HashSet<>();

        unsettledNodes.add(source);

        while (unsettledNodes.size() != 0) {
            Nodo currentNode = getLowestDistanceNode(unsettledNodes);
            unsettledNodes.remove(currentNode);
            for (Map.Entry< Nodo, Integer> adjacencyPair:
                    currentNode.getNodosAdyacentes().entrySet()) {
                Nodo adjacentNode = adjacencyPair.getKey();
                Integer edgeWeight = adjacencyPair.getValue();
                if (!settledNodes.contains(adjacentNode)) {
                    this.calculateMinimumDistance(adjacentNode, edgeWeight, currentNode);
                    unsettledNodes.add(adjacentNode);
                }
            }
            settledNodes.add(currentNode);
        }
        return graph;
    }

    public Nodo getLowestDistanceNode(Set < Nodo > unsettledNodes) {
        Nodo lowestDistanceNode = null;
        int lowestDistance = Integer.MAX_VALUE;
        for (Nodo node: unsettledNodes) {
            int nodeDistance = node.getDist();
            if (nodeDistance < lowestDistance) {
                lowestDistance = nodeDistance;
                lowestDistanceNode = node;
            }
        }
        return lowestDistanceNode;
    }

    public void calculateMinimumDistance(Nodo evaluationNode, Integer edgeWeigh, Nodo sourceNode) {
        Integer sourceDistance = sourceNode.getDist();
        if (sourceDistance + edgeWeigh < evaluationNode.getDist()) {
            evaluationNode.setDist(sourceDistance + edgeWeigh);
            LinkedList<Nodo> shortestPath = new LinkedList<>(sourceNode.getCaminoCorto());
            shortestPath.add(sourceNode);
            evaluationNode.setCaminoCorto(shortestPath);
        }
    }

}
