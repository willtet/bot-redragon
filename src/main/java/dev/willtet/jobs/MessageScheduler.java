package dev.willtet.jobs;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MessageScheduler {

    private final JDA jda;

    public MessageScheduler(JDA jda) {
        this.jda = jda;
    }

    public void agendarMensagemDiaria(String canalId) {
        // Obtém o canal de texto
        TextChannel channel = jda.getTextChannelById(canalId);
        if (channel == null) {
            System.out.println("Canal não encontrado!");
            return;
        }

        // Agendador de tarefas
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        // Calcula o tempo até 13:50 de hoje
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime targetTime = now.withHour(06).withMinute(00).withSecond(0).withNano(0);

        // Se o horário alvo já passou, agenda para o dia seguinte
        if (now.isAfter(targetTime)) {
            targetTime = targetTime.plusDays(1);
        }

        long initialDelay = Duration.between(now, targetTime).getSeconds();

        // Agenda a tarefa para executar todos os dias às 13:50
        scheduler.scheduleAtFixedRate(() -> {
            channel.sendMessage(mensagemAleatoria()).queue();
        }, initialDelay, 24 * 60 * 60, TimeUnit.SECONDS);
    }

    private String mensagemAleatoria(){
        List<String> frases = new ArrayList<>();

        //Utilizar arquivo caso ultrapasse mais de 30 linhas
        frases.add("Bom dia Familia SquadPro! ☀\n" +
                "✅ Missão do dia: Reja a essa mensagem e interaja com outros membros nas salas disponíveis! 😎");

        frases.add("Atenção, criadores! ⚠\n" +
                "➥ Reaja a esta mensagem e conquiste seus pontos do dia! \uD83D\uDE80\n" +
                "➥ Não esqueça de interagir com a galera e explorar todos os cantos do nosso Servidor! \uD83D\uDE09\n");

        frases.add("➥ E aí, pronto para somar seus pontos do dia? \uD83D\uDE0E\n" +
                "➥ Reaja a este post, participe de conversas e aproveite nosso servidor! ✨\n");

        frases.add("Pontos do dia esperando por você! ⏳\n" +
                "➥  Deixa sua reação aqui. \uD83D\uDE0E\n" +
                "➥  Compartilhe suas experiências e histórias com a galera nas salas de chat!\n");

        frases.add("Quem aí quer pontos? Só reagir e garantir o seu! \uD83D\uDCA5\n" +
                "➥ Além disso, que tal conhecer a nossa sessão de Dicas e Materiais de Apoio?! ");

        frases.add("Sua missão hoje: Reagir e pontuar! \uD83C\uDF89\n" +
                "➥  Conheça o trabalho dos outros membros do SquadPro, ajude para ser ajudado! \uD83E\uDD29\n");

        frases.add("Simples assim: Reaja e conquiste seus pontos do dia! \uD83C\uDFAF\n" +
                "➥ E não esqueça de dar uma olhadinha na sessão de parcerias. \uD83D\uDE09\n");

        frases.add("✅ Missão do dia: 1 Like = 100 Pontos.\n" +
                "➥  Reaja para conquistar. e Aproveite tudo que nosso servidor tem a oferecer!\n");

        frases.add("Hora de brilhar! Reaja e leve seus pontos do dia. ✨\n" +
                "➥  Os pontos que você soma hoje vão fazer diferença no seu Rank final! ⭐\n");

        frases.add("E aí Criador! Como vai a jornada por aí? \uD83D\uDE80\n" +
                "➥  Reaja para conquistar mais pontos! \n");

        frases.add("Pontos à vista! Só reagir e pronto! \uD83D\uDCA5\n" +
                "➥  Lembre de consultar as Missões Semanais e ficar atento à missão Surpresa! \uD83D\uDE09\n");

        Random random = new Random();

        Collections.shuffle(frases);

        return frases.get(random.nextInt(frases.size()));
    }
}
