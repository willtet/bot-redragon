package dev.willtet.commands;

import dev.willtet.model.Constants;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class ResetCargo extends ListenerAdapter {
    public void onMessageReceived(MessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();
        MessageChannel responseChannel = event.getChannel();

        if (message.startsWith("!resetarCargo")) {
            if (event.getChannel().getId().equals(Constants.MODCOMANDO)) {
                String[] parts = message.split("\\s+", 2);
                if (parts.length < 2) {
                    event.getChannel().sendMessage("Por favor, especifique o nome do cargo que deseja remover.").queue();
                    return;
                }

                String roleId = parts[1];
                Guild guild = event.getGuild();

                Role roleToRemove = guild.getRoleById(roleId);
                if (roleToRemove == null) {
                    event.getChannel().sendMessage("Cargo não encontrado: " + roleId).queue();
                    return;
                }

                List<Member> members = guild.getMembers();

                for (Member member : members) {
                    if (member.getRoles().contains(roleToRemove)) {
                        guild.removeRoleFromMember(member, roleToRemove).queue(
                                success -> System.out.println("Cargo removido de " + member.getEffectiveName()),
                                error -> System.err.println("Falha ao remover o cargo de " + member.getEffectiveName())
                        );
                    }
                }

                event.getChannel().sendMessage("Cargo **" + roleToRemove.getName() + "** removido de todos os membros que o possuíam!").queue();

            }
        }
    }
}
