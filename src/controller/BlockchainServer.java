package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import utility.Block;
import utility.Blockchain;
import utility.Ledger;
import utility.Transaction;

/**
 * Servlet implementation class BlockchainServer
 */
@WebServlet("/BlockchainServer")
public class BlockchainServer extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Blockchain bl;  
	Ledger l;
	Block b;
	Transaction t;
    public BlockchainServer() {
    	Block b=new Block();
       bl=new Blockchain(b);
       l=new Ledger(bl);
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		PrintWriter p=response.getWriter();
		String data=request.getParameter("data");
		p.print("Received:"+data);
		
		System.out.println("Connected");
		System.out.println("Received request from:"+request.getRemoteAddr());
		System.out.println("Received data:"+data);
		
		String m=request.getParameter("req");
		if(m!=null){
			
		if(m.equalsIgnoreCase("'file'")){
			t=new Transaction("",request.getRemoteAddr(),data);
			System.out.println("Transaction created");
			b=new Block(t,new Date().toString(),t.data.hashCode());
			System.out.println("Block Created");
			bl.addBlock(b, t);
			System.out.println("Block Added");
			l.createLedger();
		}}
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
	}

}
