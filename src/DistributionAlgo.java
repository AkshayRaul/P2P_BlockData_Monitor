
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Collections;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.HashMap;

//import java.io.IOException;
//import java.util.Date;
import javax.websocket.Session;

/**
* Servlet implementation class DistributionAlgo
*/

// class SortByStorage implements Comparator<Session>{
// 	// Used for sorting in ascending order of
// 	// storage
// 	public int compare(Session a, Session b){
// 		return a.storage - b.storage;
// 	}
// }
//
// class SortByRating implements Comparator<Session>{
// 	// Used for sorting in ascending order of
// 	// rating
// 	public double compare(Session a, Session b){
// 		return a.rating - b.rating;
// 	}
// }
// class SortByOnlinePercent implements Comparator<Session>{
// 	// Used for sorting in ascending order of
// 	// onlinePercent
// 	public double compare(Session a, Session b){
// 		return a.onlinePercent - b.onlinePercent;
// 	}
// }

public class DistributionAlgo  {
	private int storage;
	private double rating;
	private double onlinePercent;
	private ArrayList<Session> clients;
	private Session bestPeersStorage[]=new Session[5];
	private Session bestPeersRating[]=new Session[5];
	DistributionAlgo(){

	}

	DistributionAlgo(int storage,double rating,double onlinePercent){
		this.storage=storage;
		this.rating=rating;
		this.onlinePercent=onlinePercent;
	}
	public String distribute(String appId){
		HashMap<String,DistributionAlgo> clientData=WebSocketServer.getClientData();
		clients=WebSocketServer.getOpenSessions();
		// Collections.sort(clients,new SortByRating());
		// for(int i=0;i<5;i++){
		// 	bestPeersRating[i]=clients.get(i);
		// }
		// for(int i=0;i<5;i++){
		// 	bestPeersStorage[i]=clients.get(i);
		// }
		String ret=(String)clients.get(0).getUserProperties().get("userId");
		return ret;
	}

}
