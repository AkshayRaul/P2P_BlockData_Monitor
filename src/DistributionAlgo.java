
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;

//import java.io.IOException;
//import java.util.Date;

/**
* Servlet implementation class DistributionAlgo
*/
public class DistributionAlgo  {
	private Integer storage;
	private double rating;
	private double onlinePercent;
	private Set<Session> clients;

	DistributionAlgo(int storage,double rating,double onlinePercent){
		this.storage=storage;
		this.rating=rating;
		this.onlinePercent=onlinePercent;
	}
	static String distribute(){
		this.clients=WebSocketServer.getOpenSessions();
		
		return "";
	}

}
