package backend.iothub;

import backend.iothub.models.Command;
import org.springframework.stereotype.Component;

import java.util.EmptyStackException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@Component
public class InMemoryCommandBuffer implements CommandBuffer {

    private BlockingQueue<Command> queue = new ArrayBlockingQueue<>(10000);

    @Override
    public void buffer(Command command) {
        queue.add(command);
    }

    @Override
    public Command take() {
        return queue.poll();
    }
}
