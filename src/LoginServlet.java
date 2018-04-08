package blokdata;
import javax.servlet.http.*;
import javax.servlet.*;
import java.io.*;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import java.security.Key;
import io.jsonwebtoken.impl.crypto.MacProvider;
import io.jsonwebtoken.impl.compression.*;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Claims;
import javax.xml.bind.DatatypeConverter;
import io.jsonwebtoken.SignatureException;

public class LoginServlet extends HttpServlet{
	//boolean auth;MongoClient mongo;  MongoDatabase db;

	public void doGet(HttpServletRequest req,HttpServletResponse res) throws ServletException,IOException
	{  //System.out.println("do get called");
		System.out.println("REQUEST");

		boolean flag=false;
		String username=req.getParameter("user");
		String pass=req.getParameter("pass");
		PrintWriter p=res.getWriter();
		try{
		Scanner scanner = new Scanner(new File("C:/Users/SHWETHA/Desktop/apache-tomcat-8.5.23/webapps/Blockchain/src/User.txt"));
			while (scanner.hasNextLine()) {
				String[] data=scanner.nextLine().split(",");
				if(data[1].equalsIgnoreCase(username) && data[2].equalsIgnoreCase(pass)){
					flag=true;
					String compactJws = Jwts.builder()
					  .claim("Username",data[1])
					  .signWith(SignatureAlgorithm.HS256,"Secret")
					  .compact();
						System.out.println(compactJws);
					  p.print(compactJws.trim());

					    // try {
						//
						//     Claims claim=Jwts.parser().setSigningKey("Secret").parseClaimsJws(compactJws).getBody();
						//     String u=(String)claim.get("Username");
						//       p.print("verified");
						//
						//
						//     //OK, we can trust this JWT
						//
						// } catch (SignatureException e) {
						// 	p.println("not verified");
						//
						//     e.printStackTrace();
						// }
				}
			}
			if(!flag){
				p.print("Unauthorized");
			}
			scanner.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
