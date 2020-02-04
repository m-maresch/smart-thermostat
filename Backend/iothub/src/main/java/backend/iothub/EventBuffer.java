package backend.iothub;

import backend.iothub.models.Event;

public interface EventBuffer {
    void buffer(Event event);
    Event take();
}
