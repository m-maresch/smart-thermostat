package backend.iothub;

import backend.iothub.models.Command;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class CommandsListener {

    private final CommandBuffer buffer;

    @Autowired
    public CommandsListener(CommandBuffer buffer) {
        this.buffer = buffer;
    }

    @StreamListener(CommandsStream.INPUT)
    public void handleCommands(@Payload Command command) {
        buffer.buffer(command);
    }
}
