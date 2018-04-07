package blokdata;

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
//
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
// 	public int compare(Session a, Session b){
// 		a.getUserProperties().get("userId");
// 	}
// }
// class SortByOnlinePercent implements Comparator<Session>{
// 	// Used for sorting in ascending order of
// 	// onlinePercent
// 	public int compare(Session a, Session b){
// 		return (int)(a.onlinePercent - b.onlinePercent);
// 	}
// }

public class DistributionAlgo  {
	long storage;
	double rating;
	double onlinePercent;
	private static long MAX_STORAGE=5000000;
	private ArrayList<Session> clients;
	private Session bestPeersStorage[]=new Session[5];
	private Session bestPeersRating[]=new Session[5];
	DistributionAlgo(){

	}

	DistributionAlgo(long storage,double rating,double onlinePercent){
		this.storage=storage;
		this.rating=rating;
		this.onlinePercent=onlinePercent;
	}
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

}
