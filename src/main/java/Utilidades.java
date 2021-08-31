// Implementación basada de Dijkstra en https://www.baeldung.com/java-dijkstra
import java.util.*;

import static org.junit.Assert.assertTrue;

public class Utilidades {

    public Grafo dijkstra(Grafo red, Nodo origen) {
        origen.setDist(0);

        Set<Nodo> settledNodes = new HashSet<>();
        Set<Nodo> unsettledNodes = new HashSet<>();

        unsettledNodes.add(origen);

        while (unsettledNodes.size() != 0) {
            Nodo currentNode = calcularCortaDist(unsettledNodes);
            unsettledNodes.remove(currentNode);
            for (Map.Entry< Nodo, Integer> adjacencyPair:
                    currentNode.getNodosAdyacentes().entrySet()) {
                Nodo adjacentNode = adjacencyPair.getKey();
                Integer edgeWeight = adjacencyPair.getValue();
                if (!settledNodes.contains(adjacentNode)) {
                    this.calcularMinimaDist(adjacentNode, edgeWeight, currentNode);
                    unsettledNodes.add(adjacentNode);
                }
            }
            settledNodes.add(currentNode);
        }
        return red;
    }

    public Nodo calcularCortaDist(Set<Nodo> unsettledNodes) {
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

    public void calcularMinimaDist(Nodo evaluationNode, Integer edgeWeigh, Nodo sourceNode) {
        Integer sourceDistance = sourceNode.getDist();
        if (sourceDistance + edgeWeigh < evaluationNode.getDist()) {
            evaluationNode.setDist(sourceDistance + edgeWeigh);
            LinkedList<Nodo> shortestPath = new LinkedList<>(sourceNode.getCaminoCorto());
            shortestPath.add(sourceNode);
            evaluationNode.setCaminoCorto(shortestPath);
        }
    }

    /*public void test(){

        Nodo nodeA = new Nodo("A");
        Nodo nodeB = new Nodo("B");
        Nodo nodeC = new Nodo("C");
        Nodo nodeD = new Nodo("D");
        Nodo nodeE = new Nodo("E");
        Nodo nodeF = new Nodo("F");

        nodeA.addDest(nodeB, 10);
        nodeA.addDest(nodeC, 15);

        nodeB.addDest(nodeD, 12);
        nodeB.addDest(nodeF, 15);

        nodeC.addDest(nodeE, 10);

        nodeD.addDest(nodeE, 2);
        nodeD.addDest(nodeF, 1);

        nodeF.addDest(nodeE, 5);

        Grafo graph = new Grafo();

        graph.addNode(nodeA);
        graph.addNode(nodeB);
        graph.addNode(nodeC);
        graph.addNode(nodeD);
        graph.addNode(nodeE);
        graph.addNode(nodeF);

        graph = this.dijkstra(graph, nodeA);

        List<Nodo> shortestPathForNodeB = Arrays.asList(nodeA);
        List<Nodo> shortestPathForNodeC = Arrays.asList(nodeA);
        List<Nodo> shortestPathForNodeD = Arrays.asList(nodeA, nodeB);
        List<Nodo> shortestPathForNodeE = Arrays.asList(nodeA, nodeB, nodeD);
        List<Nodo> shortestPathForNodeF = Arrays.asList(nodeA, nodeB, nodeD);
        //assertTrue(false);
        //while (true){
            for (Nodo node : graph.getNodos()) {
                switch (node.getNombre()) {
                    case "B":
                        node.printCaminoCorto();
                        assertTrue(node.getCaminoCorto().equals(shortestPathForNodeB));
                        break;
                    case "C":
                        node.printCaminoCorto();
                        assertTrue(node.getCaminoCorto().equals(shortestPathForNodeC));
                        break;
                    case "D":
                        node.printCaminoCorto();
                        assertTrue(node.getCaminoCorto().equals(shortestPathForNodeD));
                        break;
                    case "E":
                        node.printCaminoCorto();
                        assertTrue(node.getCaminoCorto().equals(shortestPathForNodeE));
                        break;
                    case "F":
                        node.printCaminoCorto();
                        assertTrue(node.getCaminoCorto().equals(shortestPathForNodeF));
                        break;
                }
            }
        //}
        System.out.println("_______________");
    }*/

}
