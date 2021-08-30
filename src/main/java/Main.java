
public class Main {
    public static void main(String[] args){

        ClienteXMPP cli = new ClienteXMPP();
        Utilidades uti = new Utilidades();

        //cli.mensaje(cli.conectar());
        //uti.test();
        //cli.CrearUsuario(cli.conectar());


        Menu m = new Menu();
        m.configuararTopo();
    }

}
