import org.json.JSONArray;
import org.json.JSONObject;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class Topologia {

    private ArrayList<Nodo> nodos;
    private Grafo red;

    public Topologia(){
        this.nodos = new ArrayList<>();
        this.red = new Grafo();
    }

    public JSONObject readJson(String archivo){
        String json = "";
        try {

            File myObj = new File("src\\main\\resources\\"+archivo);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                json = json + data;
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        JSONObject jo = new JSONObject(json);
        //System.out.println(jo.toString()); //Mostrar JSON
        return jo;
    }

    public void analizarNames(JSONObject topo){
        //obtenemos el config
        JSONObject config = topo.getJSONObject("config");

        //obtenemos las keys existentes "nombres de nodos"
        Iterator<String> keys = config.keys();

        while (keys.hasNext()){

            String key = keys.next();

            // Usuario que representa el nodo
            String jid = config.getString(key);

            //System.out.println(key+" : "+jid);
            Nodo n = new Nodo(key, jid);
            this.nodos.add(n);
        }
        for (Nodo n: this.nodos){
            this.red.addNode(n);
        }
    }

    public void analizarTopo(JSONObject topo){
        //obtenemos el config
        JSONObject config = topo.getJSONObject("config");
        //obtenemos las keys existentes
        Iterator<String> keys = config.keys();

        while (keys.hasNext()){
            String key = keys.next();
            //System.out.print(key+" : ");
            Nodo prin = this.buscarNodo(key);

            // obtenemos el array de cada key "cada nodo"
            JSONArray nodo = config.getJSONArray(key);

            Iterator iterator = nodo.iterator();
            while(iterator.hasNext()) {
                String name = (String) iterator.next();
                //System.out.print(name);
                //Acá estan las conexiones de cada nodo

                Nodo cnn = this.buscarNodo(name);
                if (cnn !=null)
                    prin.addDest(cnn, 1);
                else
                    System.out.println("no se encontro el nodo");
            }
            //System.out.println("");
        }
    }

    public void calulateD(Nodo actualN){

        Utilidades uti = new Utilidades();

        this.red = uti.dijkstra(red, actualN);

/*        for (Nodo n: this.red.getNodos()){
            this.buscarNodo(n.getNombre()).printCaminoCorto();
        }*/
    }

    public Nodo buscarNodo(String nombre){
        for (Nodo n: this.nodos){
            if (n.getNombre().equals(nombre))
                return n;
        }
        return null;
    }
    public Nodo buscarNodoJID(String JID){
        for (Nodo n: this.nodos){
            if (n.getNombre().equals(JID))
                return n;
        }
        return null;
    }
}
