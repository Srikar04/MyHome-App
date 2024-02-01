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

@WebServlet("/addFlat")
public class AddFlat extends HttpServlet {
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
        // Get the flat details from the form
        String flatId = request.getParameter("flatNum");
        double flatArea = Double.parseDouble(request.getParameter("flatArea"));
        String buildingName = request.getParameter("buildingName");
        String bhkType = request.getParameter("bhkType");

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
            // SQL query to insert flat details into the database
            String query = "INSERT INTO FlatDetails (flatNumber, flatArea, buildingName, bhkType) VALUES (?, ?, ?, ?)";

            try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
                preparedStatement.setString(1, flatId);
                preparedStatement.setDouble(2, flatArea);
                preparedStatement.setString(3, buildingName);
                preparedStatement.setString(4, bhkType);

                int rowsInserted = preparedStatement.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("Flat successfully added.");
                    response.sendRedirect("adminHome.jsp");
                } else {
                    request.setAttribute("errorMessage", "Some error has occurred");
                    request.getRequestDispatcher("addFlat.jsp").forward(request, response);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            request.setAttribute("errorMessage", "Flat with the ID already exists");
            request.getRequestDispatcher("addFlat.jsp").forward(request, response);
        }
    }
}
