import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/fetchDues")
public class FetchDues extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static Properties getConnectionData() {
        Properties props = new Properties();

        String fileName = "/home/srikar/eclipse-workspace/21MCME19_SSS_DBMS/src/main/webapp/WEB-INF/db.properties";

        try (FileInputStream in = new FileInputStream(fileName)) {
            props.load(in);
        } catch (IOException ex) {
            Logger lgr = Logger.getLogger(login.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return props;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Properties props = getConnectionData();
        String url = props.getProperty("db.url");
        String user = props.getProperty("db.user");
        String passwd = props.getProperty("db.passwd");

        response.setContentType("text/html");

        StringBuilder html = new StringBuilder();
        
        String username = (String) request.getSession().getAttribute("userName");
        

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        boolean empty = true;

        try (Connection con = DriverManager.getConnection(url, user, passwd)) {
            // SQL query to retrieve dues from the Maintenance table
            String query = "SELECT * FROM MaintenanceDetails,FlatDetails,Residents WHERE "
            		+ "? = Residents.username AND "
            		+ "Residents.flatNumber = FlatDetails.flatNumber AND "
            		+ "MaintenanceDetails.flatNumber = FlatDetails.flatNumber AND "
            		+ "MaintenanceDetails.dueStatus = ? ";

            Statement statement = con.createStatement();

            try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
            	preparedStatement.setString(1,username);
            	preparedStatement.setString(2,"due");
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    empty = false;

                    int flatNumber = resultSet.getInt("flatNumber");
                    String dueStatus = resultSet.getString("dueStatus");
                    String date = resultSet.getString("date");
                    double amount = resultSet.getDouble("amount");
                    
                    html.append("<form action=\"payDues\" method=\"post\">");

                    // Create an HTML card for each due entry
                    html.append("<div class=\"card\">");
                    html.append("<div class=\"card-body\">");
                    html.append("<h5 class=\"card-title\">Flat Number: " + flatNumber + "</h5>");
                    html.append("<p class=\"card-text\"><b>Date:</b> " + date + "</p>");
                    html.append("<p class=\"card-text\"><b>Amount:</b> " + amount + "</p>");
                    
                    // Adding Mode of Payment label
                    html.append("<label for=\"paymentMode\"><b>Mode of Payment</b></label>");
                    html.append("<select class=\"form-control\" name=\"paymentMode\" id=\"paymentMode\">");
                    html.append("<option value=\"cash\">Cash</option>");
                    html.append("<option value=\"creditCard\">Credit Card</option>");
                    html.append("<option value=\"debitCard\">Debit Card</option>");
                    html.append("</select><br>");
                    
                    // Hidden fields for Flat Number and Date
                    html.append("<input type=\"hidden\" name=\"flatNumber\" value=\"" + flatNumber + "\">");
                    html.append("<input type=\"hidden\" name=\"date\" value=\"" + date + "\">");
                    
                    // Adding Pay Dues button
                    html.append("<button type=\"submit\" class=\"btn btn-success custom-margin\">Pay Dues</button>");
                    html.append("</div></div>");
                    html.append("</form>");
                    html.append("<br>");
                }
                resultSet.close();
            }
            statement.close();
            con.close();
        } catch (Exception e) {
            html.append("An error occurred while fetching dues.");
            e.printStackTrace();
        }

        if (empty) {
            html.setLength(0);
            html.append("<p>No Pending dues found</p>");
        }
        request.setAttribute("dues", html.toString());

        request.getRequestDispatcher("residentDues.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
