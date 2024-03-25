package dev.willtet.commands;

import dev.willtet.database.DatabaseService;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Publicacao extends ListenerAdapter{

	@Override
	public void onMessageReactionAdd(MessageReactionAddEvent event) {
		super.onMessageReactionAdd(event);
		
		var reacao = event.getReaction();
		
		if(event.getChannel().getId().equals("12168983002974823201216898300297482320")) {
			if(reacao.getEmoji().asUnicode().equals(Emoji.fromUnicode("U+1f44d"))) {
//				DatabaseService.adicionarPontos(
//						
//						);
			}
		}
	}
	
	
}
