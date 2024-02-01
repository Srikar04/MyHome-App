<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
  .navbar-brand {
    color: white; 
    font-size: 24px; 
  }
  #myHomeAppLink:hover {
	  cursor: pointer;
  }
  
</style>
</head>
<body>
	<nav class="navbar bg-primary">
	  	<div class="container-fluid">
	    <a class="navbar-brand"  id="myHomeAppLink">MyHome App</a>
	    <form class="d-flex"  action="logOut" method="GET">
		    <input type="submit" value="Log Out" id="logout" class="btn btn-danger">
		</form>

	  </div>
	</nav>
	<script>
	 document.addEventListener("DOMContentLoaded", function() {
	        var userName = "<%= session.getAttribute("userName") %>";

	        var logout = document.getElementById("logout");
				        
	        if(userName === 'null'){
	            logout.style.visibility= 'hidden';
	            var currentLocation = window.location.href;
	            // if not on the home page redirect to home page
	            if (!currentLocation.includes("Login.jsp")) {
	            	if (!currentLocation.includes("login"))
	                	window.location.href = "Login.jsp";
	            }
	        }else{
	        	var myHomeAppLink = document.getElementById("myHomeAppLink");
	        	 var userType = "<%= session.getAttribute("userType") %>";
	        	var url = "";
	        	if(userType === "Administrator"){
	        		url = "adminHome.jsp";
	        	}else if(userType === "Supervisor"){
	        		url = "superHome.jsp";
	        	}else{
	        		url = "residentHome.jsp";
	        	}
	        	myHomeAppLink.addEventListener("click", function(event) {
	        	    window.location.href = url;
	        	  });
	        }
	 });
	</script>
</body>
</html>