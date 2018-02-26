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
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Claims;
import javax.xml.bind.DatatypeConverter;
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
  private static ArrayList<PushFile> pf=new ArrayList<PushFile>();
  private HashMap<String,String> file2peer=new HashMap<String,String>();
  private static ArrayList<Session> clients = new ArrayList<Session>();
  private static HashMap<String,Session> sessions = new HashMap<String,Session>();
  static HashMap<String,DistributionAlgo> clientData= new HashMap<String,DistributionAlgo>();
  public static HashMap<String, ArrayList<fileMetaData>> fileMD = new  HashMap<String,ArrayList<fileMetaData>>();
  private final static Logger LOGGER = Logger.getLogger("Websocketserver");
  static BlockchainServer bcs=new BlockchainServer();

  static HashMap<String,DistributionAlgo> getClientData(){
    return clientData;
  }
  static ArrayList<Session> getOpenSessions(){
    return clients;
  }
  static BlockchainServer getServer(){
    return bcs;
  }
  public void finalize(){
  }
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
    LOGGER.info(session.getId());
    clients.add(session);
    LOGGER.info("client connections:"+clients.size());
  }

  @OnMessage
  public void onMessage(Session session, byte[] message) throws IOException {
    // Handle new messages
    // String jsonText = JSONValue.toJSONString(obj);
    // For file transfers only
    LOGGER.severe("message from "+session.getId());
    //Upload bits 11   & Download bits 00 to be prefixed to the ByteArray
    if(message[0]==1&&message[1]==1){
      LOGGER.info("Upload");
      File file = new File("/opt/tomcat/data/"+(fileMD.get((String)session.getUserProperties().get("userId")).get(0)).getFileId()+"."+(fileMD.get((String)session.getUserProperties().get("userId")).get(0)).getFileType());
      try (FileOutputStream fileOuputStream = new FileOutputStream(file)) {
        fileOuputStream.write(message);
        LOGGER.info(message.toString());
      } catch (IOException e) {
        e.printStackTrace();
      }
      // Distribution =========================================================================

      Thread t =new Thread(new Runnable(){

        public void run(){
          String s=new DistributionAlgo().distribute((String)session.getUserProperties().get("userId"));
          Blockchain bc=new Blockchain();
          String appId=(String)session.getUserProperties().get("userId");
          try{
            //LOGGER.info("GETAGENT"+bcs.getAgent(appId).toString());
            BlockchainServer bcs=WebSocketServer.getServer();
            List<Blockchain> agents=bcs.getAllAgents();
            for(Blockchain agent:agents){
              LOGGER.info("Agent"+agent);
            }
            if(bcs.getAgent(appId)==null){
              LOGGER.info("AGENT DOESNT EXIST");
              bc=bcs.addAgent(appId);
              bc.addBlock(bc.createBlock(fileMD.get((String)session.getUserProperties().get("userId")).get(0).getFileId(),s));
              LOGGER.info(bc.toString());
            }else{
              LOGGER.info("AGENT EXISTS");
              bc=bcs.getAgent(appId);
              bc.addBlock(bc.createBlock(fileMD.get((String)session.getUserProperties().get("userId")).get(0).getFileId(),s));
              LOGGER.info("BLockADDED");
            }
            String filename= "/opt/tomcat/data/Blockchain/blockchain.csv";
            FileWriter fw = new FileWriter(filename,true); //the true will append the new data
            Block block= bc.getLatestBlock();
            String newBlock=block.getIndex()+","+block.getTimestamp()+","+block.getHash()+","+block.getPreviousHash()+","+appId+","+block.getPeer()+","+block.getFileId()+"\n";
            LOGGER.info("String:"+newBlock);
            //fw.write(newBlock+"\n");//appends the string to the file
            //fw.close();
            try (FileOutputStream fileOuputStream = new FileOutputStream(filename,true)) {
              fileOuputStream.write(newBlock.getBytes());
              LOGGER.info(message.toString());
            } catch (IOException e) {
              e.printStackTrace();
            }
            file2peer.put(fileMD.get((String)session.getUserProperties().get("userId")).get(0).getFileId(),s);
            LOGGER.info(file2peer.size()+"");
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("messageType","storage");
            jsonObject.put("owner",(String)session.getUserProperties().get("userId"));
            jsonObject.put("fileName",fileMD.get((String)session.getUserProperties().get("userId")).get(0).getFileName());
            jsonObject.put("fileId",fileMD.get((String)session.getUserProperties().get("userId")).get(0).getFileId());
            jsonObject.put("fileSize",fileMD.get((String)session.getUserProperties().get("userId")).get(0).getFileSize());
            //session.getBasicRemote().send(
            sendToPeer(s,jsonObject,(fileMD.get((String)session.getUserProperties().get("userId")).get(0)).getFileId()+"."+(fileMD.get((String)session.getUserProperties().get("userId")).get(0)).getFileType());
            fileMD.get(session.getUserProperties().get("userId")).remove(0);
            LOGGER.info("DONE");
            broadcast(session);
          }
          catch(Exception ioe){
            ioe.printStackTrace();
          }
          finally{
            //fw.close();
          }
          //========================================================================================
          //broadcast(session,fileMD.get(session.getUserProperties().get("userId")).get(0).getFileName());


        }
      });
      t.start();

    }else {
      LOGGER.info("Download");
      message[0]=message[1]=0;
      File file = new File("/opt/tomcat/data/"+(fileMD.get((String)session.getUserProperties().get("userId")).get(0)).getFileId()+"."+(fileMD.get((String)session.getUserProperties().get("userId")).get(0)).getFileType());
      try (FileOutputStream fileOuputStream = new FileOutputStream(file)) {
        fileOuputStream.write(message);
        // LOGGER.info(message.toString());
      } catch (IOException e) {
        e.printStackTrace();
      }
      for(int i=0;i<pf.size();i++){
        if((pf.get(i).from).compareTo((String)session.getUserProperties().get("userId"))==0){
          File f=new File("/opt/tomcat/data/"+pf.get(i).fileId+pf.get(i).fileType);
          byte[] bytes=new byte[(int)f.length()];
          FileInputStream fileStream= new FileInputStream(f);
          fileStream.read(bytes);
          (sessions.get(pf.get(i).to)).getBasicRemote().sendBinary(ByteBuffer.wrap(bytes));
          pf.remove(i);
          break;
        }
      }
    }

  }

  @OnMessage
  public void onMessage(Session session, String message) throws IOException {
    LOGGER.info(message);
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
          fileMD.get((String)session.getUserProperties().get("userId")).add(new fileMetaData((String) file.get("fileName"),(String) file.get("fileId"),(long)file.get("fileSize")));
        }catch(Exception e){
          e.printStackTrace();
        }
        LOGGER.info(title);
      }
    }
    else if(messageType.compareToIgnoreCase("metaData")==0){
      //       Claims jws=Jwts.parser()
      //       .setSigningKey(TextCodec.BASE64.decode(
      // // This generated signing key is
      // // the proper length for the
      // // HS256 algorithm.
      // "TD8Guh82ARO4/jsAkAlfGB1fDy0QL+OmDFFvwrJa8LA=")).parseClaimsJws((String)jsonObject.get("userId")).getBody();
      //       String claim=(String)jws.get("Username");
      session.getUserProperties().put("userId",jsonObject.get("userId"));
      sessions.put((String)jsonObject.get("userId"),session);
      clientData.put((String)jsonObject.get("userId"),new DistributionAlgo((Double)jsonObject.get("storage"),(Double)jsonObject.get("rating"),(Long)jsonObject.get("onlinePercent")));
    }
    else if(messageType.compareToIgnoreCase("fetchFile")==0){
      Session fetchSession=sessions.get(file2peer.get(jsonObject.get("fileId")));
      JSONObject file=new JSONObject();
      file.put("messageType","fetch");
      file.put("fileId",(String)jsonObject.get("fileId"));
      pf.add(new PushFile((String)session.getUserProperties().get("userId"),(String)jsonObject.get("peerId"),(String)jsonObject.get("fileId"),(String)jsonObject.get("fileType")));
      fetchSession.getBasicRemote().sendText(file.toString());
    }
    try{

    }catch(Exception e){

    }

  }

  @OnClose
  public void onClose(Session session) throws IOException {
    LOGGER.info("Closed:"+session.getUserProperties().get("userId"));
    fileMD.remove(session.getUserProperties().get("userId"));
    clients.remove(session);
    System.gc();

  }

  @OnError
  public void onError(Session session, Throwable throwable) {
    // Do error handling here
    LOGGER.severe(throwable.getMessage());
    throwable.printStackTrace();
  }

  public static void sendToPeer(String user,JSONObject json,String fileName){
    Session sendToPeer=sessions.get(user);
    File f=new File("/opt/tomcat/data/"+fileName);
    byte[] bytes=new byte[(int)f.length()];

    try{
      sendToPeer.getBasicRemote().sendText(json.toString());
      FileInputStream fileStream= new FileInputStream(f);
      fileStream.read(bytes);
      sendToPeer.getBasicRemote().sendBinary(ByteBuffer.wrap(bytes));
    }catch(Exception e){
      e.printStackTrace();
    }

  }
  static void broadcast(Session session){

    try{
      File f=new File("/opt/tomcat/data/Blockchain/blockchain.csv");
      LOGGER.info("Blockchain file length"+f.length());
      byte[] bytes=new byte[(int)f.length()+2];
      bytes[0]=0;
      bytes[1]=1;

      FileInputStream fileStream= new FileInputStream(f);
      fileStream.read(bytes,2,bytes.length-2);
     // for(int i=0;i<bytes.length;i++){
       // System.out.print(bytes[i]);
     // }
      for(Iterator<Session> iter = clients.iterator(); iter.hasNext(); ){
        Session sess=iter.next();
        sess.getBasicRemote().sendBinary(ByteBuffer.wrap(bytes));
      }
    }catch(Exception e){
      e.printStackTrace();
    }


  }
}//ws
