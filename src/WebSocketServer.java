import java.io.*;
import java.util.*;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.server.PathParam;
import org.json.simple.*;

//translate bytes of request to string


/*
* @WebServlet indicates the url of this file (ex: localhost:8080/WSTest/ws)
*/
@ServerEndpoint(value="/ws",configurator = HttpSessionConfigurator.class)
public class WebSocketServer {

  HttpSession httpSession;
  private static final long serialVersionUID = 1L;
  static int count=0;
  private Session session;
  private static Set<Session> clients = new HashSet<Session>();
  public static HashMap<String, Session> users = new  HashMap<String,Session>();


  @OnOpen
  public void onOpen(Session session,EndpointConfig config) throws IOException {
    // Get session and WebSocket connection
    // users.put(id,session);
    System.out.println("opened() Current thread "+ Thread.currentThread().getName());
    this.httpSession = (HttpSession) config.getUserProperties().get("ws");
    System.out.println("User joined with http session id "+ httpSession.getId());
    String response = "User | WebSocket session ID "+ session.getId() +" | HTTP session ID " + httpSession.getId();
    System.out.println(response);
    session.getBasicRemote().sendText(response);


  }

  @OnMessage
  public void onMessage(Session session, byte[] message) throws IOException {
    // Handle new messages
    // String jsonText = JSONValue.toJSONString(obj);

    session.getBasicRemote().sendText("GOT IT");
    System.out.println("HERE");
    File file = new File("/opt/tomcat/data/testSOP.docx");
    try (FileOutputStream fileOuputStream = new FileOutputStream(file)) {
      fileOuputStream.write(message);
      System.out.println(message.toString());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  @OnMessage
  public void onMessage(Session session, String message) throws IOException {
    // Handle new messages
    // String jsonText = JSONValue.toJSONString(obj);
    System.out.println(message);
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
