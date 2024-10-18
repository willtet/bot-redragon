package dev.willtet.commands;

import dev.willtet.Main;
import dev.willtet.database.DatabaseService;
import dev.willtet.enumeration.SalaEnum;
import dev.willtet.model.Constants;
import dev.willtet.model.Publicacao;
import dev.willtet.model.Role;
import dev.willtet.model.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Objects;

public class Ponto extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split(" ");

        if (event.getChannel() instanceof TextChannel) {
            TextChannel textChannel = (TextChannel) event.getChannel();

            if(equalSalaPontos(event)){
                if (args[0].equalsIgnoreCase("!"+"pontos")){
                    int pontos = DatabaseService.findPontosByUsuario(event.getAuthor().getId());
                    Role roles = DatabaseService.findRoleByPontos(pontos);

                    int pontoFaltante = (roles.getMaxRange() + 1) - pontos;


                    event.getAuthor().openPrivateChannel().queue(
                        (privateChannel) -> {
                                privateChannel.sendMessage(event.getAuthor().getGlobalName() + " está com "+ pontos+" pontos. \r" +
                                        "- Nível atual: "+roles.getNome()+". \r" +
                                        "- Ponto faltante para proximo nível: " + pontoFaltante).queue();
                        }
//                        ,
//                        (failure) -> {
//                            // Como envio mensagem marcando a pessoa em ephemeral?
//                            textChannel.sendMessage(event.getAuthor().getAsMention() + " para verificar os pontos, favor habilitar o envio de mensagem privado!").queue();
//                        }
                    );

                }
            }else if(equalModCommand(event)){
                if (args[0].equalsIgnoreCase("!"+"pontos")){
                    if(args.length != 2){
                        textChannel.sendMessage("Comando precisa de um id do usuário.").queue();
                        return;
                    }

                    String idUser = args[1];
                    if(idUser.isBlank()){
                        textChannel.sendMessage("O id do usuário está inválido").queue();
                        return;
                    }

                    User usuario = DatabaseService.findUserById(idUser);
                    if(Objects.isNull(usuario)){
                        textChannel.sendMessage("Usuário não registrado no cadastrado!").queue();
                        return;
                    }

                    List<Publicacao> publis = DatabaseService.findPublicacaoByUsuario(usuario.getId());
                    int pontos = DatabaseService.findPontosByUsuario(idUser);

                    StringBuilder builder = new StringBuilder();

                    builder.append("Usuário "+usuario.getUsername() + " possui "+ pontos +" pontos.").append("\r");

                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

                    for(Publicacao pub : publis){
                        builder.append("- "+pub.getPontoPeso() + " - "+pub.getMensagem() + " - Obtido em: "+ formatter.format(pub.getDataEntrada())).append("\r");

                    }

                    textChannel.sendMessage(builder.toString()).queue();

                }
            }
        }

    }

    public boolean equalSalaPontos(MessageReceivedEvent event){
        return event.getChannel().getId().equals("1203052240395440250") ||
                event.getChannel().getId().equals("1272967732794494986");
    }

    public boolean equalModCommand(MessageReceivedEvent event){
        return event.getChannel().getId().equals(Constants.MODCOMANDO) ;
    }
}