package dev.willtet;

import dev.willtet.commands.Cadastrar;
import dev.willtet.commands.Ping;
import dev.willtet.commands.Publicacao;
import dev.willtet.events.PostPublish;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.security.auth.login.LoginException;

import java.util.EnumSet;

public class Main {
    public static JDA jda;

    //private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) throws LoginException {
            jda = JDABuilder.create(System.getenv("BOT_TOKEN"),
                    EnumSet.allOf(GatewayIntent.class)).build();

            jda.addEventListener(new Ping());
            jda.addEventListener(new PostPublish());
            jda.addEventListener(new Cadastrar());
            jda.addEventListener(new Publicacao());
            
            
    }
}