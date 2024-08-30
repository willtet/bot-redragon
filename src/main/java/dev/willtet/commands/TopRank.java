package dev.willtet.commands;

import dev.willtet.database.DatabaseService;
import dev.willtet.model.Role;
import dev.willtet.model.vo.TopRankVO;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.List;

public class TopRank extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split(" ");
        TextChannel textChannel = (TextChannel) event.getChannel();

        if (args[0].equalsIgnoreCase("!"+"rank")){
            List<TopRankVO> listaRank = DatabaseService.findTopRanking();
            String retorno = "";
            int rank = 1;

            for(TopRankVO vo: listaRank){
                retorno += rank + " - " + vo.getNome() + " \r";
                rank++;
            }

            textChannel.sendMessage(retorno).queue();
        }


    }
}
