package backend.apigateway;

import backend.apigateway.models.Command;

public interface CommandBuffer {
    void buffer(Command command);
    Command take();
}
