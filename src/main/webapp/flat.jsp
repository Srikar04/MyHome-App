<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous">
<title>MyHome App</title>
</head>
<body>
	<jsp:include page="header.jsp" />
		<div class="container">
		<br>
        <h1>Building Information</h1>
        <div class="row">
            <div class="col-9">
                <%= request.getAttribute("flatTable") %>
            </div>
        </div>
    </div>
	<br><br><br><br><br><br><br>
	<jsp:include page="footer.jsp" />
</body>
</html>