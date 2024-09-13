package dev.willtet.room;

import dev.willtet.database.DatabaseService;
import dev.willtet.enumeration.RoleEnum;
import dev.willtet.model.Constants;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public class CadastroRoom extends ListenerAdapter {
    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event) {
        super.onMessageReactionAdd(event);
        TextChannel textChannel = (TextChannel) event.getChannel();

        if(event.getChannel().getId().equals("1203052120258121778")) {
            boolean existe = DatabaseService.existeUsuarioCadastrado(event.getUser().getId());

            if(existe) {
                event.getUser().openPrivateChannel().queue((privateChannel) -> {
                    privateChannel.sendMessage(event.getUser().getGlobalName() + " já está cadastrado no SquadPro").queue();
                });
                //textChannel.sendMessage(event.getUser().getGlobalName() + " já está cadastrado no SquadPro").queue();
            }else {

                var status = DatabaseService.cadastrarUsuarioLegends(
                        event.getUser().getGlobalName(),
                        event.getUser().getName(),
                        event.getUser().getId()
                );
                if(status) {
                    Role aprendiz = event.getGuild().getRoleById("1231960763770605588");

                    event.getGuild().addRoleToMember(event.getUser(), aprendiz).queue();
                    event.getUser().openPrivateChannel().queue((privateChannel) -> {
                        privateChannel.sendMessage(event.getUser().getGlobalName() + " acabou de se cadastrar no SquadPro!").queue();
                    });
                }else {
                    Guild guild = event.getGuild();
                    Role modRole = guild.getRoleById(RoleEnum.MOD.getId());
                    for (Member member : guild.getMembersWithRoles(modRole)) {
                        // Abre o canal privado e envia a mensagem
                        member.getUser().openPrivateChannel().queue(privateChannel -> {
                            privateChannel.sendMessage("Mensagem para Moderadores! Houve um erro do cadastro de um usuário no servidor SquadPro! \r" +
                                    "Dados: \r" +
                                    "- GlobalUserName -> " + event.getUser().getGlobalName() + " \r" +
                                    "- Name -> " + event.getUser().getName() + " \r" +
                                    "- UserId -> " + event.getUser().getId() + " \r").queue();
                        });
                    }
                }
            }
        }
    }
}
