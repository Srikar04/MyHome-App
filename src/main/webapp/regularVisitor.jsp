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
        <h1 class="mt-4">Add Regular Visitor</h1>
        <form action="addRegularVisitor" method="post">
            <div class="form-group">
                <label for="visitorName">Visitor Name</label> <br>
                <input type="text" class="form-control" id="visitorName" name="visitorName" required>
            </div>
            <div class="form-group">
                <label for="securityCode">Security Code</label> <br>
                <input type="text" class="form-control" id="securityCode" name="securityCode" required>
            </div>
            <div class="form-group">
                <label for="purpose">Purpose</label> <br>
                <input type="text" class="form-control" id="purpose" name="purpose" required>
            </div>
            <br>
            <div class="form-group">
                <div class="error">
                    <% String error = (String) request.getAttribute("errorMessage"); %>
                    <% if (error != null && !error.isEmpty()) { %>
                        <%= error %>
                    <% } %>
                </div>
            </div>
            <button type="submit" class="btn btn-primary">Add Regular Visitor</button>
        </form>
    </div>
    <br><br>
    <jsp:include page="footer.jsp" />
</body>
</html>
