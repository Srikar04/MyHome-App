<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>MyHome App</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous">
</head>
<style>
.custom-margin {
    margin-right: 100px; /* Adjust the value as needed */
}
</style>
<body>
	<jsp:include page="header.jsp" />
	 <div class="container mt-4">
		 <h1>Welcome <%=session.getAttribute("userName") %></h1>
		 <br>
	    <h2>Pending Requests</h2>
	    <div class="row">
        	<div class="col-lg-4 col-md-6 mb-4">
	            <%=request.getAttribute("requests") %>
        	</div>	
       </div>
        <button type="submit" class="btn btn-primary" onclick="window.location.href='fetchDues'">Show My Dues</button>
        <br><br>
        <button type="submit" class="btn btn-primary" onclick="window.location.href='fetchRegularVisitors'">Regular Visitors History</button>
       <br><br>
        <div class="error">
            <%
                String error = (String) request.getAttribute("errorMessage");
                if (error != null && !error.isEmpty()) {
            %>
                <%= error %>
            <%
                }
            %>
        </div>
	</div>	
<br><br><br><br><br><br><br>
<jsp:include page="footer.jsp" />
</body>
</html>