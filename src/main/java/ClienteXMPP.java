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
import java.util.HashMap;
import java.util.Map;

public class ClienteXMPP {


    AbstractXMPPConnection cnn;

    public AbstractXMPPConnection iniciarSesion(){
        // Create a connection and login to the example.org XMPP service.
        try {
            AbstractXMPPConnection cnn = new XMPPTCPConnection("davisgt", "bananasenpijama", "alumchat.xyz");
            cnn.connect().login();
            return cnn;

        } catch (SmackException | IOException | XMPPException | InterruptedException e) {
        }
        return null;
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

    public void CrearUsuario(AbstractXMPPConnection connection){

        try {

            Jid id = JidCreate.from("davisgt@alumchat.xyz");

            AccountManager accountManager = AccountManager.getInstance(connection);
            accountManager.createAccount(id.getLocalpartOrNull(), "bananasenpijamas");
        }catch (XmppStringprepException | SmackException.NoResponseException | XMPPException.XMPPErrorException | SmackException.NotConnectedException | InterruptedException e){

        }
    }
/*
        public void createAccount(String username, String password) throws XMPPException {
            // Create a map for all the required attributes, but give them blank values.
            Map<String, String> attributes = new HashMap<String, String>();
            for (String attributeName : getAccountAttributes()) {
                attributes.put(attributeName, "");
            }
            createAccount(username, password, attributes);
        }
    */
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

    public void mandarMensaje(Nodo dest, String msn){
        String pakage = "";

        pakage = pakage + Topologia.getInstance().getActualN().getNombre() +" ø ";

        pakage = pakage + dest.getNombre() + " ø ";

        pakage = pakage + dest.caminoCorto()+" ø ";

        pakage = pakage + dest.distCaminoCorto()+" ø ";

        pakage = pakage + Topologia.getInstance().listaNodos() + " ø ";

        pakage = pakage + msn;

        System.out.println("_____________________________");
        System.out.println(pakage);
        System.out.println("_____________________________");

    }

}
