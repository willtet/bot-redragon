package dev.willtet.commands;

import dev.willtet.Main;
import dev.willtet.database.DatabaseService;
import dev.willtet.model.Constants;
import dev.willtet.model.Role;
import dev.willtet.model.User;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.List;
import java.util.Objects;

public class Usuario extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split(" ");

        if (event.getChannel() instanceof TextChannel) {
            TextChannel textChannel = (TextChannel) event.getChannel();

            if(equalSalas(event)){
                if (args[0].equalsIgnoreCase("!"+"usuario")){
                    String idUsuario = args[1];
                    User user = DatabaseService.findUserById(idUsuario);

                    if(Objects.nonNull(user)){
                        textChannel.sendMessage(" Usuário cadastrado: \r" +
                                "- Username: "+user.getUsername()+"\r" +
                                "- Data de cadastro: "+user.getDataEntrada()+"\r" +
                                "- Sala Regra: "+verificarReacao(event, idUsuario)).queue();
                    }else{
                        textChannel.sendMessage("Usuário não cadastrado e "+verificarReacao(event, idUsuario)+"!").queue();
                    }
                }
            }
        }

    }

    public boolean equalSalas(MessageReceivedEvent event){
        return event.getChannel().getId().equals(Constants.MODCOMANDO);
    }

    public String verificarReacao(MessageReceivedEvent event, String userId){
        long channelId = 1203052120258121778L;  // Substitua pelo ID do canal
        long messageId = 1273371314249531472L;  // Substitua pelo ID da mensagem

        // Obtenha o TextChannel e a mensagem
        TextChannel channel = event.getJDA().getTextChannelById(channelId);
        if (channel != null) {
            Message message = channel.retrieveMessageById(messageId).complete();  // Agora é síncrono
            if (message != null) {
                // Iterar sobre cada reação na mensagem
                for (MessageReaction reaction : message.getReactions()) {
                    // Recupera os usuários que reagiram
                    List<net.dv8tion.jda.api.entities.User> users = reaction.retrieveUsers().complete();  // Agora é síncrono
                    // Verifica se o usuário com o ID especificado reagiu
                    boolean userReacted = users.stream()
                            .anyMatch(user -> user.getId().equalsIgnoreCase(userId));
                    if (userReacted) {
                        return "O usuário reagiu à mensagem da regra!";
                    }
                }
            }
        }
        return "O usuário NÃO reagiu à mensagem da regra!";
    }
}
