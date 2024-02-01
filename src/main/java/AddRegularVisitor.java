import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/addRegularVisitor")
public class AddRegularVisitor extends HttpServlet {
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
		String visitorName = request.getParameter("visitorName");
        String securityCode = request.getParameter("securityCode");
        String purpose = request.getParameter("purpose");

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
        	 String query = "INSERT INTO RegularVisitors (visitorName, securityCode, purpose) VALUES (?, ?, ?)";

            try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
                preparedStatement.setString(1, visitorName);
                preparedStatement.setString(2, securityCode);
                preparedStatement.setString(3, purpose);

                int rowsInserted = preparedStatement.executeUpdate();
                if (rowsInserted > 0) {
                    request.setAttribute("errorMessage", "Regular visitor successfully added.");
                    request.getRequestDispatcher("regularVisitor.jsp").forward(request, response);
                } else {
                    request.setAttribute("errorMessage", "Some error has occurred");
                    request.getRequestDispatcher("regularVisitor.jsp").forward(request, response);
                }
            } 
        } catch (SQLIntegrityConstraintViolationException ex) {
            // SQLIntegrityConstraintViolationException indicates a constraint violation
        	ex.printStackTrace();
            request.setAttribute("errorMessage", "Regular Visitor with the name already exists");
            request.getRequestDispatcher("regularVisitor.jsp").forward(request, response);
        }catch (SQLException ex) {
            ex.printStackTrace();
            request.setAttribute("errorMessage", "An error has occurred");
            request.getRequestDispatcher("regularVisitor.jsp").forward(request, response);
        }
	}

}
