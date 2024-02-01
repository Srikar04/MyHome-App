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

@WebServlet("/fetchRegularVisitors")
public class FetchRegularVisitors extends HttpServlet {
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

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        StringBuilder table = new StringBuilder();
        boolean empty = true;
        
        table.append("<table class='table table-striped table-bordered table-hover'>");
        table.append("<thead class='thead-dark'><tr><th>Visitor Name</th><th>Purpose</th><th>Check-In Time</th><th>Check-Out Time</th></tr></thead>");
        table.append("<tbody>");
        
        String username = (String) request.getSession().getAttribute("userName");

        try (Connection con = DriverManager.getConnection(url, user, passwd)) {
        	// SQL query to retrieve data from both tables where flatNumber matches
            String query = "SELECT * FROM RegularVisitorCheckIn,RegularVisitors,Residents where "
            		+ "RegularVisitors.securityCode = RegularVisitorCheckIn.securityCode and "
            		+ "RegularVisitorCheckIn.flatNumber = Residents.flatNumber and "
            		+ "Residents.username = ?";

            Statement statement = con.createStatement();
            
            try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
                preparedStatement.setString(1, username);

                ResultSet resultSet = preparedStatement.executeQuery();
	                while (resultSet.next()) {
	                    empty = false;
	                    String visitorName = resultSet.getString("VisitorName");
	                    String purpose = resultSet.getString("Purpose");
	                    String checkInTime = resultSet.getString("CheckInTime");
	                    String checkOutTime = resultSet.getString("CheckOutTime");
	                    
	                    table.append("<tr>");
	                    table.append("<td>").append(visitorName).append("</td>");
	                    table.append("<td>").append(purpose).append("</td>");
	                    table.append("<td>").append(checkInTime).append("</td>");
	                    table.append("<td>").append(checkOutTime).append("</td>");
	                    table.append("</tr>");
	                }
                }
            statement.close();
            con.close();
        } catch (Exception e) {
            html.append("An error occurred while fetching information.");
            e.printStackTrace();
        }
        
        table.append("</tbody>");
        table.append("</table>");

        if (empty) {
            table.append("<p>No Regular Visitors Check-In records found for the resident.</p>");
        }

        request.setAttribute("visitorCheckInTable", table.toString());

        request.getRequestDispatcher("residentVisitors.jsp").forward(request, response);
    }
	

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
