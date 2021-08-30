public class Menu {

    private Lector tec;

    public Menu(){
        this.tec = new Lector();
    }

    public void menuIniciar(){
        System.out.println("");
        System.out.println("Ingrese su JID:");
        System.out.print("-> ");
        String jid= this.tec.leerLinea();
        System.out.println("Ingrese su JID:");
        System.out.print("-> ");
        String pass= this.tec.leerLinea();
        System.out.println("");

        //Configurar topologia
        Topologia topo = new Topologia();
        topo.analizarNames(topo.readJson("names-demo.txt"));
        topo.analizarTopo(topo.readJson("topo-demo.txt"));

        Nodo actualN = topo.buscarNodoJID(jid); // yeet@alumchat.xyz

        if (actualN==null){
            System.out.println("No se encontro el JID "+jid);
        }else{
            topo.calulateD(actualN);
        }



    }

    public void mandarMSN(){
        System.out.println("");
        System.out.println("Ingrese el JID destino:");
        System.out.print("-> ");
        String destino= this.tec.leerLinea();
    }

}
