package dev.willtet.room;

import dev.willtet.database.DatabaseService;
import dev.willtet.enumeration.SalaEnum;
import dev.willtet.model.Constants;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;

public interface BaseRoom {
    default void verificarAtualizacaoRoles(MessageReactionAddEvent event) {
        String idAutor = event.getMessageAuthorId();
        int pontos = DatabaseService.findPontosByUsuario(idAutor);
        String idRole = DatabaseService.findRoleIdByPontos(pontos);

        var role = event.getGuild().getRoleById(idRole);
        var user = event.getGuild().getMemberById(idAutor);

        if(!user.getRoles().contains(role)){

            user.getGuild().addRoleToMember(user, role).complete();
        }

    }

    default void verificarAtualizacaoRolesDiario(MessageReactionAddEvent event) {
        String idAutor = event.getUserId();
        int pontos = DatabaseService.findPontosByUsuario(idAutor);
        String idRole = DatabaseService.findRoleIdByPontos(pontos);

        var role = event.getGuild().getRoleById(idRole);
        var user = event.getGuild().getMemberById(idAutor);

        if(!user.getRoles().contains(role)){
            user.getGuild().addRoleToMember(user, role).complete();
        }

    }

    default void enviarMensagemModeradorRoom(MessageReactionAddEvent event, String mensagem){
        TextChannel cadastroRoom = event.getJDA().getTextChannelById(SalaEnum.MODNOTIFICACAO.getId());

        cadastroRoom.sendMessage(mensagem).queue();
    }
}
