package backend.apigateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface EventsStream {

    String INPUT = "events-in";
    String OUTPUT = "events-out";

    @Input(INPUT)
    SubscribableChannel inboundEvents();

    @Output(OUTPUT)
    MessageChannel outboundEvents();
}
