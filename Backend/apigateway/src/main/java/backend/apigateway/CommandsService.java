package backend.apigateway;

import backend.apigateway.models.Command;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

@Service
public class CommandsService {

    private final CommandsStream stream;

    @Autowired
    public CommandsService(CommandsStream stream) {
        this.stream = stream;
    }

    public void sendCommand(final Command command) {
        MessageChannel messageChannel = stream.outboundCommands();
        messageChannel.send(MessageBuilder
                .withPayload(command)
                .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                .build());
    }
}
