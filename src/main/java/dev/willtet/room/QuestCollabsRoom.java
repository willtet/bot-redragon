package dev.willtet.room;

import dev.willtet.database.DatabaseService;
import dev.willtet.enumeration.RoleEnum;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.entities.emoji.UnicodeEmoji;
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

public class QuestCollabsRoom extends ListenerAdapter implements BaseRoom{
    @Override
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {
        super.onMessageReactionAdd(event);
        if(event.getChannel().getId().equals("1272979331521708173")) {
            if(isMod(event.getMember().getRoles())){

                int pontoDecidido = validarEmojiPontos(event.getReaction().getEmoji().asUnicode());

                if(pontoDecidido > 0){
                    if(isMessageRegistered(event)){
                        DatabaseService.updatePontosByUsuarioEMessage(event.getMessageId(), event.getUserId(), true);
                    }else{
                        LocalDate hoje = LocalDate.now();
                        LocalDate domingo = hoje.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));


                        var mensagem = String.format("QUEST COLLABS - %d pontos", pontoDecidido);

                        if(DatabaseService.isValidoParaPontuarCollab(hoje, domingo, mensagem)){
                            DatabaseService.postPontosByUsuario(
                                    event.getMessageId(),
                                    event.getMessageAuthorId(),
                                    mensagem,
                                    pontoDecidido
                            );
                        }
                    }
                }
            }

            verificarAtualizacaoRoles(event);

        }
    }

    private int validarEmojiPontos(UnicodeEmoji emoji) {
        String unicode = emoji.getAsCodepoints().toUpperCase();

        switch (unicode){
            case "U+1F44D": // 👍 Reels / TikTok / Shorts = 100 pontos
                return 100;
            case "U+1F600": // 😀 Apresentação em Live/Vídeo com 6min+ = 300 pontos
                return 300;
            // Adicione mais casos conforme necessário
            default:
                return 0; // Valor padrão se o emoji não for reconhecido
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
        if(event.getChannel().getId().equals("1272979331521708173")) {
            if(isMod(event.getMember().getRoles())){
                Message message = event.retrieveMessage().complete();
                if(isMessageRegistered(event)){
                    DatabaseService.updatePontosByUsuarioEMessage(event.getMessageId(), message.getAuthor().getId(), false);
                }
            }


        }
    }

    private boolean isMod(List<Role> ids) {
        return ids.stream().anyMatch(id -> id.getId().equals(RoleEnum.MOD.getId()) || id.getId().equals(RoleEnum.MODASSISTENTE.getId()) );
    }
}
