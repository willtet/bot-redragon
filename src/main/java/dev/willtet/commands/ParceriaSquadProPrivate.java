package dev.willtet.commands;

import dev.willtet.model.Constants;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ParceriaSquadProPrivate extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();
        MessageChannel responseChannel = event.getChannel();

        if (message.startsWith("!parceria")) {
            if (event.getChannel().getId().equals(Constants.MODCOMANDO)) {
                String[] parts = message.split("\\s+", 2);
                if (parts.length < 2) {
                    responseChannel.sendMessage("Uso correto: `!parceria {lista de IDs de mensagens separados por vírgula}:{Marca da parceria}`").queue();
                    return;
                }else if(parts[1].split(":").length < 2){
                    responseChannel.sendMessage("Uso correto: `!parceria {lista de IDs de mensagens separados por vírgula}:{Marca da parceria}`").queue();
                    return;
                }

                String marca = parts[1].split(":")[1];

                for (String id : parts[1].split(":")[0].split(",")) {
                    String idUser = id.trim();
                    try {
                        User usuario = event.getGuild().getMemberById(idUser).getUser();

                        usuario.openPrivateChannel().queue(
                                (privateChannel) -> {
                                    privateChannel.sendMessage(mensagem(marca)).queue(
                                            success -> responseChannel.sendMessage("Enviado mensagem para " + usuario.getName() + ": "+idUser ).queue(),
                                            failure -> responseChannel.sendMessage("Erro: Mensagem privada negada para " + usuario.getName() + ", entre em contato para enviar a mensagem!").queue()

                                    );
                                },
                                (failure) -> {
                                    responseChannel.sendMessage("Erro: Mensagem privada negada para " + usuario.getName() + ", entre em contato para enviar a mensagem!").queue();
                                }
                        );

                    }catch(Exception e){
                        responseChannel.sendMessage("Usuario inválido: "+idUser).queue();
                    }



                }
                


            }
        }


    }

    private String mensagem(String marca){
        StringBuilder mensagemRetorno = new StringBuilder();
        mensagemRetorno.append("Parabéns!! \n");
        mensagemRetorno.append("Você foi selecionado para uma ação com "+ marca+ "\n");
        mensagemRetorno.append("\n");
        mensagemRetorno.append("Formato: Permuta \n");
        mensagemRetorno.append("Entrega esperada: 1 Story de recebimento + 1 Reels em collab do produto que deve ser repostado nas outras redes \n");
        mensagemRetorno.append("\n");
        mensagemRetorno.append("Para participar: \n");
        mensagemRetorno.append("Acesse o site abaixo e preencha os dados necessários\n");
        mensagemRetorno.append("https://www.squadpro.co//collab-2024-001 \n");
        mensagemRetorno.append("\n");
        mensagemRetorno.append("Após o envio, confirme aqui o e-mail informado. \n");
        mensagemRetorno.append("__ \n");
        mensagemRetorno.append("POR FAVOR CONFIRA AS INFORMAÇÕES PARA EVITAR PROBLEMAS NO ENVIO DO PRODUTO. Caso haja alguma informação incompatível não será possível realizar a ação.\n");
        mensagemRetorno.append("\n");
        mensagemRetorno.append("Qualquer dúvida entre em contato com um Moderador. \n");

        return mensagemRetorno.toString();
    }
}
