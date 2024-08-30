package dev.willtet.commands;

import dev.willtet.database.DatabaseService;
import dev.willtet.enumeration.SalaEnum;
import dev.willtet.model.Constants;
import dev.willtet.model.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Ponto extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split(" ");
        TextChannel textChannel = (TextChannel) event.getChannel();
        if(equalSalaLegends(event)) {
            if (args[0].equalsIgnoreCase("!"+"pontos")){
                int pontos = DatabaseService.findPontosByUsuario(event.getAuthor().getId());

                textChannel.sendMessage(event.getAuthor().getGlobalName() + " está com "+ pontos).queue();

            }
        }

        if (args[0].equalsIgnoreCase("!"+"pontos")){
            int pontos = DatabaseService.findPontosByUsuario(event.getAuthor().getId());
            String role = DatabaseService.findRoleIdByPontos(pontos);
            Role roles = DatabaseService.findRoleByPontos(pontos);

            int pontoFaltante = (roles.getMaxRange() + 1) - pontos;

            textChannel.sendMessage(event.getAuthor().getGlobalName() + " está com "+ pontos+" pontos. \r" +
                    "- Nível atual: "+role+". \r" +
                    "- Ponto faltante para proximo nível: " + pontoFaltante).queue();


        }


    }

    public boolean equalSalaLegends(MessageReceivedEvent event){
        return event.getChannel().getId().equals(Constants.APRENDIZ) ||
                event.getChannel().getId().equals(Constants.ESCUDEIRO) ||
                event.getChannel().getId().equals(Constants.CAVALEIRO) ||
                event.getChannel().getId().equals(Constants.PALADINO) ||
                event.getChannel().getId().equals(Constants.MESTRE);
    }
}
