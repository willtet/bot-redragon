package dev.willtet;

import dev.willtet.commands.*;
import dev.willtet.events.PostPublish;
import dev.willtet.jobs.MessageScheduler;
import dev.willtet.model.Constants;
import dev.willtet.room.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;

import java.util.Date;
import java.util.EnumSet;
import java.util.TimeZone;

public class Main {
    public static JDA jda;

    //private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) throws LoginException, InterruptedException {
        TimeZone.setDefault(TimeZone.getTimeZone("America/Sao_Paulo"));
        System.out.println("Current Date: "+new Date());

        jda = JDABuilder.create(
                System.getenv("BOT_TOKEN"),
                EnumSet.allOf(GatewayIntent.class)).build();



        jda.awaitReady();

        MessageScheduler scheduler = new MessageScheduler(jda);

        jda.addEventListener(new Ping());
        jda.addEventListener(new PostPublish());
        jda.addEventListener(new Cadastrar());
        jda.addEventListener(new Publicacao());
        jda.addEventListener(new Ponto());
        jda.addEventListener(new Legado());
        // SQUADPRO
        jda.addEventListener(new TopRank());
        jda.addEventListener(new CadastroRoom());
        jda.addEventListener(new QuestDiarioRoom());
        jda.addEventListener(new QuestSemanalRoom());
        jda.addEventListener(new QuestSupremaRoom());
        jda.addEventListener(new QuestCollabsRoom());
        jda.addEventListener(new QuestSurpresaRoom());


        //JOBS
        scheduler.agendarMensagemDiaria(Constants.QUEST_DIARIO);
    }
}