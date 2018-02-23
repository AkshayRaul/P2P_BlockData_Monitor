import javax.servlet.http.*;
import javax.servlet.*;
import java.io.*;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
// import com.mongodb.MongoClient;
// import com.mongodb.MongoClientURI;
// import com.mongodb.ServerAddress;
//
// import com.mongodb.client.*;
// import com.mongodb.client.MongoCollection;
// import org.bson.Document;
// import com.mongodb.*;
public class SignUpServlet extends HttpServlet{
	//boolean auth;MongoClient mongo;  MongoDatabase db;

	public void doGet(HttpServletRequest req,HttpServletResponse res) throws ServletException,IOException
	{
		res.setContentType("text/html");//setting the content type
		PrintWriter pw=res.getWriter();//get the stream to write the data
		String username=req.getParameter("user");
		String pass=req.getParameter("pass");
		String email=req.getParameter("email");
		String contact=req.getParameter("contact");

 		String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 10) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
		//writing html in the stream

		pw.println(saltStr);
		pw.close();//closing the stream
		/*try{
			MongoCredential credential;
      		credential = MongoCredential.createCredential("kshitijyerande", "blockchain","Blockdata6".toCharArray());
			MongoClientOptions clientOptions = new MongoClientOptions.Builder().serverSelectionTimeout(50000).build();
            mongo = new MongoClient(new ServerAddress("mongodb://Blockchain:Blockchain@ds239988.mlab.com:39988/blockchain"),Arrays.asList(credential),clientOptions);
			System.out.println("connection successfull");

      		System.out.println("Connected to the database successfully");
			}
			catch(Exception e){
				e.printStackTrace();
			}

		try{
			db=mongo.getDatabase("blockchain");
			 MongoCollection<Document> user = db.getCollection("User");
			System.out.println("database accessed");
			System.out.println("collection accessed");

			Document document = new Document("id", saltStr)
      .append("username", username)
      .append("password", pass)
      .append("email", email)
      .append("contact", contact);
      user.insertOne(document);
      System.out.println("Document inserted successfully");*/

      //file insertion
      FileWriter fw=null;
      BufferedWriter bw=null;
      File f=null;
      String path="E:/apache/apache-tomcat-8.0.30/webapps/Blockchain/src/User.txt";
      try{
      		f=new File(path);
      		if(f.exists()){
      			System.out.println("file exists");
      			fw = new FileWriter(path,true);
      			 bw = new BufferedWriter(fw);
      			bw.write(saltStr+","+username+","+pass+","+email+","+contact+"\n");


      		}else{
      			f.createNewFile();
      			System.out.println("File created");
      			fw = new FileWriter(path);
      			 bw= new BufferedWriter(fw);
      			bw.write(saltStr+","+username+","+pass+","+email+","+contact+"\n");

      		}
      }
      catch(Exception e){
			e.printStackTrace();
	   }
		finally{
			bw.close();
			fw.close();

		}






	}
}
