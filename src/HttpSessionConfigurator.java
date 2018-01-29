import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;
import java.io.*;
import java.util.*;
import java.lang.*;

public class HttpSessionConfigurator extends ServerEndpointConfig.Configurator {

  @Override
  public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
    System.out.println("modifyHandshake() Current thread " + Thread.currentThread().getName());
    System.out.println(request.SEC_WEBSOCKET_KEY);
    Map<String,List<String>> headers=request.getHeaders();
    Set<String> keys=headers.keySet();
    for(String key:keys){
      System.out.println(key);
      for (Iterator<String> iter = (headers.get(key)).iterator(); iter.hasNext(); ) {
        String val = iter.next();
        System.out.println(val);
        // 1 - can call methods of element
        // 2 - can use iter.remove() to remove the current element from the list

        // ...
      }
    }
    //String user = request.getParameterMap().get("ws").get(0);
    //sec.getUserProperties().put(user, request.getHttpSession());
    // System.out.println("modifyHandshake() User " + user + " with http session ID " + ((HttpSession) request.getHttpSession()).getId());
  }

}
