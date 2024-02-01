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

@WebServlet("/addBuilding")
public class AddBuilding extends HttpServlet {
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
		// Get the building info from the form
		String buildingName = request.getParameter("buildingName");
		int constructionYear = Integer.parseInt(request.getParameter("constructionYear"));
		double area = Double.parseDouble(request.getParameter("area"));
		
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
		    // SQL query to insert building data into the database
		    String query = "INSERT INTO Building (buildingName,constructionYear, area) VALUES (?, ?, ?)";
		    
		    try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
		        preparedStatement.setString(1, buildingName);
		        preparedStatement.setInt(2, constructionYear);
		        preparedStatement.setDouble(3, area);
		        
		        int rowsInserted = preparedStatement.executeUpdate();
		        if (rowsInserted > 0) {
		            System.out.println("Building successfully added.");
		            response.sendRedirect("adminHome.jsp");
		        } else {
		        	request.setAttribute("errorMessage", "Some error has occured");
	            	request.getRequestDispatcher("addBuilding.jsp").forward(request, response);
		        }
		    }
		}catch (SQLException ex) {
        	ex.printStackTrace();
        	request.setAttribute("errorMessage","Building with the name alerady Exists");
         	request.getRequestDispatcher("addBuilding.jsp").forward(request, response);
		}
	}

}
