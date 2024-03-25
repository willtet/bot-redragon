package dev.willtet.commands;

import dev.willtet.Main;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Cadastrar extends ListenerAdapter{
	@Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split(" ");
        TextChannel textChannel = (TextChannel) event.getChannel();
        if(event.getChannel().getId().equals("1221867194854408263")) {
        	if (args[0].equalsIgnoreCase("!"+"participar")){
                textChannel.sendMessage(Long.toString(Main.jda.getGatewayPing())).queue();
                
                
            }
        }

        
    }
}
