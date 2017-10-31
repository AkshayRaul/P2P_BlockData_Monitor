package utility;

public class Block{
	Transaction transaction;
	String timestamp;
	int hash;
	Block next;
	public Block(){
		
	}
	public Block(Transaction t,String timestamp,int hash){
		this.transaction=t;
		this.timestamp=timestamp;
		this.hash=hash;
	}
	public static void main(String[] args){
		
	}
}
