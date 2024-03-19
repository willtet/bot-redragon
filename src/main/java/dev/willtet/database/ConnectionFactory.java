package dev.willtet.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    public static Connection getConnection(){
        String url = "jdbc:sqlite:C:/Users/Willian/Desktop/discord-bot.db";
        try {
            return DriverManager.getConnection(url);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;

    }
}
