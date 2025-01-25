package dev.willtet.commands;

import dev.willtet.Main;
import dev.willtet.model.Constants;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.GenericGuildEvent;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Ping extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split(" ");


        if (event.getChannel() instanceof TextChannel) {
            TextChannel textChannel = (TextChannel) event.getChannel();

            if (args[0].equalsIgnoreCase("!"+"ping")){
                if (event.getChannel().getId().equals(Constants.MODCOMANDO)){
                    textChannel.sendMessage(Long.toString(Main.jda.getGatewayPing())).queue();
                }
            }
        }
    }
}
