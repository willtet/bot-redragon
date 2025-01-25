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

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

public class QuestSemanalRoom extends ListenerAdapter implements BaseRoom{

    @Override
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {
        super.onMessageReactionAdd(event);
        if(event.getChannel().getId().equals("1272962405571494061")) {
            if(isMod(event.getMember().getRoles())){

                var emojiValido = Emoji.fromUnicode("U+1F44D");

                if(isMessageRegistered(event.getMessageId(), event.getMessageAuthorId())){
                    DatabaseService.updatePontosByUsuarioEMessage(event.getMessageId(), event.getUserId(), true);
                }else{
                    var mensagem = String.format("QUEST SEMANAL - 250 pontos");

                    LocalDate hoje = LocalDate.now();
                    LocalDate sabado = hoje.with(TemporalAdjusters.previousOrSame(DayOfWeek.SATURDAY));

                    if(DatabaseService.isValidoParaPontuarSemanal(hoje, sabado, mensagem, event.getMessageAuthorId())){
                        DatabaseService.postPontosByUsuario(
                                event.getMessageId(),
                                event.getMessageAuthorId(),
                                mensagem,
                                250
                        );
                    }

                }
            }

            verificarAtualizacaoRoles(event);

        }
    }

    private boolean isMessageRegistered(String idMsg, String idUserAuthor) {

        return DatabaseService.isMessageRegisteredByIdUser(idMsg, idUserAuthor);
    }

    @Override
    public void onMessageReactionRemove(@NotNull MessageReactionRemoveEvent event) {
        super.onMessageReactionRemove(event);
        if(event.getChannel().getId().equals("1272962405571494061")) {
            if(isMod(event.getMember().getRoles())){
                Message message = event.retrieveMessage().complete();
                OffsetDateTime messageCreationTime = message.getTimeCreated();

                LocalDate today = LocalDate.now();
                LocalDate dateOnly = messageCreationTime.toLocalDate();
                boolean isToday = dateOnly.isEqual(today);

                if(isToday){
                    if(isMessageRegistered(event.getMessageId(), message.getAuthor().getId())){
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
