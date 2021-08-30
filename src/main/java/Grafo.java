import java.util.HashSet;
import java.util.Set;

public class Grafo {

    private Set<Nodo> nodos;

    public Grafo(){
        this.nodos = new HashSet<>();
    }

    public void addNode(Nodo nodo) {
        nodos.add(nodo);
    }

    public Set<Nodo> getNodos() {
        return nodos;
    }

    public void setNodos(Set<Nodo> nodos) {
        this.nodos = nodos;
    }
}
