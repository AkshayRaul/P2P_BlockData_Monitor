package utility;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Ledger {
	Blockchain blockchain;
	public Ledger(Blockchain b){
		blockchain=b;
	}
	public void createLedger(){
		//blockchain.block=blockchain.block.next;
		BufferedWriter bw = null;
		FileWriter fw = null;

		try {
			String content="";
			blockchain.block=blockchain.head.next;
			while(blockchain.block!=null){

				content +=blockchain.block.transaction.toString()+","+blockchain.block.timestamp+","+blockchain.block.hash+"\n";
				blockchain.block=blockchain.block.next;
			}
			fw = new FileWriter("E:\\k.txt");
			bw = new BufferedWriter(fw);
			bw.write(content);

			System.out.println("Done");

		} catch (IOException e) {

			e.printStackTrace();

		}finally {

			try {

				if (bw != null)
					bw.close();

				if (fw != null)
					fw.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}

		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Transaction t=new Transaction("","132","5");
		Transaction t1=new Transaction("","132","10");
		Transaction t2=new Transaction("","132","100");
		Block b=new Block(t, "23",t.data.hashCode());
		Block b1=new Block(t1, "123",t1.data.hashCode());
		Block b2=new Block(t2, "143",t2.data.hashCode());
		Blockchain bl=new Blockchain(b);
		bl.addBlock(b1, t1);
		bl.addBlock(b2, t2);
		Ledger l=new Ledger(bl);
		l.createLedger();
	}

}
