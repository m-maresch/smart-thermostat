package backend.iothub;

import backend.iothub.models.Event;
import org.springframework.stereotype.Component;

import java.util.EmptyStackException;
import java.util.Stack;

@Component
public class InMemoryEventBuffer implements EventBuffer {

    private Stack<Event> stack = new Stack<>();

    @Override
    public void buffer(Event event) {
        stack.push(event);
    }

    @Override
    public Event take() {
        try {
            return stack.peek();
        } catch (EmptyStackException e) {
            return null;
        }
    }
}
