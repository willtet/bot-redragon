package dev.willtet.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    public static Connection getConnection(){
        String url = System.getenv("BOT_DB");
        String user = System.getenv("BOT_DB_USER");
        String password = System.getenv("BOT_DB_PASS");
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection myConn = DriverManager.getConnection(url,user,password);
            return myConn;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
           	return null;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
}
