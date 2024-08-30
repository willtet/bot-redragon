package dev.willtet.commands;

import dev.willtet.Main;
import dev.willtet.database.DatabaseService;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Cadastrar extends ListenerAdapter{
	@Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split(" ");
        TextChannel textChannel = (TextChannel) event.getChannel();
        if(event.getChannel().getId().equals("1203053040802865225")) {
        	if (args[0].equalsIgnoreCase("!"+"participar")){
                boolean existe = DatabaseService.existeUsuarioCadastrado(event.getAuthor().getId());
                
                if(existe) {
                	textChannel.sendMessage(event.getAuthor().getGlobalName() + " já está cadastrado no SquadPro").queue();
                }else {
                	
                	var status = DatabaseService.cadastrarUsuarioLegends(
                			event.getAuthor().getGlobalName(), 
                			event.getAuthor().getName(),
                			event.getAuthor().getId()
                			);
                	if(status) {
                		Role aprendiz = event.getGuild().getRoleById("1231960763770605588");
                		
                		event.getGuild().addRoleToMember(event.getAuthor(), aprendiz).queue();
                    	textChannel.sendMessage(event.getAuthor().getGlobalName() + " acabou de se cadastrar no SquadPro!").queue();
                	}else {
                    	textChannel.sendMessage("Teve um erro no seu cadastro, algum Staff pode te ajudar!").queue();

                	}
                }
            }
        }

        
    }
}
