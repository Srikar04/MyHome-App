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

@WebServlet("/addMaintenance")
public class AddMaintenance extends HttpServlet {
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
        // Get maintenance details from the form
    	Integer flatNumber = Integer.parseInt(request.getParameter("flatNumber"));
        String date = request.getParameter("date");
        double amount = Double.parseDouble(request.getParameter("amount"));

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
        	        request.getRequestDispatcher("Maintenance.jsp").forward(request, response);
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
        	        request.getRequestDispatcher("Maintenance.jsp").forward(request, response);
        	        return;
        	    }
        	}

           // SQL query to insert maintenance details into the database
            String query = "INSERT INTO MaintenanceDetails (flatNumber, dueStatus, date, amount) VALUES (?, ?, ?, ?)";

            try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
                preparedStatement.setInt(1, flatNumber);
                preparedStatement.setString(2, "due");
                preparedStatement.setString(3, date);
                preparedStatement.setDouble(4, amount);

                int rowsInserted = preparedStatement.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("Maintenance details successfully added.");
                    response.sendRedirect("superHome.jsp");
                } else {
                    request.setAttribute("errorMessage", "Some error has occurred");
                    request.getRequestDispatcher("Maintenance.jsp").forward(request, response);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            request.setAttribute("errorMessage", "Maintenance details could not be added.");
            request.getRequestDispatcher("Maintenance.jsp").forward(request, response);
        }
    }
}
