import java.io.*;
import java.util.*;
import java.util.logging.*;
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
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;
//translate bytes of request to string


/*
* @WebServlet indicates the url of this file (ex: localhost:8080/WSTest/ws)
*/
@ServerEndpoint(value="/ws/",configurator=HttpSessionConfigurator.class)
public class WebSocketServer {

  HttpSession httpSession;
  private static final long serialVersionUID = 1L;
  static int count=0;
  private Session session;
  private static Set<Session> clients = new HashSet<Session>();
  public static HashMap<String, Session> users = new  HashMap<String,Session>();
  private final static Logger LOGGER = Logger.getLogger("Websocketserver");


  @OnOpen
  public void onOpen(Session session,EndpointConfig config) throws IOException {
    // Get session and WebSocket connection
    HandshakeRequest request=(HandshakeRequest)session.getUserProperties().get("request");
    Map<String,List<String>> headers=request.getHeaders();
    Set<String> keys=headers.keySet();
    for(String key:keys){
      LOGGER.info(key);
      for (Iterator<String> iter = (headers.get(key)).iterator(); iter.hasNext(); ) {
        String val = iter.next();
        LOGGER.info(val);
      }
    }


  }

  @OnMessage
  public void onMessage(Session session, byte[] message) throws IOException {
    // Handle new messages
    // String jsonText = JSONValue.toJSONString(obj);
    // For file transfers only
  }
  @OnMessage
  public void onMessage(Session session, String message) throws IOException {
    // Handle new messages
    // String jsonText = JSONValue.toJSONString(obj);
    LOGGER.info(message);
    /*
      Please use JSON Objects only
      HashMap to store file upload requests if multiple files are being uploaded
    */
    JSONObject jsonObject=(JSONObject) JSONValue.parse(message);
    if((jsonObject.get("type").toString()).compareToIgnoreCase("upload")==0){
      //GET JSON ARRAY

      //Reply with IP addresses

    }
    if((jsonObject.get("type").toString()).compareToIgnoreCase("ipAddr")==0){
      //GET JSON ARRAY
        session.getUserProperties().put("ip",jsonObject.get("ipAddr"));
      //Reply with IP addresses

    }

  }

  @OnClose
  public void onClose(Session session) throws IOException {
    LOGGER.info("Closed:"+session.getId());

  }

  @OnError
  public void onError(Session session, Throwable throwable) {
    // Do error handling here
  }

  public void sendMessage(HashMap<String,String> distribute){


  }
}//ws
