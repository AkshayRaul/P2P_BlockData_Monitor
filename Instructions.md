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
public @interface OnOpen() 
```

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
public @interface OnClose
```

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

# Mobile Device Code

<b>File Encryption</b>
Use a CipherOutputStream or CipherInputStream with a Cipher and your FileInputStream / FileOutputStream.

I would suggest something like Cipher.getInstance("AES/CBC/PKCS5Padding") for creating the Cipher class. CBC mode is secure and does not have the vulnerabilities of ECB mode for non-random plaintexts. It should be present in any generic cryptographic library, ensuring high compatibility.

Don't forget to use a Initialization Vector (IV) generated by a secure random generator if you want to encrypt multiple files with the same key. You can prefix the plain IV at the start of the ciphertext. It is always exactly one block (16 bytes) in size.

If you want to use a password, please make sure you do use a good key derivation mechanism (look up password based encryption or password based key derivation). PBKDF2 is the most commonly used Password Based Key Derivation scheme and it is present in most Java runtimes, including Android. Note that SHA-1 is a bit outdated hash function, but it should be fine in PBKDF2, and does currently present the most compatible option.

Always specify the character encoding when encoding/decoding strings, or you'll be in trouble when the platform encoding differs from the previous one. In other words, don't use String.getBytes() but use String.getBytes(Charset.forName("UTF-8")).

To make it more secure, please add cryptographic integrity and authenticity by adding a secure checksum (MAC or HMAC) over the ciphertext and IV, preferably using a different key. Without an authentication tag the ciphertext may be changed in such a way that the change cannot be detected

<b>Websockets in Android</b>
WebSocket is a computer communications protocol, providing full-duplex communication channels over a single TCP connection. It is supported in HTML 5. Since the version 3.5 of the OkHttp library, you can also use WebSockets connection in your Android applications. In this tutorial, you are going to learn how to create a simple chat application with the Echo WebSocket Server which is available at the following address : http://www.websocket.org/echo.html .

First step is to add the OkHttp dependency in your Gradle build file

compile 'com.squareup.okhttp3:okhttp:3.6.0'

Don’t forget to add the Internet permission in your Android manifest since the application will use the network to create a WebSocket connection to the Echo WebSocket server. For this tutorial, we will need a simple layout with a Button to start the connection and the exchange with the server and a TextView which will be used as a console output for the messages received from the server :

```xml 
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
  xmlns:tools="http://schemas.android.com/tools"
  android:id="@+id/activity_main"
  android:layout_width="match_parent"
  android:layout_height="match_parent"
  android:paddingBottom="@dimen/activity_vertical_margin"
  android:paddingLeft="@dimen/activity_horizontal_margin"
  android:paddingRight="@dimen/activity_horizontal_margin"
  android:paddingTop="@dimen/activity_vertical_margin"
  tools:context="com.ssaurel.websocket.MainActivity">

  <Button
    android:id="@+id/start"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:layout_centerHorizontal="true"
    android:text="Start !"
    android:layout_marginTop="40dp"
    android:textSize="17sp"/>

  <TextView
    android:id="@+id/output"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:layout_below="@id/start"
    android:layout_centerHorizontal="true"
    android:textSize="16sp"
    android:layout_marginTop="30dp"/>

</RelativeLayout>
```
Then, we can write the Java code of the application. The main part will be the method used to create the connection to the WebSocket connection and the WebSocketListener object used to exchange with the server :
```java
private final class EchoWebSocketListener extends WebSocketListener {
  private static final int NORMAL_CLOSURE_STATUS = 1000;

  @Override
  public void onOpen(WebSocket webSocket, Response response) {
    webSocket.send("Hello, it's SSaurel !");
    webSocket.send("What's up ?");
    webSocket.send(ByteString.decodeHex("deadbeef"));
    webSocket.close(NORMAL_CLOSURE_STATUS, "Goodbye !");
  }

  @Override
  public void onMessage(WebSocket webSocket, String text) {
    output("Receiving : " + text);
  }

  @Override
  public void onMessage(WebSocket webSocket, ByteString bytes) {
    output("Receiving bytes : " + bytes.hex());
  }

  @Override
  public void onClosing(WebSocket webSocket, int code, String reason) {
    webSocket.close(NORMAL_CLOSURE_STATUS, null);
    output("Closing : " + code + " / " + reason);
  }

  @Override
  public void onFailure(WebSocket webSocket, Throwable t, Response response) {
    output("Error : " + t.getMessage());
  }
}
```

We send messages to the server in the onOpen method. The messages received from the Echo WebSocket server are displayed inside the onMessage method. Note that you can send text or hexadecimal messages. Lastly, we close the connection by using the close method of the WebSocket object. To create the WebSocket connection with OkHttp, we need to build a Request object with the URL of the Echo WebSocket server in parameter, then calling the newWebSocket method of the OkHttpClient object.

The code will have the following form :
```java
package com.ssaurel.websocket;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class MainActivity extends AppCompatActivity {

  private Button start;
  private TextView output;
  private OkHttpClient client;

  private final class EchoWebSocketListener extends WebSocketListener {
    private static final int NORMAL_CLOSURE_STATUS = 1000;

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
      webSocket.send("Hello, it's SSaurel !");
      webSocket.send("What's up ?");
      webSocket.send(ByteString.decodeHex("deadbeef"));
      webSocket.close(NORMAL_CLOSURE_STATUS, "Goodbye !");
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
      output("Receiving : " + text);
    }

    @Override
    public void onMessage(WebSocket webSocket, ByteString bytes) {
      output("Receiving bytes : " + bytes.hex());
    }

    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
      webSocket.close(NORMAL_CLOSURE_STATUS, null);
      output("Closing : " + code + " / " + reason);
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
      output("Error : " + t.getMessage());
    }
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    start = (Button) findViewById(R.id.start);
    output = (TextView) findViewById(R.id.output);
    client = new OkHttpClient();

    start.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        start();
      }
    });

  }

  private void start() {
    Request request = new Request.Builder().url("ws://echo.websocket.org").build();
    EchoWebSocketListener listener = new EchoWebSocketListener();
    WebSocket ws = client.newWebSocket(request, listener);

    client.dispatcher().executorService().shutdown();
  }

  private void output(final String txt) {
    runOnUiThread(new Runnable() {
      @Override
      public void run() {
        output.setText(output.getText().toString() + "\n\n" + txt);
      }
    });
  }
}
```


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
