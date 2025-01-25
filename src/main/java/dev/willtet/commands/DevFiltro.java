package dev.willtet.commands;

import dev.willtet.database.DatabaseService;
import dev.willtet.enumeration.RoleEnum;
import dev.willtet.model.Constants;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class DevFiltro extends ListenerAdapter {

    // ID fixo do canal onde as mensagens serão buscadas
    private static final String TARGET_CHANNEL_ID = "1270072766619582635";

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();
        MessageChannel responseChannel = event.getChannel();

        if (message.startsWith("!filtro")) {
            if (event.getChannel().getId().equals(Constants.MODCOMANDO)) {
                String[] parts = message.split("\\s+", 2);
                if (parts.length < 2) {
                    responseChannel.sendMessage("Uso correto: `!filtro {lista de IDs de mensagens separados por vírgula}`").queue();
                    return;
                }

                switch (parts[1]) {
                    case "usuarioReagido":
                        filtrarUsuarioReagido(event, parts, responseChannel);
                        break;
                    case "usuarioSemCargo":
                        filtrarUsuarioSemCargo(event, parts, responseChannel);
                        break;
                    default:
                        responseChannel.sendMessage("Filtro não reconhecido: " + parts[1]).queue();
                        break;
                }
            }
        }
    }

    private void filtrarUsuarioSemCargo(MessageReceivedEvent event, String[] parts, MessageChannel responseChannel) {
        //Verificar se existe usuarios sem cargos base(aprendiz) cadastrados no banco
        Role role = event.getGuild().getRoleById(RoleEnum.APRENDIZ.getId());

        if (role == null) {
            System.out.println("Cargo não encontrado para o ID: " + RoleEnum.APRENDIZ.getId());
            return;
        }

        event.getGuild().loadMembers().onSuccess(members -> {
            for (Member member : members) {
                // Verifica se o membro não possui o cargo
                if (!member.getRoles().contains(role)) {
                    if(DatabaseService.existeUsuarioCadastrado(member.getId())){
                        System.out.println("Usuário sem o cargo mas cadastrado no banco: " + member.getEffectiveName());
                    }
                }
            }
        }).onError(throwable -> {
            System.err.println("Erro ao carregar os membros: " + throwable.getMessage());
        });

    }

    private static void filtrarUsuarioReagido(MessageReceivedEvent event, String[] parts, MessageChannel responseChannel) {
        String[] messageIds = parts[1].split(",");
        Set<User> uniqueUsers = ConcurrentHashMap.newKeySet();

        TextChannel targetChannel = event.getGuild().getTextChannelById(TARGET_CHANNEL_ID);
        if (targetChannel == null) {
            responseChannel.sendMessage("O canal com ID " + TARGET_CHANNEL_ID + " não foi encontrado.").queue();
            return;
        }

        // Lista de futuros para rastrear operações
        List<CompletableFuture<Void>> futures = Arrays.stream(messageIds)
                .map(String::trim)
                .map(messageId ->
                        targetChannel.retrieveMessageById(messageId)
                                .submit()
                                .thenCompose(retrievedMessage -> {
                                    // Para cada reação, obtemos os usuários que reagiram
                                    List<CompletableFuture<Void>> reactionFutures = retrievedMessage.getReactions().stream()
                                            .map(reaction -> reaction.retrieveUsers().submit()
                                                    .thenAccept(uniqueUsers::addAll)) // Adiciona os usuários à lista
                                            .toList();

                                    // Combina todos os futuros das reações
                                    return CompletableFuture.allOf(reactionFutures.toArray(new CompletableFuture[0]));
                                })
                                .exceptionally(ex -> {
                                    responseChannel.sendMessage("Não foi possível encontrar a mensagem com o ID: " + messageId).queue();
                                    return null;
                                })
                )
                .toList();

        // Aguarda todas as operações serem concluídas
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenRun(() -> {
                    // Após todas as operações, envia a resposta
                    StringBuilder response = new StringBuilder("Usuários únicos que reagiram:\n");
                    uniqueUsers.forEach(user -> response.append(user.getId()).append("\n"));
                    responseChannel.sendMessage(response.length() > 0 ? response.toString() : "Nenhum usuário reagiu às mensagens fornecidas.").queue();
                })
                .exceptionally(ex -> {
                    responseChannel.sendMessage("Ocorreu um erro ao processar as mensagens.").queue();
                    return null;
                }
        );
    }
}
