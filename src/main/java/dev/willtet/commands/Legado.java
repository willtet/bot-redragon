package dev.willtet.commands;

import dev.willtet.database.DatabaseService;
import dev.willtet.enumeration.RoleEnum;
import dev.willtet.model.Constants;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.List;

public class Legado extends ListenerAdapter {
    public void onMessageReceived(MessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split(" ");
        TextChannel textChannel = (TextChannel) event.getChannel();
        if(event.getChannel().getId().equals(Constants.POST_LEGADO)) {
            if(isMod(event.getMember().getRoles())){
                if (args[0].equalsIgnoreCase("!"+"adicionar")){
                    String[] dados = args[1].split(":");

                    if(dados.length < 2){
                        textChannel.sendMessage("Erro ao inserir o ponto").queue();
                    }else{
                        var idAutor = dados[0];
                        var pontos = dados[1];

                        var staff = String.format("Staff:%s - Legado", event.getAuthor().getGlobalName());

                        DatabaseService.postPontosByLegado(
                                event.getMessageId(),
                                idAutor,
                                staff,
                                Integer.parseInt(pontos)
                        );

                        textChannel.sendMessage("Adicionado "+pontos+ " pontos para o usuario!").queue();

                    }



                }
                if (args[0].equalsIgnoreCase("!"+"retirar")){
                    String[] dados = args[1].split(":");

                    if(dados.length < 2){
                        textChannel.sendMessage("Erro ao inserir o ponto").queue();
                    }else{
                        var idAutor = dados[0];
                        var pontos = dados[1];

                        var staff = String.format("Staff:%s - Legado", event.getAuthor().getGlobalName());

                        DatabaseService.postPontosByLegado(
                                event.getMessageId(),
                                idAutor,
                                staff,
                                Integer.parseInt(pontos)
                        );

                        textChannel.sendMessage("Adicionado "+pontos+ " pontos para o usuario!").queue();

                    }



                }
            }

        }


    }

    private boolean isMod(List<Role> ids) {
        return ids.stream().anyMatch(id -> id.getId().equals(RoleEnum.MOD.getId()) || id.getId().equals(RoleEnum.MODASSISTENTE.getId()) );
    }


}
