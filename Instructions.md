# Server Side Monitor Code

 Please study these classes carefully. This is much better than referring other websites. Please understand the objects, their return types and play with the code.

Oracle Java WebSockets Specifications:
[https://docs.oracle.com/javaee/7/api/javax/websocket/package-summary.html](https://docs.oracle.com/javaee/7/api/javax/websocket/package-summary.html "JAVA EE WebSockets Specification")

Websockets Manual by Internet Engineering Task Force (IETF):
https://tools.ietf.org/html/rfc6455

# Websocket URL
This specification defines two URI schemes, using the ABNF syntax
   defined in RFC 5234 [RFC5234], and terminology and ABNF productions
   defined by the URI specification RFC 3986 [RFC3986].

+ ws-URI = "ws:" "//" host [ ":" port ] path [ "?" query ]
+ wss-URI = "wss:" "//" host [ ":" port ] path [ "?" query ]

The port component is OPTIONAL; the default for "ws" is port 80,
while the default for "wss" is port 443.

The URI is called "secure" (and it is said that "the secure flag is
set") if the scheme component matches "wss" case-insensitively.

Fragment identifiers are meaningless in the context of WebSocket URIs and MUST NOT be used on these URIs.  As with any URI scheme, the character "#", when not indicating the start of a fragment, MUST be
escaped as %23.
# Required jars

JSON WebToken:          
https://jar-download.com/explore-java-source-code.php?a=jjwt&g=io.jsonwebtoken&v=0.9.0&downloadable=1

JSON JAR:
http://www.java2s.com/Code/Jar/j/Downloadjsonjar.htm <br>
OR<br>
http://www.java2s.com/Code/Jar/j/Downloadjsonsimple11jar.htm


[![Build Status](https://img.shields.io/circleci/project/github/ntkme/github-buttons.svg)](https://travis-ci.org/Netflix/fast_jsonapi)

## About Websockets
The WebSocket Protocol enables two-way communication between a client
  running untrusted code in a controlled environment to a remote host
  that has opted-in to communications from that code.  The security
  model used for this is the origin-based security model commonly used
  by web browsers.  The protocol consists of an opening handshake
  followed by basic message framing, layered over TCP.  The goal of
  this technology is to provide a mechanism for browser-based
  applications that need two-way communication with servers that does
  not rely on opening multiple HTTP connections (e.g., using
  XMLHttpRequest or `<iframe>` and long polling).


## File Upload format

Prefix ```11``` to the file while uploading file to server
<br>Server changes ```11``` to ```00``` while distributing file
<br> Use Prefix ```01``` while broadcasting ledger from server

<B>Reason:</B><br>
The reason for prefixing is that WebSockets are not request-response type of communication. But invoked only when the event ```onOpen```,```onClose```,```onMessage```,```onError``` are called. So the websocket has no idea what the incoming file is. Another reason being, when a file is received, it invokes ```onMessage``` which takes ByteArray as parameter, thus only receives the file and no meta data with it.

# ServerSide get HTTP Headers
-------------
```java
import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;
import java.io.*;
import java.util.*;
import java.lang.*;
import java.util.logging.Logger;

public class HttpSessionConfigurator extends ServerEndpointConfig.Configurator {
  private static Logger LOGGER = Logger.getLogger("HttpSessionConfigurator");

  @Override
  public void modifyHandshake(ServerEndpointConfig config, HandshakeRequest request, HandshakeResponse response) {

    LOGGER.info("modifyHandshake() Current thread " + Thread.currentThread().getName());
    LOGGER.info(request.SEC_WEBSOCKET_KEY);
    config.getUserProperties().put("request",request);
    LOGGER.info((request.getHeaders()).get("sec-websocket-key").toString());
    super.modifyHandshake(config, request, response);

  }

}

```
The following code is to intercept HTTP Request to server before calling `@onOpen` method. This is a method of Interface `ServerEndpointConfig.Configurator`.
```java
@Override
public void modifyHandshake(ServerEndpointConfig config, HandshakeRequest request, HandshakeResponse response)
```
This is overridden here to get HTTP Headers, Request and Session object. The user properties acquired from `HandshakeRequest request` are accessible from `EndpointConfig`
```java
public void onOpen(Session session,EndpointConfig config)
public void onMessage(Session session,EndpointConfig config)
public void onClose(Session session,EndpointConfig config)
```
<b>Note:</b><br>
If you do not intercept by extending ```ServerEndpointConfig```, and override ```modifyHandshake()```, you will not be able to use any headers for the purpose of Authentication and storing mobile information.

# WebSocket Functions
---------------
### onOpen

```java
public @interface OnOpen() ```

This method level annotation can be used to decorate a Java method that wishes to be called when a new web socket session is open.

The method may only take the following parameters:-

    1.optional Session parameter
    2.optional EndpointConfig parameter
    3.Zero to n String parameters annotated with the PathParam annotation.

The parameters may appear in any order.

### onMessage
This method level annotation can be used to make a Java method receive incoming web socket messages. Each websocket endpoint may only have one message handling method for each of the native websocket message formats: text, binary and pong. Methods using this annotation are allowed to have parameters of types described below, otherwise the container will generate an error at deployment time.

The allowed parameters are:

    1.Exactly one of any of the following choices
        if the method is handling text messages:
            String to receive the whole message
            Java primitive or class equivalent to receive the whole message converted to that type
            String and boolean pair to receive the message in parts
            Reader to receive the whole message as a blocking stream
            any object parameter for which the endpoint has a text decoder (Decoder.Text or Decoder.TextStream).
        if the method is handling binary messages:
            byte[] or ByteBuffer to receive the whole message
            byte[] and boolean pair, or ByteBuffer and boolean pair to receive the message in parts
            InputStream to receive the whole message as a blocking stream
            any object parameter for which the endpoint has a binary decoder (Decoder.Binary or Decoder.BinaryStream).
        if the method is handling pong messages:
            PongMessage for handling pong messages
    2.and Zero to n String or Java primitive parameters annotated with the PathParam annotation for server endpoints.
    3.and an optional Session parameter
### onClose
```java
public @interface OnClose```

This method level annotation can be used to decorate a Java method that wishes to be called when a web socket session is closing.

The method may only take the following parameters:-

    1.optional Session parameter
    2.optional CloseReason parameter
    3.Zero to n String parameters annotated with the PathParam annotation.

The parameters may appear in any order. See Endpoint.onClose(javax.websocket.Session, javax.websocket.CloseReason) for more details on how the session parameter may be used during method calls annotated with this annotation.

# @onMessage requirements
--------
```onMessage```

# Server @onMessage with String as JSON
------
Send the JSON data to server in these formats only
```javascript
//JSON File Upload Structure
{
  "messageType":"fileUpload",
  "files":[
    {
      fileName:""
      fileSize:"",
      fileType:""

    }
  ]
}
//Send and Storage size IP Message

{
  "messageType":"metaData"
  "ipAddress":"",
  "storageSpace":""
  "userId":""
}
```
Server Sends data to destination node for file Upload
```javascript
//Send IP and file data
{
  "messageType":"fileDownload",
  "method":"POST",
  "file-exchange-key":Base64 key,
  "fileId":""
  "fileSize":"",
  "owner":""
  "ownerId":""
}
```
## Classes and
+ WebSocketServer

1.private Session session<br>
    Stores Session Object temporarily    
2.private static ArrayList<PushFile> pf<br>
    ArrayList serving as queue for uploaded files from mobiles before being distributed<br>
3.private HashMap<String,String> file2peer<br>
    Stores in HashMap peerId and fileId so that while downloading the file from user, fileID is used to locate the peerId
4.private static ArrayList<Session> clients<br>
    Keeps a list of all clients mobiles online
5.private static HashMap<String,Session> sessions<br>
    Stores session of a mobile device online. The key value is the userID
6.static HashMap<String,DistributionAlgo> clientData<br>
    Stores the client mobile Device data and distr data
7.public static HashMap<String, ArrayList<fileMetaData>> fileMD<br>
    HashMap of ArrayList of files data. Key is userID. The value is ArrayList of list of files uploaded and their metadata
8.static BlockchainServer bcs
    Static object of BlockchainServer to store the Blockchain Server Object

+ Distribution Algo

```java
public String distribute(String appId){
    HashMap<String,DistributionAlgo> clientData=WebSocketServer.getClientData();
    clients=WebSocketServer.getOpenSessions();
    String selectedPeer="";
    double maxProfileIndex=-99999;
    for(Session client:clients){
        DistributionAlgo profile=clientData.get((String)client.getUserProperties().get("userId"));
        double peerProfile=((profile.storage/MAX_STORAGE)-1/profile.onlinePercent)/3;
        System.out.println(peerProfile);
        if(peerProfile>maxProfileIndex){
            selectedPeer=(String)client.getUserProperties().get("userId");
            maxProfileIndex=peerProfile;
        }
    }
    System.out.println("USERID<DIST"+selectedPeer);
    return selectedPeer;
}
```
The peerProfile decides what is the rating of a device before file has been distributed.





# ToDo List
----------
+ ~~Send file data to server as per Specification below~~
+ ~~Create a service for the app which runs even after app is Closed~~
+ ~~Handle nanoHTTP Server errors~~
+ ~~Authentication using key exchange~~
+ ~~Server Side Authentication~~
+ ~~Create Blockchain and save data on a file/csv/json~~
+ ~~Send file permissions~~
+ ~~Broadcast ledgers~~
+ ~~Check ledgers~~

# Future Work
----------
+ Implement queues on mobile nodes
+ Make true peer to peer network
+ Implement peer penalty system
