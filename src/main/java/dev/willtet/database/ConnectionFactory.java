package dev.willtet.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    public static Connection getConnection(){
        String url = System.getenv("c");
        String user = System.getenv("BOT_DB_USER");
        String password = System.getenv("BOT_DB_PASS");

        try{
            Connection myConn = DriverManager.getConnection(url,user,password);
            return myConn;

        } catch (SQLException e) {
           	return null;
        }

    }
}
