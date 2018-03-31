<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
pageEncoding="ISO-8859-1" import="java.io.*,java.util.Arrays, java.net.*"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <style>
table, th, td {
    border: 1px solid black;
}
</style>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>JSP Reading Text File</title>
</head>

<body>
  <table>


<%
String fileName = "C:/Users/SHWETHA/Desktop/apache-tomcat-8.5.23/data/Blockchain/blockchain.csv";
out.println(fileName);
InputStream ins = application.getResourceAsStream(fileName);
String block="";
try{
  File f=new File(fileName);
  byte bytes[]=new byte[(int)f.length()];
  FileInputStream fis=new FileInputStream(f);
  fis.read(bytes);
  String s=new String(bytes);
  String blocks[]=s.split("\n");
  for(int j=0;j<blocks.length;j++){
    String columns[]=blocks[j].split(",");
  %>   <tr>
  <%  for(int i=0;i<7;i++){%>

      <td><%= columns[i] %></td>

  <%  }%>
  </tr>
    <%
    }
}
catch(Exception e){
  e.printStackTrace();
}
%>
</table>
</body>
</html>
