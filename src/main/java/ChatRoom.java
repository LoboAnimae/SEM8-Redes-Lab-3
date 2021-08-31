import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;

public class ChatRoom {

    private Chat chat;
    private String JID;
    private String nombre;

    public ChatRoom(){

    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public String getJID() {
        return JID;
    }

    public void setJID(String JID) {
        this.JID = JID;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
