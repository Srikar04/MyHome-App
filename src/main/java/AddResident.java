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

@WebServlet("/addResident")
public class AddResident extends HttpServlet {
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
        // Get resident details from the form
        String residentName = request.getParameter("residentName");
        int numOfPeople = Integer.parseInt(request.getParameter("numOfPeople"));
        String occupation = request.getParameter("occupation");
        String contactNumber = request.getParameter("contactNumber");
        int flatNumber = Integer.parseInt(request.getParameter("flatNumber"));
        String password = request.getParameter("password");

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
        	
        	String checkFlatQuery = "SELECT COUNT(*) FROM FlatDetails WHERE flatNumber = ?";
        	try (PreparedStatement checkFlatStatement = con.prepareStatement(checkFlatQuery)) {
        	    checkFlatStatement.setInt(1, flatNumber);
        	    ResultSet flatResultSet = checkFlatStatement.executeQuery();
        	    if (!flatResultSet.next() || flatResultSet.getInt(1) == 0) {
        	        request.setAttribute("errorMessage", "No flat with that number exists");
        	        request.getRequestDispatcher("resident.jsp").forward(request, response);
        	        return;
        	    }
        	}
        	
        	
        	String checkResidentQuery = "SELECT * FROM Residents WHERE flatNumber = ?";
        	try (PreparedStatement checkResidentStatement = con.prepareStatement(checkResidentQuery)) {
        	    checkResidentStatement.setInt(1, flatNumber);
        	    ResultSet residentResultSet = checkResidentStatement.executeQuery();
        	    if (residentResultSet.next()) {
        	        request.setAttribute("errorMessage", "Flat alloted to some other resident");
        	        request.getRequestDispatcher("resident.jsp").forward(request, response);
        	        return;
        	    }
        	}
        	
            // SQL query to insert resident data into the database
            String query = "INSERT INTO Residents (username, numOfPeople, occupation, contactNumber,password, flatNumber) VALUES (?, ?, ?, ?, ?, ?)";

            try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
                preparedStatement.setString(1, residentName);
                preparedStatement.setInt(2, numOfPeople);
                preparedStatement.setString(3, occupation);
                preparedStatement.setString(4, contactNumber);
                preparedStatement.setString(5, password);
                preparedStatement.setInt(6, flatNumber);

                int rowsInserted = preparedStatement.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("Resident successfully added.");
                    response.sendRedirect("superHome.jsp");
                } else {
                    request.setAttribute("errorMessage", "Some error has occurred");
                    request.getRequestDispatcher("resident.jsp").forward(request, response);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            request.setAttribute("errorMessage", "An error has occurred");
            request.getRequestDispatcher("resident.jsp").forward(request, response);
        }
    }

}
