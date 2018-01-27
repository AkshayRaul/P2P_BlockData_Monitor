import java.io.*;
import java.util.*;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import javax.ws.rs.PathParam;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
// import org.json.*;
import org.json.simple.*;

/*
 * @WebServlet indicates the url of this file (ex: localhost:8080/WSTest/ws)
*/
@ServerEndpoint("/ws")
public class WebSocketServer {
    private static final long serialVersionUID = 1L;
    static int count=0;
    private Session session;
    private static Set<Session> clients = new HashSet<Session>();
    public static HashMap<String, Session> users = new HashMap<String,Session>();
    @OnOpen
    public void onOpen(Session session) throws IOException {
        // Get session and WebSocket connection
       // users.put(id,session);
        clients.add(session);
        System.out.println(session.getId());
    }
 
    @OnMessage
    public void onMessage(Session session, String message) throws IOException {
        // Handle new messages
        Object obj=JSONValue.parse(message);  
        JSONObject jsonObject = (JSONObject) obj;     
  
        String ip = (String) jsonObject.get("ip");  
        System.out.println("Got IP"+ip);
        // String jsonText = JSONValue.toJSONString(obj);  
        session.getBasicRemote().sendText("Received "+ip);      
    }
 
    @OnClose
    public void onClose(Session session) throws IOException {
        // WebSocket connection closes
        //users.remove(session);
        clients.remove(session);

    }
 
    @OnError
    public void onError(Session session, Throwable throwable) {
        // Do error handling here
    }
    
    public void sendMessage(HashMap<String,String> distribute){
        //Send File ID to clients
        //Iterate over map
        for (Object user: distribute.keySet()) {
            Session sess=users.get(user);
            try{
                sess.getBasicRemote().sendText(distribute.get(user).toString());
            }
            catch(IOException e){

            }            
        }
        
    }
}//ws