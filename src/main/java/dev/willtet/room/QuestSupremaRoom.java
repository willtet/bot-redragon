package dev.willtet.room;

import dev.willtet.database.DatabaseService;
import dev.willtet.enumeration.RoleEnum;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.react.GenericMessageReactionEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

public class QuestSupremaRoom extends ListenerAdapter implements BaseRoom{
    @Override
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {
        super.onMessageReactionAdd(event);
        if(event.getChannel().getId().equals("1272974608403791904")) {
            if(isMod(event.getMember().getRoles())){

                var emojiValido = Emoji.fromUnicode("U+1F44D");

                if(isMessageRegistered(event)){
                    DatabaseService.updatePontosByUsuarioEMessage(event.getMessageId(), event.getUserId(), true);
                }else{
                    var mensagem = String.format("QUEST SUPREMA - 1000 pontos");

                    DatabaseService.postPontosByUsuario(
                            event.getMessageId(),
                            event.getMessageAuthorId(),
                            mensagem,
                            1000
                    );
                }
            }

            verificarAtualizacaoRoles(event);

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
        if(event.getChannel().getId().equals("1272963331602387047")) {
            if(isMod(event.getMember().getRoles())){
                Message message = event.retrieveMessage().complete();
                OffsetDateTime messageCreationTime = message.getTimeCreated();

                LocalDate today = LocalDate.now();
                LocalDate dateOnly = messageCreationTime.toLocalDate();
                boolean isToday = dateOnly.isEqual(today);

                if(isToday){
                    if(isMessageRegistered(event)){
                        DatabaseService.updatePontosByUsuarioEMessage(event.getMessageId(), message.getAuthor().getId(), false);
                    }
                }
            }


        }
    }

    private boolean isMod(List<Role> ids) {
        return ids.stream().anyMatch(id -> id.getId().equals(RoleEnum.MOD.getId()) || id.getId().equals(RoleEnum.MODASSISTENTE.getId()) );
    }
}
