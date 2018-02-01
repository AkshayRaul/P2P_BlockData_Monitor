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
