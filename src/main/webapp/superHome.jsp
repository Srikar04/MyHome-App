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
	 <div class="container mt-4">
		 <h1>Welcome <%=session.getAttribute("userName") %></h1>
		 <br>
	    <h2>Supervisor Options</h2>
	    <div class="row">
	      <div class="col-md-4">
	        <div class="card" style="margin-bottom: 20px;">
	          <div class="card-body">
	            <h5 class="card-title">Access Building Information</h5>
	            <p class="card-text">Click here to view info about buildings.</p>
	            <a href="fetchBuilding" class="btn btn-primary">Access Building</a>
	          </div>
	        </div>
	      </div>
	      <div class="col-md-4">
	        <div class="card" style="margin-bottom: 20px;">
	          <div class="card-body">
	            <h5 class="card-title">Access Flat Information</h5>
	            <p class="card-text">Click here to view info about all flats.</p>
	            <a href="fetchFlat" class="btn btn-primary">Access Flats</a>
	          </div>
	        </div>
	      </div>
	      <div class="col-md-4">
		    <div class="card">
		      <div class="card-body">
		        <h5 class="card-title">Add Resident</h5>
		        <p class="card-text">Click here to add a resident.</p>
		        <a href="resident.jsp" class="btn btn-primary">Add Resident</a>
		      </div>
		    </div>
		 </div>
	    <div class="col-md-4">
	    <div class="card">
	      <div class="card-body">
	        <h5 class="card-title">Upload and Access Visitor Details</h5>
	        <p class="card-text">Click here to upload and access visitor details.</p>
	        <a href="visitor.jsp" class="btn btn-primary">Visitor Details</a>
	      </div>
	    </div>
	  </div>
	  <div class="col-md-4">
	    <div class="card">
	      <div class="card-body">
	        <h5 class="card-title">Upload and Access Staff Details</h5>
	        <p class="card-text">Click here to upload and access staff details.</p>
	        <a href="staffDetails.jsp" class="btn btn-primary">Staff Details</a>
	      </div>
	    </div>
	 </div>
	 <div class="col-md-4">
	    <div class="card">
	      <div class="card-body">
	        <h5 class="card-title">Update Maintenance details</h5>
	        <p class="card-text">Click here to update maintenance.</p>
	        <a href="Maintenance.jsp" class="btn btn-primary">Update Maintenance</a>
	      </div>
	    </div>
	 </div>
	</div>
</div>
<br><br><br><br><br><br><br>
<jsp:include page="footer.jsp" />
</body>
</html>