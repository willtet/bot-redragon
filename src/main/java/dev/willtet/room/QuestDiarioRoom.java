package dev.willtet.room;

import dev.willtet.database.DatabaseService;
import dev.willtet.enumeration.RoleEnum;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.react.GenericMessageReactionEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

public class QuestDiarioRoom extends ListenerAdapter implements BaseRoom {
    @Override
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {
        super.onMessageReactionAdd(event);
        if(event.getChannel().getId().equals("1270072766619582635")) {
            Message message = event.retrieveMessage().complete();
            OffsetDateTime messageCreationTime = message.getTimeCreated();

            LocalDate today = LocalDate.now();
            LocalDate dateOnly = messageCreationTime.toLocalDate();
            boolean isToday = dateOnly.isEqual(today);

            if(isToday){
                if(isMessageRegistered(event)){
                    DatabaseService.updatePontosByUsuarioEMessage(event.getMessageId(), event.getUserId(), true);
                }else{
                    var mensagem = String.format("QUEST DIARIO - 100 pontos");

                    DatabaseService.postPontosByUsuario(
                            event.getMessageId(),
                            event.getUserId(),
                            mensagem,
                            100
                    );
                }

            }

            verificarAtualizacaoRolesDiario(event);
        }
    }

    private boolean isMessageRegistered(GenericMessageReactionEvent event) {
        String idMsg = event.getMessageId();
        String idUser = event.getUserId();

        return DatabaseService.isMessageRegisteredByIdUser(idMsg, idUser);
    }

    @Override
    public void onMessageReactionRemove(@NotNull MessageReactionRemoveEvent event) {
        super.onMessageReactionRemove(event);
        if(event.getChannel().getId().equals("1270072766619582635")) {
            Message message = event.retrieveMessage().complete();
            OffsetDateTime messageCreationTime = message.getTimeCreated();

            LocalDate today = LocalDate.now();
            LocalDate dateOnly = messageCreationTime.toLocalDate();
            boolean isToday = dateOnly.isEqual(today);

            if(isToday){
                if(isMessageRegistered(event)){
                    DatabaseService.updatePontosByUsuarioEMessage(event.getMessageId(), event.getUserId(), false);
                }
            }
        }
    }
}
