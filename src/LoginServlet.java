import javax.servlet.http.*;
import javax.servlet.*;
import java.io.*;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse; 
public class LoginServlet extends HttpServlet{
	//boolean auth;MongoClient mongo;  MongoDatabase db;
	
	public void doGet(HttpServletRequest req,HttpServletResponse res) throws ServletException,IOException
	{  System.out.println("do get called");
		boolean flag=false;
		String username=req.getParameter("user");
		String pass=req.getParameter("pass");
		PrintWriter p=res.getWriter();
		try{
		Scanner scanner = new Scanner(new File("/E:/apache/apache-tomcat-8.0.30/webapps/Blockchain/src/User.txt"));
			while (scanner.hasNextLine()) {
				String[] data=scanner.nextLine().split(",");

				if(data[1].equalsIgnoreCase(username) && data[2].equalsIgnoreCase(pass)){
					flag=true;
					p.println("Succesfull");
				}
			}
			if(!flag){
				
				p.println("Username and Password doesnt match");
			}
			scanner.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}