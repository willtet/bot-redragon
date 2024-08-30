package dev.willtet.room;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public class QuestSemanal extends ListenerAdapter {

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event) {
        super.onMessageReactionAdd(event);
        if(event.getChannel().getId().equals("1272967732794494986")) {
            Message message = event.retrieveMessage().complete();
            OffsetDateTime messageCreationTime = message.getTimeCreated();

            LocalDate today = LocalDate.now();
            LocalDate dateOnly = messageCreationTime.toLocalDate();
            boolean isToday = dateOnly.isEqual(today);

            if(isToday){
                System.out.println("Enviar pontos para reação");
            }

        }
    }
}
