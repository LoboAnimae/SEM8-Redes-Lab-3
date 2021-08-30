import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Nodo {

    private String nombre;
    private String JID;
    private List<Nodo> caminoCorto;
    private Integer dist;
    private Map<Nodo, Integer> nodosAdyacentes;

    public Nodo(String nombre) {
        this.nombre = nombre;
        this.JID = "";
        this.caminoCorto = new LinkedList<>();
        this.dist = Integer.MAX_VALUE;
        this.nodosAdyacentes = new HashMap<>();
    }

    public Nodo(String nombre, String jid) {
        this.nombre = nombre;
        this.JID = jid;
        this.caminoCorto = new LinkedList<>();
        this.dist = Integer.MAX_VALUE;
        this.nodosAdyacentes = new HashMap<>();
    }

    public void addDest(Nodo destination, int distance) {
        nodosAdyacentes.put(destination, distance);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Nodo> getCaminoCorto() {
        return caminoCorto;
    }

    public void setCaminoCorto(List<Nodo> caminoCorto) {
        this.caminoCorto = caminoCorto;
        //this.printCaminoCorto();
    }

    public Integer getDist() {
        return dist;
    }

    public void setDist(Integer dist) {
        this.dist = dist;
    }

    public Map<Nodo, Integer> getNodosAdyacentes() {
        return nodosAdyacentes;
    }

    public void setNodosAdyacentes(Map<Nodo, Integer> nodosAdyacentes) {
        this.nodosAdyacentes = nodosAdyacentes;
    }

    public void printCaminoCorto(){
        System.out.print( this.nombre+" -> ");
        for (Nodo n: this.caminoCorto){
            System.out.print(n.getNombre()+" - ");
        }
        System.out.println("");
    }
}
