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
		<h2>Regular Visitor</h2>
       <button type="button" class="btn btn-info" onclick="window.location.href='regularVisitor.jsp'">Add a Regular Visitor</button>
       <button type="button" class="btn btn-success" onclick="window.location.href='addTime.jsp?action=checkin'">CheckIn</button>
		<button type="button" class="btn btn-warning" onclick="window.location.href='addTime.jsp?action=checkout'">CheckOut</button>
		<br>
		<h2>Normal Visitor</h2>
       <button type="button" class="btn btn-info" onclick="window.location.href='normalVisitor.jsp'">Add a Normal Visitor</button>
    </div>
	<br><br><br><br><br><br><br><br>
	<jsp:include page="footer.jsp" />
</body>
</html>