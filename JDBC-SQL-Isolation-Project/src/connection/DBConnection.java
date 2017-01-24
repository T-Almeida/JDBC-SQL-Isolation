/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import main.Dirty_Read_Demo;
/**
 *
 * @author ASUS
 */
public class DBConnection {
    //macros
    public static final String IP = "127.0.0.1\\DESKTOP-FV2JT0T";
    public static final String USER = "sa";
    public static final String PASS = "sa";
    public static final String DATABASE = "Concurrency";
    
    public static final String URL="jdbc:sqlserver://"+IP+":1433;DatabaseName="+DATABASE;
    
    public static Connection getSQLConnection() throws Exception
    {
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        return DriverManager.getConnection(URL,USER,PASS);
    }
}
