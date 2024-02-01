import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

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

@WebServlet("/login")
public class login extends HttpServlet {
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
		// Get the data from the request
        String username = request.getParameter("username");
        String passwd = request.getParameter("password");
        String tableName = request.getParameter("tableName");
        
        Properties props = getConnectionData();
        		
		String url = props.getProperty("db.url");
        String user = props.getProperty("db.user");
        String password = props.getProperty("db.passwd");
        
        try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		}catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
        
        
        try {
            Connection conn = DriverManager.getConnection(url,user,password);
            String sql = "SELECT * FROM " + tableName + " WHERE username = ?";
            
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, username);

            ResultSet resultSet = statement.executeQuery();            
            String fetchedPassword = null;

            if (resultSet.next()) {
                fetchedPassword = resultSet.getString("password");
            }
            if(fetchedPassword == null) {
            	request.setAttribute("errorMessage", "No such user exits");
            	request.getRequestDispatcher("Login.jsp").forward(request, response);
            }
            else if (fetchedPassword != null && fetchedPassword.equals(passwd)) {
            	HttpSession session = request.getSession();
				session.setAttribute("userName", username);
				session.setAttribute("userType", tableName);
				session.setMaxInactiveInterval(30 * 60);
            	if(tableName.equals("Administrator")) {
            		response.sendRedirect("adminHome.jsp");
            	}
            	else if(tableName.equals("Supervisor")) {
            		response.sendRedirect("superHome.jsp");
            	}else {
            		response.sendRedirect("fetchRequests");
            	}
            }else{
            	request.setAttribute("errorMessage", "Incorrect password");
            	request.getRequestDispatcher("Login.jsp").forward(request, response);
            }
            resultSet.close();
            statement.close();
            conn.close();
        }catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", e.getMessage());
        	request.getRequestDispatcher("Login.jsp").forward(request, response);
        }

	}

}
