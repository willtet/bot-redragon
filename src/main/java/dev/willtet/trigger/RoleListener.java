package dev.willtet.trigger;

import dev.willtet.database.DatabaseService;
import dev.willtet.enumeration.RoleEnum;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class RoleListener extends ListenerAdapter {
    @Override
    public void onGuildMemberRoleAdd(GuildMemberRoleAddEvent event) {
        // Obtém o usuário que recebeu o cargo
        var members = event.getMember();
        var user = members.getUser();

        if (event.getRoles().stream().anyMatch(role -> role.getId().equals(RoleEnum.APRENDIZ.getId()))){
            var status = DatabaseService.cadastrarUsuarioLegends(
                    user.getGlobalName(),
                    user.getName(),
                    user.getId()
            );
            if (status) {
                Role aprendiz = event.getGuild().getRoleById(RoleEnum.APRENDIZ.getId());

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
                                "- GlobalUserName -> " + user.getGlobalName() + " \r" +
                                "- Name -> " + user.getName() + " \r" +
                                "- UserId -> " + user.getId() + " \r").queue();
                    });
                }
            }
        }

    }
}
