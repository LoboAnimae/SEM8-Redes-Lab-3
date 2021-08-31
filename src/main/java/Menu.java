import java.awt.desktop.SystemEventListener;

public class Menu {

    private Lector tec;
    private Topologia topo;
    private ClienteXMPP cli;

    public Menu(){
        this.tec = new Lector();
        this.topo = Topologia.getInstance();
        this.cli = new ClienteXMPP().getInstance();
    }

    public void menuIniciar(){
        System.out.println("");
        System.out.println("Ingrese su JID:");
        System.out.print("-> ");
        String jid= this.tec.leerLinea();
        //String jid= "davisgt@jabber.hot-chilli.net";
        System.out.println("Ingrese su contraseña:");
        System.out.print("-> ");
        String pass= this.tec.leerLinea();
        System.out.println("");

        //Configurar topologia
        topo.analizarNames(topo.readJson("names-demo.txt"));

        Nodo actualN = topo.buscarNodoJID(jid);

       if (actualN==null){
            System.out.println("No se encontro el JID "+jid);
       }else{
           String[] chapus = jid.split("@");
           topo.setActualN(actualN);
           ClienteXMPP.getInstance().iniciarSesion(chapus[0], pass,"alumchat.xyz");
           topo.analizarTopo(topo.readJson("topo-demo.txt"));
           topo.calulateD(actualN);

           for (Nodo n: Topologia.getInstance().getNodos()){
               n.arreglarCamino();
           }
       }
       String salir = "";

       while (!salir.equals("2")){
           System.out.println("Opciones:");
           System.out.println("1 - Mandar Mensaje.");
           System.out.println("2 - Salir.");
           System.out.print("-> ");
           salir = this.tec.leerLinea();
           switch (salir){
               case "1":
                   mandarMSN();
                   break;
               case "2":
                   System.out.println("Bye! :D");
                    break;
               default:
                   break;
           }
       }
    }

    public void mandarMSN(){
        System.out.println("");
        System.out.println("Ingrese el JID destino:");
        System.out.print("-> ");
        String destino= this.tec.leerLinea();

        System.out.println("");
        System.out.println("Ingrese el mensaje:");
        System.out.print("-> ");
        String txt= this.tec.leerLinea();

        Nodo nodoD = topo.buscarNodoJID(destino); //destino davisgt2@jabb3r.org

        String msn = this.cli.armarMensaje(nodoD, txt);

        this.cli.enrutarMSN(msn);

    }

}
