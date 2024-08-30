package dev.willtet.room;

import dev.willtet.database.DatabaseService;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public class QuestDiario extends ListenerAdapter {
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

    @Override
    public void onMessageReactionRemove(@NotNull MessageReactionRemoveEvent event) {
        super.onMessageReactionRemove(event);
        if(event.getChannel().getId().equals("1272967732794494986")) {
            Message message = event.retrieveMessage().complete();
            OffsetDateTime messageCreationTime = message.getTimeCreated();

            LocalDate today = LocalDate.now();
            LocalDate dateOnly = messageCreationTime.toLocalDate();
            boolean isToday = dateOnly.isEqual(today);

            if(isToday){
                System.out.println("retirar pontos para reação");
            }

        }
    }
}
