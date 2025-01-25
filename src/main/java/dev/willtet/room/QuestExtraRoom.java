package dev.willtet.room;

import dev.willtet.database.DatabaseService;
import dev.willtet.enumeration.RoleEnum;
import dev.willtet.enumeration.SalaEnum;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
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

public class QuestExtraRoom extends ListenerAdapter implements BaseRoom{

    @Override
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {
        super.onMessageReactionAdd(event);
        if(isMissaoExtraRoom(event.getChannel().getId())) {
            if(isMod(event.getMember().getRoles())){

                if(isMessageRegistered(event.getMessageId(), event.getMessageAuthorId())){
                    DatabaseService.updatePontosByUsuarioEMessage(event.getMessageId(), event.getMessageAuthorId(), true);
                }else{
                    var nomeSala = verificarSala(event.getChannel().getId());

                    if(Objects.isNull(nomeSala)){
                        enviarMensagemModeradorRoom(event, "MissaoExtraRoom - Sala não encontrada");
                        return;
                    }

                    var mensagem = String.format("QUEST EXTRA %s - 50 pontos", nomeSala);

                    LocalDate hoje = LocalDate.now();
                    LocalDate domingo = hoje.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));

                    if(DatabaseService.isValidoParaPontuarSemanal(hoje, domingo, mensagem, event.getMessageAuthorId())){
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

    private String verificarSala(String id) {
        if(SalaEnum.MISSAOEXTRA1.getId().equals(id)){
            return "MISSAOEXTRA1";
        }

        if(SalaEnum.MISSAOEXTRA2.getId().equals(id)){
            return "MISSAOEXTRA2";
        }
        if(SalaEnum.MISSAOEXTRA3.getId().equals(id)){
            return "MISSAOEXTRA3";
        }
        if(SalaEnum.MISSAOEXTRA4.getId().equals(id)){
            return "MISSAOEXTRA4";
        }
        if(SalaEnum.MISSAOEXTRA5.getId().equals(id)){
            return "MISSAOEXTRA5";
        }

        return null;


    }

    private boolean isMessageRegistered(String idMsg, String idUserAuthor) {

        return DatabaseService.isMessageRegisteredByIdUser(idMsg, idUserAuthor);
    }

    @Override
    public void onMessageReactionRemove(@NotNull MessageReactionRemoveEvent event) {
        super.onMessageReactionRemove(event);
        if(isMissaoExtraRoom(event.getChannel().getId())) {
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

    private boolean isMissaoExtraRoom(String idRoom) {
        Set<String> VALID_IDS = Set.of(
                SalaEnum.MISSAOEXTRA1.getId(),
                SalaEnum.MISSAOEXTRA2.getId(),
                SalaEnum.MISSAOEXTRA3.getId(),
                SalaEnum.MISSAOEXTRA4.getId(),
                SalaEnum.MISSAOEXTRA5.getId()
        );
        return VALID_IDS.contains(idRoom);
    }
}
