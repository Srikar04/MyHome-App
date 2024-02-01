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

@WebServlet("/fetchRequests")
public class FetchRequest extends HttpServlet {
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

        // Set the content type to HTML
        response.setContentType("text/html");

        // Get a PrintWriter to write the HTML response
        StringBuilder html = new StringBuilder();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        boolean empty = true;
        
        String username = (String) request.getSession().getAttribute("userName");

        try (Connection con = DriverManager.getConnection(url, user, passwd)) {
        	// SQL query to retrieve data from both tables where flatNumber matches
            String query = "SELECT * FROM NormalVisitors, Residents WHERE NormalVisitors.flatNumber = Residents.flatNumber"
            		+ " AND Residents.username = ? "
            		+ "AND NormalVisitors.status = ?";

            Statement statement = con.createStatement();
            
            try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, "Pending");

                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                         empty = false;

                         String visitorName = resultSet.getString("visitorName");
                         String purpose = resultSet.getString("Purpose");
                         
                         html.append("<form action=\"updateStatus\" method=\"post\">");  // Replace "YourServletURL" with the actual URL of your servlet

                         // Create an HTML card for each entry
                         html.append("<div class=\"card\">");
                         html.append("<div class=\"card-body\">");
                         html.append("<h5 class=\"card-title\">Visitor Name: " + visitorName + "</h5>");
                         html.append("<p class=\"card-text\">Purpose: " + purpose + "</p>");
                         html.append("<input type=\"hidden\" name=visitorName value="+ visitorName + ">");
                         html.append("<button type=\"submit\" name=\"action\" value=\"approve\" class=\"btn btn-success custom-margin\">Approve</button>");
                         html.append("<button type=\"submit\" name=\"action\" value=\"reject\" class=\"btn btn-danger\">Reject</button>");
                         html.append("</div></div>");
                         html.append("</form>");
                         html.append("<br>");
                     }
                resultSet.close();
                }
            statement.close();
            con.close();
        } catch (Exception e) {
            html.append("An error occurred while fetching information.");
            e.printStackTrace();
        }

        if (empty) {
            html.setLength(0);
            html.append("<p>No pending requests</p>");
        }
        request.setAttribute("requests", html.toString());

        request.getRequestDispatcher("residentHome.jsp").forward(request, response);
    }
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
