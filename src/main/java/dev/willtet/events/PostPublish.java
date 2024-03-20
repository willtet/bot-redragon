package dev.willtet.events;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.entities.emoji.UnicodeEmoji;
import net.dv8tion.jda.api.events.emoji.EmojiAddedEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.internal.entities.emoji.UnicodeEmojiImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;


public class PostPublish extends ListenerAdapter {


    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        var x = event.getChannel().getId();

        var y = event.getGuild();
        var z = event.getAuthor();
        var aprendiz = event.getGuild().getRoles().get(2);
        var a = 1;

        if(event.getChannel().getId().equalsIgnoreCase("1216898300297482320")){
            //Recuperar o usuario pelo id

            //Adicionar

            Member user =  event.getGuild().getMemberById("158732504239439872");

            user.getGuild().modifyMemberRoles(user, aprendiz).complete();




        }
    }

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event) {
        super.onMessageReactionAdd(event);



        var x = Emoji.fromUnicode("U+1F44E");


        if(event.getChannel().getId().equalsIgnoreCase("1216898300297482320")){

            //verificar se o emoji é o de joinha

        }
    }
}
