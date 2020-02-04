package backend.iothub;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface CommandsStream {

    String INPUT = "commands-in";
    String OUTPUT = "commands-out";

    @Input(INPUT)
    SubscribableChannel inboundCommands();

    @Output(OUTPUT)
    MessageChannel outboundCommands();
}