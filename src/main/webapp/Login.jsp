<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>MyHome</title>
<style>
h1{	
	text-align:center;
}
#error-message{
	color:red;
}
</style>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous">
</head>
<body>
	<jsp:include page="header.jsp" />
	<section class="vh-100">
	  <div class="container py-5 h-100">
	    <div class="row d-flex align-items-center justify-content-center h-100">
	      <div class="col-md-8 col-lg-7 col-xl-6">
	        <img src="https://mdbcdn.b-cdn.net/img/Photos/new-templates/bootstrap-login-form/draw2.svg" class="img-fluid" alt="Phone image">
	      </div>
	      <div class="col-md-7 col-lg-5 col-xl-5 offset-xl-1">
	        <form id="login-form" action="login" method="post">
		          <div class="form-group mb-4">
		            <input type="text" class="form-control form-control-lg" name="username" id="username" placeholder="Username" required>
		          </div>
		
		          <div class="form-group mb-4">
		            <input type="password" class="form-control form-control-lg" name="password" id="password" placeholder="Password" required>
		          </div>
				
				  <input type="hidden" name="tableName" id="tableName">
				  
		          <button type="submit" class="btn btn-primary btn-lg btn-block">Sign in</button>
	        </form>
	        <div id="error-message">
		        <!-- This is where the error message will be displayed -->
		    </div>  
	      </div>
	    </div>
	  </div>
	</section>
	
	<script>
	  document.addEventListener('DOMContentLoaded', function () {
	    var form = document.getElementById('login-form');
	    form.addEventListener('submit', function (event) {
	      var username = document.getElementById('username').value;
	      var password = document.getElementById('password').value;
	      var tableName = "Residents";
	
	      const atIndex = username.indexOf('@');
	      var name = null;
	      if(atIndex >0){
	    	  name = username.substring(0,atIndex);
	    	  document.getElementById('username').value = username.substring(atIndex+ 1);
	      }

	      if (name === "admin") {
	        tableName = "Administrator";
	      } else if (name === "super") {
	        tableName = "Supervisor";
	      }
	      document.getElementById('tableName').value = tableName;
	    });
	  });
	</script>
	
	<% String errorMessage = (String) request.getAttribute("errorMessage"); 
	    if (errorMessage != null) { %>
	    <script>
	        var errorMessageElement = document.getElementById('error-message');
	        errorMessageElement.innerHTML = '<%= errorMessage %>';
	    </script>
	<% } %>
	
	<jsp:include page="footer.jsp" />

</body>
</html>