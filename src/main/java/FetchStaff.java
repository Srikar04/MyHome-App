import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/fetchStaff")
public class FetchStaff extends HttpServlet {
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
        StringBuilder table = new StringBuilder();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        boolean empty = true;

        try (Connection con = DriverManager.getConnection(url, user, passwd)) {
            // SQL query to fetch staff details from the database
            String query = "SELECT * FROM StaffDetails";

            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            // Generate the HTML table
            table.append("<table class='table table-striped table-bordered table-hover'>");
            table.append("<thead class='thead-dark'><tr><th>Name</th><th>Role</th><th>Salary</th><th>Hours</th></tr></thead>");
            table.append("<tbody>");

            while (resultSet.next()) {
                empty = false;
                String name = resultSet.getString("name");
                String role = resultSet.getString("role");
                double salary = resultSet.getDouble("salary");
                int hours = resultSet.getInt("hours");

                table.append("<tr>");
                table.append("<td>").append(name).append("</td>");
                table.append("<td>").append(role).append("</td>");
                table.append("<td>").append(salary).append("</td>");
                table.append("<td>").append(hours).append("</td>");
                table.append("</tr>");
            }
            table.append("</tbody>");
            table.append("</table");

            resultSet.close();
            statement.close();
            con.close();
        } catch (Exception e) {
            table.append("An error occurred while fetching staff information.");
            e.printStackTrace();
        }

        if (empty) {
            table.setLength(0);
            table.append("<p>No Staff members in the database</p>");
        }
        request.setAttribute("staffTable", table);

        request.getRequestDispatcher("staffDetails.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
