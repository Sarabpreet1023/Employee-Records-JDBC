import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class EmployeeServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String empId = request.getParameter("empId");

        // JDBC parameters
        String url = "jdbc:mysql://localhost:3306/your_db";
        String user = "your_username";
        String pwd = "your_password";
        
        out.println("<html><body>");
        out.println("<h2>Employee Records</h2>");
        out.println("<form method='get' action=''>");
        out.println("Search by Employee ID: <input type='text' name='empId'/>");
        out.println("<input type='submit' value='Search'/>");
        out.println("</form>");

        Connection con = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(url, user, pwd);

            PreparedStatement ps;
            if (empId != null && !empId.trim().isEmpty()) {
                ps = con.prepareStatement("SELECT * FROM Employee WHERE EmpID = ?");
                ps.setInt(1, Integer.parseInt(empId));
            } else {
                ps = con.prepareStatement("SELECT * FROM Employee");
            }

            ResultSet rs = ps.executeQuery();
            out.println("<table border='1'><tr><th>EmpID</th><th>Name</th><th>Salary</th></tr>");
            boolean hasRow = false;
            while (rs.next()) {
                hasRow = true;
                out.println("<tr>");
                out.println("<td>" + rs.getInt("EmpID") + "</td>");
                out.println("<td>" + rs.getString("Name") + "</td>");
                out.println("<td>" + rs.getDouble("Salary") + "</td>");
                out.println("</tr>");
            }
            if (!hasRow) {
                out.println("<tr><td colspan='3'>No records found.</td></tr>");
            }
            out.println("</table>");
            rs.close();
            ps.close();
        } catch (Exception e) {
            out.println("<p>Error: " + e.getMessage() + "</p>");
        } finally {
            try { if (con != null) con.close(); } catch (Exception ignored) {}
        }
        out.println("</body></html>");
    }
}
