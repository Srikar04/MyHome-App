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

@WebServlet("/addStaff")
public class AddStaff extends HttpServlet {
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
        // Get staff details from the form
        String name = request.getParameter("name");
        String role = request.getParameter("role");
        double salary = Double.parseDouble(request.getParameter("salary"));
        int hours = Integer.parseInt(request.getParameter("hours"));

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
            // SQL query to insert staff details into the database
            String query = "INSERT INTO StaffDetails (name, role, salary, hours) VALUES (?, ?, ?, ?)";

            try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, role);
                preparedStatement.setDouble(3, salary);
                preparedStatement.setInt(4, hours);

                int rowsInserted = preparedStatement.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("Staff member successfully added.");
                    response.sendRedirect("staffDetails.jsp");
                } else {
                    request.setAttribute("errorMessage", "Some error has occurred");
                    request.getRequestDispatcher("addStaff.jsp").forward(request, response);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            request.setAttribute("errorMessage", "A staff member with the same details may already exist");
            request.getRequestDispatcher("addStaff.jsp").forward(request, response);
        }
    }
}
