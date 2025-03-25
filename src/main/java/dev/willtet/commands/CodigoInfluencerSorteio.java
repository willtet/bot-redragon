package dev.willtet.commands;

import dev.willtet.database.DatabaseService;
import dev.willtet.model.Constants;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.text.MessageFormat;

public class CodigoInfluencerSorteio extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();
        MessageChannel responseChannel = event.getChannel();

        if (event.getChannel().getId().equals(Constants.CADASTRO_COD_INFLUENCER)) {
            if (message.startsWith("!codigo")) {
                String[] parts = message.split("\\s+", 2);
                if (parts.length < 2) {
                    responseChannel.sendMessage("Uso correto: `!codigo {código com 6 digitos}`").queue();
                    return;
                }else if(parts[1].length() != 6){
                    responseChannel.sendMessage("Código deve ter 6 dígitos").queue();
                    return;
                }

                String codigo = parts[1];

                if(!DatabaseService.isValidoCadastrarUsuarioSorteioCodigoInfluencer(event.getAuthor().getId())){
                    String codigoInfl = DatabaseService.findCodigoSorteioByUserId(event.getAuthor().getId());
                    responseChannel.sendMessage(MessageFormat.format("Influenciador {0} possui código \"{1}\" cadastrado",event.getAuthor().getGlobalName(), codigoInfl)).queue();
                    return;
                }

                if(!DatabaseService.isValidoCadastrarCodigoInfluencer(codigo)){
                    responseChannel.sendMessage("Código já utilizado pelo outro influenciador").queue();
                    return;
                }

                if(!DatabaseService.postCodigoInfluencer(codigo, event.getAuthor().getId())) {
                    responseChannel.sendMessage("Ocorreu um erro ao cadastrar o seu código, entre em contato com o moderador!").queue();
                    return;
                }

                responseChannel.sendMessage("Código cadastrado com sucesso!").queue();
            }
        }


    }
}
