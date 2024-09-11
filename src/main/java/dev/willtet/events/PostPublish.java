package dev.willtet.events;

import dev.willtet.Main;
import dev.willtet.database.ConnectionFactory;
import dev.willtet.database.DatabaseService;
import dev.willtet.model.Constants;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.entities.emoji.UnicodeEmoji;
import net.dv8tion.jda.api.events.emoji.EmojiAddedEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.internal.entities.UserImpl;
import net.dv8tion.jda.internal.entities.emoji.UnicodeEmojiImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;


public class PostPublish extends ListenerAdapter {


    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        var x = event.getChannel().getId();

        var y = event.getGuild();
        var idAutor = event.getAuthor().getId();
        var aprendiz = event.getGuild().getRoles().get(2);
        var idMensagem = event.getMessage().getId();
        var mensagem = event.getMessage().getContentRaw();

        if(event.getChannel().getId().equalsIgnoreCase("1216898300297482320")){
            //Recuperar o usuario pelo id

            //Adicionar
            //DatabaseService.postPontosByUsuario(idMensagem, idAutor, mensagem);

            Member user =  event.getGuild().getMemberById("158732504239439872");

            user.getGuild().modifyMemberRoles(user, aprendiz).complete();




        }
    }

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event) {
        super.onMessageReactionAdd(event);
        TextChannel textChannel = (TextChannel) event.getChannel();

        var idMensagem = event.getMessageId();
        var emojiValido = Emoji.fromUnicode("U+1F44D");
        var emojiInvalido = Emoji.fromUnicode("U+1F44E");


        if(event.getChannel().getId().equalsIgnoreCase(Constants.POST_PUBLISH)){
            if(event.getReaction().getEmoji().equals(emojiValido)){
                DatabaseService.updatePontosByUsuario(idMensagem, true);

                verificarAtualizacaoRoles(event);
            }else if(event.getReaction().getEmoji().equals(emojiInvalido)){
                DatabaseService.updatePontosByUsuario(idMensagem, false);
            }else{
                textChannel
                        .sendMessage("Erro ao confirmar o ponto da mensagem ~ ").setMessageReference(idMensagem).queue();
            }

        }
    }

    private void verificarAtualizacaoRoles(MessageReactionAddEvent event) {
        String idAutor = event.getMessageAuthorId();
        int pontos = DatabaseService.findPontosByUsuario(idAutor);
        String idRole = DatabaseService.findRoleIdByPontos(pontos);

        var role = event.getGuild().getRoleById(idRole);
        var user = event.getGuild().getMemberById(idAutor);

        if(!user.getRoles().contains(role)){
            user.getGuild().modifyMemberRoles(user, role).complete();
        }

    }
}
