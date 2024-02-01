<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>MyHome App</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous">
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
<body>
    <jsp:include page="header.jsp" />
    <div class="container">
        <h1 class="mt-4">
            <%
                String action = request.getParameter("action");
            %>
            <%= "checkin".equals(action) ? "Check-In" : "checkout".equals(action) ? "Check-Out" : "Invalid Action" %>
        </h1>
        <form id="timeForm" action="addTime" method="post">
            <div class="form-group">
                <label for="securityCode">Security Code</label> <br>
                <input type="text" class="form-control" id="securityCode" name="securityCode" required>
            </div>
            <div class="form-group">
                <label for="flatNumber">Flat Number</label> <br>
                <input type="text" class="form-control" id="flatNumber" name="flatNumber" required>
            </div>
            <input type="hidden" name="action" value="<%= action %>">
            <br>
			<div class="error">
                 <% String error = (String) request.getAttribute("errorMessage"); %>
                 <% if (error != null && !error.isEmpty()) { %>
                     <%= error %>
                 <% } %>
             </div>
            <button type="button" class="btn btn-primary" id="submitButton">
                <%= "checkin".equals(action) ? "Check-In" : "checkout".equals(action) ? "Check-Out" : "" %>
            </button>

        </form>
    </div>
    <br><br><br><br>
    <jsp:include page="footer.jsp" />
    <script>
        document.getElementById("submitButton").addEventListener("click", function() {
            var action = "<%= action %>";
            var form = document.getElementById("timeForm");
            if (action === "checkin" || action === "checkout") {
                form.submit();
            } else {
                alert("Invalid Action");
            }
        });
    </script>
</body>
</html>
