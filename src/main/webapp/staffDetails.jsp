<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>MyHome App</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous">
</head>
<body>
	<jsp:include page="header.jsp" />
		<div class="container">
			<br>
			<h2>Add a new Staff </h2>
	       <button type="button" class="btn btn-info" onclick="window.location.href='addStaff.jsp'">Add Staff</button>
			<br>
			<h2>View existing Staff</h2>
	       <button type="button" class="btn btn-info" onclick="window.location.href='fetchStaff'">Access Staff Details</button>
	       <% if (request.getAttribute("staffTable") != null) { %>
	            <h3>Staff Details</h3>
	             <%= request.getAttribute("staffTable") %>
	        <% } %>
    	</div>
	<br><br><br><br><br><br><br><br>
	<jsp:include page="footer.jsp" />
</body>

</html>