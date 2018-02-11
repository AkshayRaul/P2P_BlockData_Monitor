import java.io.*;
import java.nio.*;
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
    private static ArrayList<Session> clients = new ArrayList<Session>();
    static HashMap<String,DistributionAlgo> clientData= new HashMap<String,DistributionAlgo>();
    public static HashMap<String, ArrayList<fileMetaData>> fileMD = new  HashMap<String,ArrayList<fileMetaData>>();
    private final static Logger LOGGER = Logger.getLogger("Websocketserver");

    static HashMap<String,DistributionAlgo> getClientData(){
        return clientData;
    }
    static ArrayList<Session> getOpenSessions(){
        return clients;
    }
    public void finalize(){

    }
    @OnOpen
    public void onOpen(Session session,EndpointConfig config) throws IOException {
        // Get session and WebSocket connection
        System.gc();
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

        LOGGER.info(session.getId());
        clients.add(session);
        LOGGER.info("client connections:"+clients.size());

        // session.getBasicRemote().sendText("");


    }

    @OnMessage
    public void onMessage(Session session, byte[] message) throws IOException {
        // Handle new messages
        // String jsonText = JSONValue.toJSONString(obj);
        // For file transfers only
        LOGGER.severe("message from "+session.getId());
        //session.getBasicRemote().sendText("GOT IT");
        File file = new File("/opt/tomcat/data/"+(fileMD.get((String)session.getUserProperties().get("userId")).get(0)).getFileName());
        try (FileOutputStream fileOuputStream = new FileOutputStream(file)) {
            fileOuputStream.write(message);
            System.out.println(message.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }


        // Distribution =========================================================================
        new DistributionAlgo().distribute();
        //========================================================================================
        broadcast(session,fileMD.get(session.getUserProperties().get("userId")).get(0).getFileName());
        fileMD.get(session.getUserProperties().get("userId")).remove(0);
        LOGGER.info("DONE");
    }
    @OnMessage
    public void onMessage(Session session, String message) throws IOException {

        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject=new JSONObject();
        try{
            jsonObject =(JSONObject) jsonParser.parse(message);
        }catch(ParseException e){

        }
        String messageType=(String)jsonObject.get("messageType");
        if(messageType==null){
            messageType="messageType";
        }
        LOGGER.info("MessageType:"+messageType);
        if(messageType.compareToIgnoreCase("fileUpload")==0){
            // Logic for File Distribution
            LOGGER.info("messagetype: "+(String)jsonObject.get("messageType"));
            JSONArray arr=(JSONArray)jsonObject.get("files");
            Iterator i = arr.iterator();
            System.out.println(arr);
            fileMD.put((String)session.getUserProperties().get("userId"),new ArrayList<fileMetaData>());
            while (i.hasNext()) {
                JSONObject file=(JSONObject)i.next();
                String title = (String) file.get("fileName");
                LOGGER.info("fileName"+title.substring(title.indexOf("."),title.length()));
                try{
                    fileMD.get((String)(String)session.getUserProperties().get("userId")).add(new fileMetaData((String) file.get("fileName"),fileMetaData.RandomString(),(long)file.get("fileSize")));
                }catch(Exception e){
                    e.printStackTrace();
                }
                LOGGER.info(title);
            }
        }
        else if(messageType.compareToIgnoreCase("metaData")==0){
            session.getUserProperties().put("userId",(String)jsonObject.get("userId"));
            clientData.put((String)jsonObject.get("userId"),new DistributionAlgo((int)jsonObject.get("storage"),(double)jsonObject.get("rating"),(double)jsonObject.get("onlinePercent")));
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
        fileMD.remove(session.getId());
        clients.remove(session);
        System.gc();

    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        // Do error handling here
        LOGGER.severe(throwable.getMessage());
    }

    public void sendMessage(HashMap<String,String> distribute){


    }
    void broadcast(Session session,String fileName){
        for(Iterator<Session> iter = clients.iterator(); iter.hasNext(); ){
            Session sess=iter.next();
            if(sess.equals(session)==true){

            }else{
                try{
                    File f=new File("/opt/tomcat/data/"+fileName);
                    byte[] bytes=new byte[(int)f.length()];
                    FileInputStream fileStream= new FileInputStream(f);
                    fileStream.read(bytes);
                    sess.getBasicRemote().sendBinary(ByteBuffer.wrap(bytes));
                }catch(Exception e){

                }
            }
        }

    }
}//ws
