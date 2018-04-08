package blokdata;
import java.io.*;
import java.nio.*;
import java.util.*;
import java.lang.System;
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
import io.jsonwebtoken.SignatureException;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
  public static BlockchainServer getServer(){
    return bcs;
  }

  public void finalize(){
  }

  private String calculateHash(byte[] text) {
      MessageDigest digest;
      try {
          digest = MessageDigest.getInstance("SHA-1");
      } catch (NoSuchAlgorithmException e) {
          return "HASH_ERROR";
      }
      byte finalText[]=new byte[text.length-2];
      System.arraycopy(text,2,finalText,0,finalText.length);
      final byte bytes[] = digest.digest(finalText);

      final StringBuilder hexString = new StringBuilder();
      for (int i=2;i<bytes.length;i++) {
          String hex = Integer.toHexString(0xff & bytes[i]);
          if (hex.length() == 1) {
              hexString.append('0');
          }
          hexString.append(hex);
      }
      LOGGER.info(hexString.toString());
      return hexString.toString();
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
        if(key.compareToIgnoreCase("userid")==0){
            try {

                  Claims claim=Jwts.parser().setSigningKey("Secret").parseClaimsJws(val).getBody();
                  String u=(String)claim.get("Username");
                  System.out.println("verified");
                  System.out.println(u.trim());
                  session.getUserProperties().put("userId",u);
                  //OK, we can trust this JWT

              } catch (SignatureException e) {
                System.out.println("not verified");

                  e.printStackTrace();
              }
        }
        LOGGER.info(val);
      }
    }

    session.getUserProperties().put("Download",pf);
    LOGGER.info(session.getId());
    clients.add(session);
    LOGGER.info("client connections:"+clients.size());
  }

  @OnMessage
  public void onMessage(Session session, byte[] message) throws IOException {

    LOGGER.severe("message from "+session.getId());
    //Upload bits 11   & Download bits 00 to be prefixed to the ByteArray
    if(message[0]==1&&message[1]==1){
      LOGGER.info("Upload"+message.length);
      File file = new File("/opt/tomcat/data/"+(fileMD.get((String)session.getUserProperties().get("userId")).get(0)).getFileId()+"."+(fileMD.get((String)session.getUserProperties().get("userId")).get(0)).getFileType());
      try (FileOutputStream fileOuputStream = new FileOutputStream(file)) {
        fileOuputStream.write(message);
        LOGGER.info(message.toString());
      } catch (IOException e) {
        e.printStackTrace();
      }

      String hash=calculateHash(message);
      // Distribution =========================================================================

      String s=new DistributionAlgo().distribute((String)session.getUserProperties().get("userId"));
      Blockchain bc=new Blockchain();
      String appId=(String)session.getUserProperties().get("userId");
      try{
        BlockchainServer bcs=WebSocketServer.getServer();
        List<Blockchain> agents=bcs.getAllAgents();
        for(Blockchain agent:agents){
          LOGGER.info("Agent"+agent);
        }
        if(bcs.getAgent(appId)==null){
          LOGGER.info("AGENT DOESNT EXIST");
          bc=bcs.addAgent(appId);
          bc.addBlock(bc.createBlock("Create",fileMD.get((String)session.getUserProperties().get("userId")).get(0).getFileId(),s,hash));
          LOGGER.info(bc.toString());
        }else{
          LOGGER.info("AGENT EXISTS");
          bc=bcs.getAgent(appId);
          bc.addBlock(bc.createBlock("Create",fileMD.get((String)session.getUserProperties().get("userId")).get(0).getFileId(),s,hash));
          LOGGER.info("BLockADDED");
        }
        String filename= "/opt/tomcat/data/Blockchain/blockchain.csv";
        FileWriter fw = new FileWriter(filename,true); //the true will append the new data
        Block block= bc.getLatestBlock();
        String newBlock="Create,"+block.getIndex()+","+block.getTimestamp()+","+block.getHash()+","+block.getPreviousHash()+","+appId+","+block.getPeer()+","+block.getFileId()+"\n";
        LOGGER.info("String:"+newBlock);
        //fw.write(newBlock+"\n");//appends the string to the file
        //fw.close();
        try (FileOutputStream fileOuputStream = new FileOutputStream(filename,true)) {
          fileOuputStream.write(newBlock.getBytes());
          LOGGER.info(message.toString());
        } catch (IOException e) {
          e.printStackTrace();
        }
        file2peer.put(fileMD.get((String)session.getUserProperties().get("userId")).get(0).getFileId(),(String)session.getUserProperties().get("userId")+","+s);
        LOGGER.info(file2peer.size()+"");
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("messageType","storage");
        jsonObject.put("owner",(String)session.getUserProperties().get("userId"));
        jsonObject.put("fileName",fileMD.get((String)session.getUserProperties().get("userId")).get(0).getFileName());
        jsonObject.put("fileId",fileMD.get((String)session.getUserProperties().get("userId")).get(0).getFileId());
        jsonObject.put("fileSize",fileMD.get((String)session.getUserProperties().get("userId")).get(0).getFileSize());
        sendToPeer(s,jsonObject,(fileMD.get((String)session.getUserProperties().get("userId")).get(0)).getFileId()+"."+(fileMD.get((String)session.getUserProperties().get("userId")).get(0)).getFileType());
        LOGGER.info(jsonObject.toString());
        fileMD.get((String)session.getUserProperties().get("userId")).remove(0);
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



    }else {
      LOGGER.info("Download"+message.length);
      message[0]=message[1]=0;
      //
    //   File file = new File("/opt/tomcat/data/"+((ArrayList<PushFile>)session.getUserProperties().get("Download")).get(0).fileId);
    //   try (FileOutputStream fileOuputStream = new FileOutputStream(file)) {
    //     fileOuputStream.write(message);
    //   } catch (IOException e) {
    //     e.printStackTrace();
    //   }
      //
    //   File f=new File("/opt/tomcat/data/"+((ArrayList<PushFile>)session.getUserProperties().get("Download")).get(0).fileId);
    //   byte[] bytes=new byte[(int)f.length()];
    //   FileInputStream fileStream= new FileInputStream(f);
    //   fileStream.read(bytes);
    //   fileStream.close();
        String hash=calculateHash(message);
        String filename= "/opt/tomcat/data/Blockchain/blockchain.csv";
        FileWriter fw = new FileWriter(filename,true); //the true will append the new data
        BlockchainServer bcs=WebSocketServer.getServer();
        Blockchain bc=bcs.getAgent(((ArrayList<PushFile>)session.getUserProperties().get("Download")).get(0).to);
        Block block=bc.getLatestBlock();
        bc.addBlock(bc.createBlock("Fetch",((ArrayList<PushFile>)session.getUserProperties().get("Download")).get(0).to,((ArrayList<PushFile>)session.getUserProperties().get("Download")).get(0).from,hash));
        LOGGER.info("BLockADDED");
        String newBlock="Get,"+block.getIndex()+","+System.currentTimeMillis()+","+","+","+((ArrayList<PushFile>)session.getUserProperties().get("Download")).get(0).to+","+((ArrayList<PushFile>)session.getUserProperties().get("Download")).get(0).from+","+((ArrayList<PushFile>)session.getUserProperties().get("Download")).get(0).fileId+"\n";
        LOGGER.info("String:"+newBlock);
        //fw.write(newBlock+"\n");//appends the string to the file
        //fw.close();
        try (FileOutputStream fileOuputStream = new FileOutputStream(filename,true)) {
          fileOuputStream.write(newBlock.getBytes());
          LOGGER.info(message.toString());
        } catch (IOException e) {
          e.printStackTrace();
        }
          (sessions.get(((ArrayList<PushFile>)session.getUserProperties().get("Download")).get(0).to)).getBasicRemote().sendBinary(ByteBuffer.wrap(message));
          ((ArrayList<PushFile>)session.getUserProperties().get("Download")).remove(0);
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
        //decoding the token and verifying it
      LOGGER.info((String)session.getUserProperties().get("userId"));
      sessions.put((String)session.getUserProperties().get("userId"),session);
      clientData.put((String)session.getUserProperties().get("userId"),new DistributionAlgo((Long)jsonObject.get("storage"),(Long)jsonObject.get("rating"),(Double)jsonObject.get("onlinePercent")));
    }
    else if(messageType.compareToIgnoreCase("metaData")==0){
        //decoding the token and verifying it
      LOGGER.info((String)session.getUserProperties().get("userId"));
      sessions.put((String)session.getUserProperties().get("userId"),session);
      clientData.put((String)session.getUserProperties().get("userId"),new DistributionAlgo((Long)jsonObject.get("storage"),(Long)jsonObject.get("rating"),(Double)jsonObject.get("onlinePercent")));
    }
    else if(messageType.compareToIgnoreCase("fetchFile")==0){
      Session fetchSession=sessions.get(file2peer.get(jsonObject.get("fileId")).split(",")[1]);
      JSONObject file=new JSONObject();
      file.put("messageType","fetch");
      file.put("fileId",(String)jsonObject.get("fileId"));
      file.put("peerId",file2peer.get(jsonObject.get("fileId")).split(",")[0]);
      //pf.add(new PushFile((String)session.getUserProperties().get("userId"),(String)jsonObject.get("peerId"),(String)jsonObject.get("fileId"),(String)jsonObject.get("fileType")));
      fetchSession.getBasicRemote().sendText(file.toString());
    }else if(messageType.compareToIgnoreCase("fetch")==0){
        ((ArrayList<PushFile>)session.getUserProperties().get("Download")).add(new PushFile((String)jsonObject.get("peerId"),(String)session.getUserProperties().get("userId"),(String)jsonObject.get("fileId")));
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
     fileStream.close();
      for(Iterator<Session> iter = clients.iterator(); iter.hasNext(); ){
        Session sess=iter.next();
        sess.getBasicRemote().sendBinary(ByteBuffer.wrap(bytes));
      }

    }catch(Exception e){
      e.printStackTrace();
    }


  }
}//ws
