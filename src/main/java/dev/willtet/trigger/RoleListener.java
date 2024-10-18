package dev.willtet.trigger;

import dev.willtet.database.DatabaseService;
import dev.willtet.enumeration.RoleEnum;
import dev.willtet.model.Constants;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class RoleListener extends ListenerAdapter {
    @Override
    public void onGuildMemberRoleAdd(GuildMemberRoleAddEvent event) {
        // Obtém o usuário que recebeu o cargo
        var members = event.getMember();
        var user = members.getUser();

        if (event.getRoles().stream().anyMatch(role -> role.getId().equals(RoleEnum.APRENDIZ.getId()))){
            boolean existe = DatabaseService.existeUsuarioCadastrado(event.getUser().getId());
            if(!existe){
                var status = DatabaseService.cadastrarUsuarioLegends(
                        validarGlobalName(user),
                        user.getName(),
                        user.getId()
                );
                if (status) {
                    Role aprendiz = event.getGuild().getRoleById(RoleEnum.APRENDIZ.getId());
                    TextChannel cadastroRoom = event.getJDA().getTextChannelById(Constants.CADASTRO_SQUAD);

                    event.getGuild().addRoleToMember(event.getUser(), aprendiz).queue();
                    event.getUser().openPrivateChannel().queue((privateChannel) -> {
                        privateChannel.sendMessage(event.getUser().getGlobalName() + " acabou de se cadastrar no SquadPro!").queue(
                                success -> System.out.println("Enviado"),
                                failure -> cadastroRoom.sendMessage(event.getUser().getAsMention() + ", acabou de se cadastrar no SquadPro! \r" +
                                        "Favor habilite a permissão de mensagens diretas para aproveitar melhor o servidor!").queue()
                        );
                    });
                }else {
                    boolean doubleCheck = DatabaseService.existeUsuarioCadastrado(user.getId());
                    if(!doubleCheck){
                        Guild guild = event.getGuild();
                        Role modRole = guild.getRoleById(RoleEnum.MOD.getId());

                        for (Member member : guild.getMembersWithRoles(modRole)) {
                            // Abre o canal privado e envia a mensagem
                            member.getUser().openPrivateChannel().queue(privateChannel -> {
                                privateChannel.sendMessage("Mensagem para Moderadores! Houve um erro do cadastro de um usuário no servidor SquadPro! - Cadastro MOD role \r" +
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

    }

    public static String removerEmojis(String input) {
        // Expressão regular para detectar emojis e outros símbolos não-textuais
        return input.replaceAll("[\\p{So}\\p{Cn}]", "");
    }

    public static String validarGlobalName(User user) {
        if(Objects.isNull(user.getGlobalName())){
            return user.getName();
        }

        String globalName = removerEmojis(user.getGlobalName());

        // Tentativa de converter a string para bytes no charset Latin1
        byte[] latin1Bytes = globalName.getBytes(StandardCharsets.ISO_8859_1);

        // Reconvertendo os bytes para String usando Latin1
        String decoded = new String(latin1Bytes, StandardCharsets.ISO_8859_1);

        // Comparar o texto original com o reconvertido
        if(globalName.equals(decoded)){
            return globalName;
        }

        return user.getName();
    }
}
