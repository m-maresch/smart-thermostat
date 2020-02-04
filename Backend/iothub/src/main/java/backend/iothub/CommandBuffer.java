package backend.iothub;

import backend.iothub.models.Command;

public interface CommandBuffer {
    void buffer(Command command);
    Command take();
}
