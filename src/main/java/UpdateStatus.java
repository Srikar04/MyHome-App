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
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/updateStatus")
public class UpdateStatus extends HttpServlet {
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
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("action");
	    String visitorName = request.getParameter("visitorName");
	    
	    String username = (String) request.getSession().getAttribute("userName");

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
        	 
        	String query = "";
        	
             if ("approve".equals(action)) {
                 query = "UPDATE NormalVisitors SET Status = 'Approved' WHERE visitorName = ? AND flatNumber = ("
                 		+ "    SELECT flatNumber"
                 		+ "    FROM Residents"
                 		+ "    WHERE username = ?"
                 		+ ")";
             } else if ("reject".equals(action)) {
            	 query = "UPDATE NormalVisitors SET Status = 'Rejected' WHERE visitorName = ? AND flatNumber = ("
                  		+ "    SELECT flatNumber"
                  		+ "    FROM Residents"
                  		+ "    WHERE username = ?"
                  		+ ")";
             }

            try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
            	preparedStatement.setString(1, visitorName);
            	preparedStatement.setString(2, username);

                int rowsUpdated = preparedStatement.executeUpdate();
                if (rowsUpdated > 0) {
                    System.out.println("Visitor status updated.");
                    response.sendRedirect("fetchRequests");
                } else {
                    request.setAttribute("errorMessage", "No records updated. Visitor not found or not matching resident");
                    request.getRequestDispatcher("residentHome.jsp").forward(request, response);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            request.setAttribute("errorMessage", "An error has occurred");
            request.getRequestDispatcher("residentHome.jsp").forward(request, response);
        }
    }
}
