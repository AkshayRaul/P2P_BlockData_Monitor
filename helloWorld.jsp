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
<link rel="stylesheet" type="text/css" href="https://stackpath.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>JSP Reading Text File</title>
<link rel="stylesheet" href="css/bootstrap.min.css" type="text/css">
       <link rel="stylesheet" href="css/animate.min.css" type="text/css">

   <!-- Custom CSS -->
   <link rel="stylesheet" href="css/creative.min.css" type="text/css">

            <!-- Custom Fonts -->
  <link rel="stylesheet" href="css/font-awesome/css/font-awesome.min.css" type="text/css">

</head>
<header style="background-image: url('WebContent/code.jpg'); image-rendering: optimize-contrast ;">

        <div class="header-content">
                <div class="header-content-inner">
                        <h1>Blockchain Check System</h1><br>
                </div>
        </div>
  </header>

<body>
    <br>
    <div class="container-fluid">
    <table class="table table-responsive">
        <thead class="thead-dark">
            <th>Index</th>
            <th>Timestamp</th>
            <th>Prev Hash</th>
            <th>Curr Hash</th>
            <th>Sender</th>
            <th>Peer</th>
            <th>FileId</th>
        </thead>

<%
String fileName = "/opt/tomcat/data/Blockchain/blockchain.csv";
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
</div>
</body>
</html>
