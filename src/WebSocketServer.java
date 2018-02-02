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
import org.json.simple.parser.*;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;
//translate bytes of request to string


/*
* @WebServlet indicates the url of this file (ex: localhost:8080/WSTest/ws)
*/
@ServerEndpoint(value="/ws/", configurator=HttpSessionConfigurator.class)
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
    clients.add(session);
    // session.getBasicRemote().sendText("");


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
    //JSON File Upload Structure
    /*
    {
    "messageType":"fileUpload"
    "files":[
    {
    fileName:""
    fileSize:"",
    fileType:""

  },..
  ]
}
}
*/

//Send and Storage size IP Message
/*
{
"messageType":"metaData"
"ipAddress":"",
"storageSpace":""
}
*/
LOGGER.info(message);
JSONParser jsonParser = new JSONParser();
JSONObject jsonObject=new JSONObject();
try{
  jsonObject =(JSONObject) jsonParser.parse(message);
}catch(ParseException e){

}
String messageType=(String)jsonObject.get("messageType");
if(!messageType){
  messageType="messageType";
}
LOGGER.info("MessageType:"+messageType);
if(messageType.compareToIgnoreCase("fileUpload")==0){
// Logic for File Distribution
}
else if(messageType.compareToIgnoreCase("metaData")==0){
  LOGGER.info("messagetype: "+(String)jsonObject.get("messageType"));
  session.getUserProperties().put("ip",(String)jsonObject.get("ipAddress"));
  session.getUserProperties().put("storageSpace",(String)jsonObject.get("storageSpace"));
}

// LOGGER.info("Test:"+(String)jsonObject.get("test"));
// LOGGER.info("JSON:"+(String)jsonObject.get("messageType"));
try{
  // session.getBasicRemote().sendObject((Object)jsonObject);
}catch(Exception e){

}
session.getBasicRemote().sendText(message);
}

@OnClose
public void onClose(Session session) throws IOException {
  LOGGER.info("Closed:"+session.getId());
  LOGGER.info("IP:"+session.getUserProperties().get("ipAddress"));
LOGGER.info("IP:"+session.getUserProperties().get("storageSpace"));
  clients.remove(session);
}

@OnError
public void onError(Session session, Throwable throwable) {
  // Do error handling here
}

public void sendMessage(HashMap<String,String> distribute){


}
}//ws
