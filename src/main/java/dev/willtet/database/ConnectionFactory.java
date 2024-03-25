package dev.willtet.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    public static Connection getConnection(){
    	String url = "jdbc:mysql://191.101.71.197:3306/files_discordbot_db?autoReconnect=true&useSSL=false";
        String user = "files_discord";
        String password = "CzsCDI^BPs+yr0Kk";

        try{
            Connection myConn = DriverManager.getConnection(url,user,password);
            return myConn;

        } catch (SQLException e) {
           	return null;
        }

    }
}
