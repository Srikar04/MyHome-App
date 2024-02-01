<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Add Resident</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
    <style>
        .form-control {
            width: 60%;
            display: inline-block;
        }
        .error {
            color: red;
            margin-bottom: 1rem;
        }
    </style>
</head>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
<body>
    <jsp:include page="header.jsp" />
    <div class="container">
        <h1 class="mt-4">Add Resident</h1>
        <form action="addResident" method="post" id="checkout-form">
            <div class="form-group">
                <label for="residentName">Resident Name</label> <br>
                <input type="text" class="form-control" id="residentName" name="residentName" required>
            </div>
            <div class="form-group">
                <label for="numOfPeople">Number of People</label> <br>
                <input type="number" class="form-control" id="numOfPeople" name="numOfPeople" min="1" required>
            </div>
            <div class="form-group">
                <label for="occupation">Occupation</label> <br>
                <input type="text" class="form-control" id="occupation" name="occupation" required>
            </div>
            <div class="form-group">
                <label for="contactNumber">Contact Number(10 digit Number)</label> <br>
                <input type="text" class="form-control" id="contactNumber" name="contactNumber" required>
            </div>
            <div class="form-group">
                <label for="flatNumber">Flat Number</label> <br>
                <input type="text" class="form-control" id="flatNumber" name="flatNumber" required>
            </div>
            <!-- <div class="form-grourp">
				    <label for="flatNumber">Flat Number:</label>
				    <div id="selectContainer"></div>
            </div> -->
            <div class="form-group">
                <label for="password">Password</label> <br>
                <input type="text" class="form-control" id="password" name="password" required>
            </div>
            <br>
            <div class="form-group">
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
            <button type="submit" class="btn btn-primary">Add Resident</button>
        </form>
    </div>
    <br><br>
    <jsp:include page="footer.jsp" />
    <script>
    $(document).ready(function() {
        const form = $("#checkout-form");

        form.on("submit", function(event) {
            if (!validateForm()) {
                event.preventDefault();
            }
        });
        
        function validateForm() {
        	const phone = $("#contactNumber").val().trim();
        	 var numbersOnly = /^[0-9]+$/;
        	 if (phone === "" || !phone.match(numbersOnly) || phone.length !== 10) {
                 alert("Phone number must be a 10 digit number.");
                 return false;
             }
        	 return true;
        }
        
        $.ajax({
            url: 'fetchFlatInfo',
            method: 'GET',
            dataType: 'html',
            success: function(data) {
            	console.log(data);
                $('#selectContainer').html(data);
            },
            
            error: function(xhr, status, error) {
                console.error('Error fetching shopkeepers: ' + error);
            }
        });
        
    })
    </script>
</body>
</html>
