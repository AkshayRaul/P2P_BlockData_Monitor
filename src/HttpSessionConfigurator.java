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
    Map<String,List<String>> headers=request.getHeaders();
    Set<String> keys=headers.keySet();
    for(String key:keys){
      LOGGER.info(key);
      for (Iterator<String> iter = (headers.get(key)).iterator(); iter.hasNext(); ) {
        String val = iter.next();
        LOGGER.info(val);
        
      }
      
    }
    HttpSession httpSession = (HttpSession)request.getHttpSession();
    config.getUserProperties().put(HttpSession.class.getName(),httpSession);
    LOGGER.info(HttpSession.class.getName());
  }

}
