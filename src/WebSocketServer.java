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
import javax.servlet.http.HttpSession;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;


import org.json.simple.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//translate bytes of request to string


/*
* @WebServlet indicates the url of this file (ex: localhost:8080/WSTest/ws)
*/
@ServerEndpoint("/ws")
public class WebSocketServer extends ServerEndpointConfig.Configurator{

  HttpSession httpSession;
  private static final long serialVersionUID = 1L;
  static int count=0;
  private Session session;
  private static Set<Session> clients = new HashSet<Session>();
  public static HashMap<String, Session> users = new  HashMap<String,Session>();
  @Override
  public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
    System.out.println("modifyHandshake() Current thread " + Thread.currentThread().getName());
    String user = request.getParameterMap().get("user").get(0);
    sec.getUserProperties().put(user, request.getHttpSession());
    System.out.println("modifyHandshake() User " + user + " with http session ID " + ((HttpSession) request.getHttpSession()).getId());
  }

  @OnOpen
  public void onOpen(Session session,EndpointConfig config) throws IOException {
    // Get session and WebSocket connection
    // users.put(id,session);
    System.out.println("OPEN");
    System.out.println("Protocol Version"+session.getProtocolVersion());
    System.out.println(session.getRequestParameterMap().toString());
    this.httpSession = (HttpSession) config.getUserProperties().get("user");
    System.out.println("User joined with http session id "+ httpSession.getId());
    String response = "User  | WebSocket session ID "+ session.getId() +" | HTTP session ID " + httpSession.getId();
    System.out.println(response);


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
