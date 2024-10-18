package dev.willtet.room;

import dev.willtet.database.DatabaseService;
import dev.willtet.enumeration.RoleEnum;
import dev.willtet.model.Constants;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class CadastroRoom extends ListenerAdapter {

    private final Set<String> usuariosEmProcessoDeCadastro = ConcurrentHashMap.newKeySet();

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event) {
        super.onMessageReactionAdd(event);


        if(event.getChannel().getId().equals("1203052120258121778")) {
            if (!usuariosEmProcessoDeCadastro.add(event.getUser().getId())) {
                return; // Já está em processo de cadastro, então ignoramos
            }

            try{
                boolean existe = DatabaseService.existeUsuarioCadastrado(event.getUser().getId());

                if(existe) {
                    usuariosEmProcessoDeCadastro.remove(event.getUser().getId());

                    event.getUser().openPrivateChannel().queue((privateChannel) -> {
                        privateChannel.sendMessage(event.getUser().getGlobalName() + " já está cadastrado no SquadPro").queue();
                    });
                }else {

                    var status = DatabaseService.cadastrarUsuarioLegends(
                            validarGlobalName(event.getUser()),
                            event.getUser().getName(),
                            event.getUser().getId()
                    );
                    if(status) {
                        Role aprendiz = event.getGuild().getRoleById("1231960763770605588");
                        TextChannel cadastroRoom = event.getJDA().getTextChannelById(Constants.CADASTRO_SQUAD);
                        event.getGuild().addRoleToMember(event.getUser(), aprendiz).queue();
                        event.getUser().openPrivateChannel().queue(
                                (privateChannel) -> {
                                    privateChannel.sendMessage(event.getUser().getGlobalName() + " acabou de se cadastrar no SquadPro!").queue(
                                            success -> System.out.println("Enviado"),
                                            failure -> cadastroRoom.sendMessage(event.getUser().getAsMention() + " Favor habilite a permissão de mensagens diretas para aproveitar melhor o servidor!")
                                                    .queue()

                                    );
                                },
                                (failure) -> {
                                    cadastroRoom.sendMessage(event.getUser().getAsMention() + " Favor habilite a permissão de mensagens diretas para aproveitar melhor o servidor!")
                                            .queue();

                                }
                        );
                    }else {
                        boolean doubleCheck = DatabaseService.existeUsuarioCadastrado(event.getUser().getId());
                        if(!doubleCheck){
                            Guild guild = event.getGuild();
                            Role modRole = guild.getRoleById(RoleEnum.MOD.getId());
                            for (Member member : guild.getMembersWithRoles(modRole)) {
                                // Abre o canal privado e envia a mensagem
                                member.getUser().openPrivateChannel().queue(privateChannel -> {
                                    privateChannel.sendMessage("Mensagem para Moderadores! Houve um erro do cadastro de um usuário no servidor SquadPro! - Cadastro em Regras \r" +
                                            "Dados: \r" +
                                            "- GlobalUserName -> " + event.getUser().getGlobalName() + " \r" +
                                            "- Name -> " + event.getUser().getName() + " \r" +
                                            "- UserId -> " + event.getUser().getId() + " \r").queue();
                                });
                            }
                        }

                    }
                }

            }finally {
                usuariosEmProcessoDeCadastro.remove(event.getUser().getId());
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
