
package DBConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class database {
    public static void connection() throws SQLException, ClassNotFoundException{
        Connection con = null;//Connection for database server
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");//Connects the driver class of DB
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/coursework","root","");//Connects the DB server
        } catch (ClassNotFoundException ex) {
            ex.getMessage();
        }
    }
}