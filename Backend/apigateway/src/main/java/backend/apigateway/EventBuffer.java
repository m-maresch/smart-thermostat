package backend.apigateway;

import backend.apigateway.models.Event;

public interface EventBuffer {
    void buffer(Event event);
    Event take();
}
