package backend.iothub;

import backend.iothub.models.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

@Service
public class EventsService {

    private final EventsStream stream;

    @Autowired
    public EventsService(EventsStream stream) {
        this.stream = stream;
    }

    public void sendEvent(final Event event) {
        MessageChannel messageChannel = stream.outboundEvents();
        messageChannel.send(MessageBuilder
                .withPayload(event)
                .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                .build());
    }
}