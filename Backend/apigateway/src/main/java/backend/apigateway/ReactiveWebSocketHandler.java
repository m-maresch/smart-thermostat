package backend.apigateway;

import backend.apigateway.models.Command;
import backend.apigateway.models.Event;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SynchronousSink;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.util.function.Tuple2;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Component
public class ReactiveWebSocketHandler implements WebSocketHandler {

    private final InMemoryEventBuffer eventBuffer;
    private final CommandBuffer commandBuffer;

    @Autowired
    public ReactiveWebSocketHandler(InMemoryEventBuffer eventBuffer, CommandBuffer commandBuffer) {
        this.eventBuffer = eventBuffer;
        this.commandBuffer = commandBuffer;
    }

    @Override
    public Mono<Void> handle(WebSocketSession webSocketSession) {
        ObjectMapper mapper = new ObjectMapper();

        Flux<Event> flux = Flux.zip(Flux.fromIterable(eventBuffer), Flux.interval(Duration.ofMillis(500)))
                .map(Tuple2::getT1);

        return webSocketSession.send(flux.map(event -> {
            try {
                return webSocketSession.textMessage(mapper.writeValueAsString(event));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }))
        .and(webSocketSession.receive()
                .map(webSocketMessage -> {
                    try {
                        return mapper.readValue(webSocketMessage.getPayloadAsText(), Command.class);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .doOnNext(commandBuffer::buffer)
                .log());
    }
}
