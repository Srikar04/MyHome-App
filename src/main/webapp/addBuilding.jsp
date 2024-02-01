<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>MyHome App</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous">
<style>
     .form-control {
          width:60%;
          display: inline-block;
      }
      
      .error{
      	color:red;
      	margin-bottom:1rem;
      }
</style>
</head>
<body>
	<jsp:include page="header.jsp" />
	<div class="container">
        <h1 class="mt-4">Add New Building</h1>
        <form action="addBuilding" method="post">
            <div class="form-group">
                <label for="buildingName">Building Name</label> <br>
                <input type="text" class="form-control" id="buildingName" name="buildingName" required>
            </div>
            <div class="form-group">
                <label for="constructionYear">Construction Year</label> <br>
                <input type="number" class="form-control" id="constructionYear" name="constructionYear" required>
            </div>
            <div class="form-group">
                <label for="area in meter square">Area (in m2)</label> <br>
                <input type="number" class="form-control" id="area" name="area" required>
            </div>
            <br>
            
            <div class = "form-group">
	            <div class="error">
					<%String error = (String)request.getAttribute("errorMessage") ;%>
					<%if(error != null && !error.isEmpty()){%>
				    <%= error %>
				    <% } %>
				</div>
            </div>
            <button type="submit" class="btn btn-primary">Add Building</button>
        </form>
    </div>
	
    <br><br>
	<jsp:include page="footer.jsp" />
</body>
</html>