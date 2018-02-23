# P2P_BlockData_Monitor
This repository will host the monitoring system for BlockData

All you need to know about Java Websockets API, Please study these classes carefully. This is much better than referring other websites. Please understand the objects, their return types and play with the code.

[https://docs.oracle.com/javaee/7/api/javax/websocket/package-summary.html](https://docs.oracle.com/javaee/7/api/javax/websocket/package-summary.html "JAVA EE WebSockets Specification")

# Required jars

https://jar-download.com/explore-java-source-code.php?a=jjwt&g=io.jsonwebtoken&v=0.9.0&downloadable=1


http://www.java2s.com/Code/Jar/j/Downloadjsonjar.htm <br>
OR<br>
http://www.java2s.com/Code/Jar/j/Downloadjsonsimple11jar.htm


[![Build Status](https://img.shields.io/circleci/project/github/ntkme/github-buttons.svg)](https://travis-ci.org/Netflix/fast_jsonapi)


# ToDo List
----------
+ ~~Send file data to server as per Specification below~~
+ Create a service for the app which runs even after app is Closed
+ ~~Handle nanoHTTP Server errors~~
+ ~~Authentication using key exchange~~
+ Server Side Authentication
+ ~~Create Blockchain and save data on a file/csv/json~~
+ Send file permissions
+ ~~Broadcast ledgers~~
+ Check ledgers

## File Upload format

Prefix ```11``` while uploading file to server
<br>Server changes ```11``` to ```00``` while distributing file
<br> Use Prefix ```01``` while broadcasting ledger from server

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
# @onMessage requirements
--------

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
