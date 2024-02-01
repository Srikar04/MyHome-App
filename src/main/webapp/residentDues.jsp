<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Resident Dues</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous">
</head>
<body>
<jsp:include page="header.jsp" />
    <div class="container mt-4">
        <h1>Resident Dues</h1>
        <div>
            <div class="row">
        	<div class="col-lg-4 col-md-6 mb-4">
	            <%=request.getAttribute("dues") %>
        	</div>	
       </div>
        </div>
    </div>
    <% String errorMessage = (String) request.getAttribute("errorMessage"); 
	    if (errorMessage != null) { %>
	    <script>
	        var errorMessageElement = document.getElementById('error-message');
	        errorMessageElement.innerHTML = '<%= errorMessage %>';
	    </script>
	<% } %>
    <br><br><br><br><br><br>
<jsp:include page="footer.jsp" />
</body>
</html>
