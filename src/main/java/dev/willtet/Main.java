package dev.willtet;

import dev.willtet.commands.Cadastrar;
import dev.willtet.commands.Ping;
import dev.willtet.database.ConnectionFactory;
import dev.willtet.events.PostPublish;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.security.auth.login.LoginException;

import java.sql.ResultSet;
import java.util.EnumSet;

public class Main {
    public static JDA jda;

    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) throws LoginException {
            jda = JDABuilder.create(System.getenv("BOT_TOKEN"),
                    EnumSet.allOf(GatewayIntent.class)).build();

            jda.addEventListener(new Ping());
            jda.addEventListener(new PostPublish());
            jda.addEventListener(new Cadastrar());
            
            try {
            	var con = new ConnectionFactory();
                var connection = con.getConnection();
                String sql = "select * FROM tb_usuarios";
                
                var stm = connection.createStatement();
                
                var x = stm.execute(sql);
                ResultSet rs = stm.executeQuery(sql);		      

                // Extract data from result set
                while (rs.next()) {
                   // Retrieve by column name
                   System.out.print("ID: " + rs.getString("id"));
                   System.out.print(", Name: " + rs.getString("name"));
                   System.out.print(", username: " + rs.getString("username"));
                   System.out.println(", discord: " + rs.getString("discord_id"));
                }
                rs.close();
                
            }catch (Exception e) {
				// TODO: handle exception
			}
            
    }
}