<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
pageEncoding="ISO-8859-1" import="java.io.*,java.util.*, java.net.*, blokdata.*,BlockStats.*,WebSocketServer.*" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" type="text/css" href="https://stackpath.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>JSP Reading Text File</title>
<link rel="stylesheet" href="css/bootstrap.min.css" type="text/css">
       <link rel="stylesheet" href="css/animate.min.css" type="text/css">

<!-- Custom CSS -->
<link rel="stylesheet" href="css/creative.min.css" type="text/css">
<style>
    .holder{
        font-family:serif;
        font-size:20px;
    }
    .fas{
        width:30%;
        heigth:100%;
    }
    .holder>.stat-text{
        display:inline-block;
        width:70%;
        margin-top:0;
        position:absolute;
        padding: 1% 0% 0% 4%;
    }
    .holder>.stat-num{
        display:inline-block;
        width:70%;
        margin-top:0;
        padding: 1% 0% 0% 4%;
    }

</style>
<!-- Custom Fonts -->
<script defer src="https://use.fontawesome.com/releases/v5.0.9/js/all.js" integrity="sha384-8iPTk2s/jMVj81dnzb/iFR2sdA7u06vHJyyLlAd4snFpCl/SnyUjRrbdJsw1pGIl" crossorigin="anonymous"></script>
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
        <div class="row">
            <div class="col-lg-4 col-sm-12 col-md-6 holder">
                <i class="fa fa-4x fa-file"></i>

                <span class="stat-text">Files Shared</span>
                <span class="stat-num"><%= BlockStats.getFilesCount() %></span>

            </div>
            <div class="col-lg-4 col-sm-12 col-md-6 holder">
                <i class="fas fa-4x fa-clock"></i>

                <span class="stat-text">Average Latency</span>
                <span class="stat-num"><%= BlockStats.getAvgLatency() %>ms</span>

            </div>
            <div class="col-lg-4 col-sm-12 col-md-6 holder">
                <i class="fas fa-4x fa-mobile"></i>

                <span class="stat-text">Devices Online</span>
                <span class="stat-num"><%= BlockStats.onlineDevices %></span>

            </div>
        </div>


<%
try{
  BlockchainServer bcs=WebSocketServer.getServer();
  List<Blockchain> bc=bcs.getAllAgents();
  for(int i=0;i<bc.size();i++){
      List<Block> blocks=bc.get(i).getBlockchain();
      %>

      <table class="table table-responsive">
      <h3><%=  bc.get(i).getName() %></h3>
          <thead class="thead-dark">
              <th>Mode</th>
              <th>Index</th>
              <th>Timestamp</th>
              <th>Curr Hash</th>
              <th>Prev Hash</th>
              <th>Sender</th>
              <th>Peer</th>
              <th>FileId</th>
          </thead>
        <%
        for(int j=1;j<blocks.size();j++){
            %>
             <tr class="alert alert-success">

          <td><%= blocks.get(j).getMode()%></td>
          <td><%= blocks.get(j).getIndex()%></td>
          <td><%= blocks.get(j).getTimestamp()%></td>
          <td ><%= blocks.get(j).getHash()%></td>
          <td><%= blocks.get(j).getPreviousHash()%></td>
          <td><%= blocks.get(j).getCreator()%></td>
          <td><%= blocks.get(j).getPeer()%></td>
          <td><%= blocks.get(j).getFileId()%></td>
          </tr>
        <%
      }

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
