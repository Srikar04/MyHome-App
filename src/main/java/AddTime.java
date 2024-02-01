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
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/addTime")
public class AddTime extends HttpServlet {
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
        response.getWriter().append("Served at: ").append(request.getContextPath());
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        String securityCode = request.getParameter("securityCode");
        Integer flatNumber = Integer.parseInt(request.getParameter("flatNumber"));

        Properties props = getConnectionData();
        String url = props.getProperty("db.url");
        String user = props.getProperty("db.user");
        String passwd = props.getProperty("db.passwd");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try (Connection con = DriverManager.getConnection(url, user, passwd)) {
        	// Check if the flat exists in FlatDetails
        	String checkFlatQuery = "SELECT COUNT(*) FROM FlatDetails WHERE flatNumber = ?";
        	try (PreparedStatement checkFlatStatement = con.prepareStatement(checkFlatQuery)) {
        	    checkFlatStatement.setInt(1, flatNumber);
        	    ResultSet flatResultSet = checkFlatStatement.executeQuery();
        	    if (!flatResultSet.next() || flatResultSet.getInt(1) == 0) {
        	        request.setAttribute("errorMessage", "No flat with that number exists");
        	        request.getRequestDispatcher("addTime.jsp").forward(request, response);
        	        return;
        	    }
        	}

        	// Check if there is a resident living in the flat
        	String checkResidentQuery = "SELECT COUNT(*) FROM Residents WHERE flatNumber = ?";
        	try (PreparedStatement checkResidentStatement = con.prepareStatement(checkResidentQuery)) {
        	    checkResidentStatement.setInt(1, flatNumber);
        	    ResultSet residentResultSet = checkResidentStatement.executeQuery();
        	    if (!residentResultSet.next() || residentResultSet.getInt(1) == 0) {
        	        request.setAttribute("errorMessage", "No resident living in the flat");
        	        request.getRequestDispatcher("addTime.jsp").forward(request, response);
        	        return;
        	    }
        	}
        	
        	// Check if there is a visitor with the given security code
        	String checkVisitorQuery = "SELECT COUNT(*) FROM RegularVisitors WHERE securityCode = ?";
        	try (PreparedStatement checkVisitorStatement = con.prepareStatement(checkVisitorQuery)) {
        	    checkVisitorStatement.setString(1, securityCode);
        	    ResultSet visitorResultSet = checkVisitorStatement.executeQuery();
        	    if (!visitorResultSet.next() || visitorResultSet.getInt(1) == 0) {
        	        request.setAttribute("errorMessage", "No visitor with the provided security code exists");
        	        request.getRequestDispatcher("addTime.jsp").forward(request, response);
        	        return;
        	    }
        	}


        	if ("checkin".equals(action)) {
        	    // For check-in, insert a new entry with check-in time
        	    String insertQuery = "INSERT INTO RegularVisitorCheckIn (securityCode, flatNumber, checkInTime) VALUES (?, ?, CURRENT_TIME())";

        	    try (PreparedStatement preparedStatement = con.prepareStatement(insertQuery)) {
        	        preparedStatement.setString(1, securityCode);
        	        preparedStatement.setInt(2, flatNumber);

        	        int rowsInserted = preparedStatement.executeUpdate();
        	        if (rowsInserted > 0) {
        	            System.out.println("Check-In successful.");
        	        } else {
        	            request.setAttribute("errorMessage", "Some error has occurred during Check-In");
        	        }
        	    }
        	}else if ("checkout".equals(action)) {
        	    // For check-out, update the check-out time in the existing entry
        	    String updateQuery = "UPDATE RegularVisitorCheckIn SET checkOutTime = CURRENT_TIME() WHERE securityCode = ? AND flatNumber = ? AND checkOutTime IS NULL";

        	    try (PreparedStatement preparedStatement = con.prepareStatement(updateQuery)) {
        	        preparedStatement.setString(1, securityCode);
        	        preparedStatement.setInt(2, flatNumber);

        	        int rowsUpdated = preparedStatement.executeUpdate();
        	        if (rowsUpdated > 0) {
        	            System.out.println("Check-Out successful.");
        	        } else {
        	            request.setAttribute("errorMessage", "No matching entry found for Check-Out");
        	        }
        	    }
        	}else {
                request.setAttribute("errorMessage", "Invalid action");
            }
            request.getRequestDispatcher("addTime.jsp").forward(request, response);
        } catch (SQLException ex) {
            ex.printStackTrace();
            request.setAttribute("errorMessage", "An error has occurred");
            request.getRequestDispatcher("addTime.jsp").forward(request, response);
        }
    }
}
