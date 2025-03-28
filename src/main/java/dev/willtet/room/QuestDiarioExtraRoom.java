package dev.willtet.room;

import dev.willtet.database.DatabaseService;
import dev.willtet.enumeration.RoleEnum;
import dev.willtet.enumeration.SalaEnum;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
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
import java.util.Objects;
import java.util.Set;

public class QuestDiarioExtraRoom extends ListenerAdapter implements BaseRoom{

    @Override
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {
        super.onMessageReactionAdd(event);
        if(isMissaoDiarioExtraRoom(event.getChannel().getId())) {
            if(isMod(event.getMember().getRoles())){

                if(isMessageRegistered(event.getMessageId(), event.getMessageAuthorId())){
                    DatabaseService.updatePontosByUsuarioEMessage(event.getMessageId(), event.getMessageAuthorId(), true);
                }else{

                    var mensagem = String.format("QUEST DIARIO EXTRA - 50 pontos");

                    LocalDate hoje = LocalDate.now();
                    LocalDate hojeDiario = hoje;

                    if(DatabaseService.isValidoParaPontuarSemanal(hoje, hojeDiario, mensagem, event.getMessageAuthorId())){
                        DatabaseService.postPontosByUsuario(
                                event.getMessageId(),
                                event.getMessageAuthorId(),
                                mensagem,
                                50
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
        if(isMissaoDiarioExtraRoom(event.getChannel().getId())) {
            if(isMod(event.getMember().getRoles())){
                Message message = event.retrieveMessage().complete();
                OffsetDateTime messageCreationTime = message.getTimeCreated();

                LocalDate today = LocalDate.now();
                LocalDate dateOnly = messageCreationTime.toLocalDate();
                boolean isToday = dateOnly.isEqual(today);
                String authorId = message.getAuthor().getId();


                if(isToday){
                    if(isMessageRegistered(event.getMessageId(), authorId)){
                        DatabaseService.updatePontosByUsuarioEMessage(event.getMessageId(), message.getAuthor().getId(), false);
                    }
                }
            }


        }
    }

    private boolean isMod(List<Role> ids) {
        return ids.stream().anyMatch(id -> id.getId().equals(RoleEnum.MOD.getId()) || id.getId().equals(RoleEnum.MODASSISTENTE.getId()) );
    }

    private boolean isMissaoDiarioExtraRoom(String idRoom) {
        Set<String> VALID_IDS = Set.of(
                SalaEnum.MISSAODIARIOEXRA.getId()
        );
        return VALID_IDS.contains(idRoom);
    }
}
