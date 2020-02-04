package backend.apigateway;

import backend.apigateway.models.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class EventsListener {

    private final EventBuffer buffer;

    @Autowired
    public EventsListener(EventBuffer buffer) {
        this.buffer = buffer;
    }

    @StreamListener(EventsStream.INPUT)
    public void handleEvents(@Payload Event event) {
        buffer.buffer(event);
    }
}
