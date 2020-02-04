package backend.apigateway;

import backend.apigateway.models.Event;
import org.springframework.stereotype.Component;

import java.util.EmptyStackException;
import java.util.Iterator;
import java.util.Queue;
import java.util.Stack;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@Component
public class InMemoryEventBuffer implements EventBuffer, Iterable<Event> {

    private BlockingQueue<Event> queue = new ArrayBlockingQueue<>(1000);

    @Override
    public void buffer(Event event) {
        queue.add(event);
    }

    @Override
    public Event take() {
        return queue.poll();
    }

    public Iterator<Event> iterator() {
        return queue.iterator();
    }

}
