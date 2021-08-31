import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.chat2.IncomingChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smackx.iqregister.AccountManager;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.Jid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.jid.parts.Localpart;
import org.jxmpp.stringprep.XmppStringprepException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ClienteXMPP {

    private static ClienteXMPP single_instance = null;
    private AbstractXMPPConnection cnn;
    private ArrayList<ChatRoom> chats;

    public static ClienteXMPP getInstance() {
        if (single_instance == null)
            single_instance = new ClienteXMPP();
        return single_instance;
    }

    public ClienteXMPP(){
        this.chats = new ArrayList<>();
    }

    public void iniciarSesion(String usr, String pass, String xmpp){
        // Create a connection and login to the example.org XMPP service.
        try {
            AbstractXMPPConnection cnn = new XMPPTCPConnection(usr, pass, xmpp);
            cnn.connect().login();
            this.cnn = cnn;

        } catch (SmackException | IOException | XMPPException | InterruptedException e) {
        }
    }

    public AbstractXMPPConnection conectar(){
        // Create a connection and login to the example.org XMPP service.
        try {
            AbstractXMPPConnection cnn = new XMPPTCPConnection("davisgt", "bananasenpijama", "alumchat.xyz");
            cnn.connect();
            return cnn;

        } catch (SmackException | IOException | XMPPException | InterruptedException e) {
        }
        return null;
    }

    public void mensaje(AbstractXMPPConnection connection){
        try{
            ChatManager chatManager = ChatManager.getInstanceFor(connection);
            EntityBareJid jid = JidCreate.entityBareFrom("davisgt2@jabb3r.org");
            Chat chat = chatManager.chatWith(jid);

            chat.send("Saludos desde guatemala2");

            chatManager.addIncomingListener(new IncomingChatMessageListener() {
                @Override
                public void newIncomingMessage(EntityBareJid from, Message message, Chat chat) {
                    System.out.println("New message from " + from + ": " + message.getBody());
                }
            });
            while (true){}

        }catch (XmppStringprepException e){

        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void enrutar(String receptor, String msn){
        try{
            ChatManager chatManager = ChatManager.getInstanceFor(this.cnn);
            EntityBareJid jid = JidCreate.entityBareFrom(receptor);
            Chat chat = chatManager.chatWith(jid);

            chat.send(msn);

            chatManager.addIncomingListener(new IncomingChatMessageListener() {
                @Override
                public void newIncomingMessage(EntityBareJid from, Message message, Chat chat) {
                    System.out.println("New message from " + from + ": " + message.getBody());
                }
            });
            while (true){}

        }catch (XmppStringprepException e){

        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String armarMensaje(Nodo dest, String msn){
        String pakage = "";

        pakage = pakage + Topologia.getInstance().getActualN().getNombre() +" ø ";

        pakage = pakage + dest.getNombre() + " ø ";

        pakage = pakage + dest.caminoCorto()+" ø ";

        pakage = pakage + dest.distCaminoCorto()+" ø ";

        pakage = pakage + Topologia.getInstance().listaNodos() + " ø ";

        pakage = pakage + msn;

/*        System.out.println("_____________________________");
        System.out.println(pakage);
        System.out.println("_____________________________");*/

        return pakage;
    }

    public void enrutarMSN(String msn){
        String[] pakage = msn.split("ø");

        Nodo camino = Topologia.getInstance().buscarNodo(pakage[1].trim());

/*        if (camino.getNombre().equals(Topologia.getInstance().getActualN().getNombre())){
            //Mostar Mensaje
            System.out.println("llego mensaje1");
        }*/

        if (camino.getCaminoCorto().size()==1){
            System.out.println("____________NUEVO_MENSAJE_________________");
            Nodo emisario = Topologia.getInstance().buscarNodo(pakage[0].trim());
            System.out.println("De: " +emisario.getJID());
            System.out.println("Mensaje:");
            System.out.println(pakage[5]);
            System.out.println("________________FIN_XD____________________");
        }else if (camino.getCaminoCorto().size()>=2){
            Nodo destino = camino.getCaminoCorto().get(1);
            this.enviarMensaje(msn, destino.getJID());
            camino.printCaminoCorto();
            System.out.println("Mensaje enrutado "+Topologia.getInstance().getActualN().getNombre() + " ->  "+pakage[1].trim()+" exitosamente!");
        }

        //this.mensaje(this.cnn);

    }

    public void agregarChat(Nodo cnn){

        ChatRoom amigo = new ChatRoom();
        amigo.setNombre(cnn.getNombre());
        amigo.setJID(cnn.getJID());
        amigo.setChat(this.crearChat(cnn.getJID()));
        this.chats.add(amigo);
    }

    public Chat crearChat(String receptor){
        System.out.println("ChatRoom lista! '"+receptor+"'");
        try{
            ChatManager chatManager = ChatManager.getInstanceFor(this.cnn);
            EntityBareJid jid = JidCreate.entityBareFrom(receptor);
            Chat chat = chatManager.chatWith(jid);

            chatManager.addIncomingListener(new IncomingChatMessageListener() {
                @Override
                public void newIncomingMessage(EntityBareJid from, Message message, Chat chat) {
                    //System.out.println("New message from " + from + ": " + message.getBody());
                    String mensaje = message.getBody();
                    String[] pakage = mensaje.split("ø");

                    System.out.println("Paquete interceptado: "+pakage[0].trim()+" -> "+pakage[1].trim()+"...");
                    ClienteXMPP.getInstance().enrutarMSN(mensaje);
                }
            });

            return chat;

        }catch (XmppStringprepException e){

        }
        return null;
    }

    public void enviarMensaje(String mensaje, String destino){
        Chat wasap=null;
        for (ChatRoom c: this.chats){
            if (c.getJID().equals(destino)){
                wasap = c.getChat();
            }
        }
        if (wasap != null){
            try{
                wasap.send(mensaje);
            } catch (SmackException.NotConnectedException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }else {
            System.out.println("Error al enviar el mensaje");
        }

    }
}
