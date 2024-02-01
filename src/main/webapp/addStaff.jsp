<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Add Staff</title>
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
        <h1 class="mt-4">Add Staff</h1>
        <form action="addStaff" method="post">
            <div class="form-group">
                <label for="name">Name</label> <br>
                <input type="text" class="form-control" id="name" name="name" required>
            </div>
            <div class="form-group">
                <label for="role">Role</label> <br>
                <input type="text" class="form-control" id="role" name="role" required>
            </div>
            <div class="form-group">
                <label for="salary">Salary</label> <br>
                <input type="text" class="form-control" id="salary" name="salary" required>
            </div>
            <div class="form-group">
                <label for="hours">Hours</label> <br>
                <input type="text" class="form-control" id="hours" name="hours" required>
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
            <button type="submit" class="btn btn-primary">Add Staff</button>
        </form>
    </div>
    <br><br>
    <jsp:include page="footer.jsp" />
</body>
</html>
