public class Menu {

    private Lector tec;
    private Topologia topo;
    private ClienteXMPP cli;

    public Menu(){
        this.tec = new Lector();
        this.topo = Topologia.getInstance();
        this.cli = new ClienteXMPP();
    }

    public void menuIniciar(){
        System.out.println("");
        System.out.println("Ingrese su JID:");
        System.out.print("-> ");
        String jid= this.tec.leerLinea();
        System.out.println("Ingrese su contraseña:");
        System.out.print("-> ");
        String pass= this.tec.leerLinea();
        System.out.println("");

        //Configurar topologia
        topo.analizarNames(topo.readJson("names-demo.txt"));
        topo.analizarTopo(topo.readJson("topo-demo.txt"));

        /*
        Nodo actualN = topo.buscarNodoJID(jid);

       if (actualN==null){
            System.out.println("No se encontro el JID "+jid);
        }else{
            topo.calulateD(actualN);
            topo.setActualN(actualN);
        }*/

        Nodo actualN = topo.buscarNodoJID("bar@alumchat.xyz"); //
        topo.calulateD(actualN);
        topo.setActualN(actualN);
    }

    public void mandarMSN(){
        System.out.println("");
        System.out.println("Ingrese el JID destino:");
        System.out.print("-> ");
        String destino= this.tec.leerLinea();

        System.out.println("");
        System.out.println("Ingrese el mensaje:");
        System.out.print("-> ");
        String msn= this.tec.leerLinea();

        Nodo actualD = topo.buscarNodoJID("yeet@alumchat.xyz");

        this.cli.mandarMensaje(actualD, "Salu2");


    }

}
