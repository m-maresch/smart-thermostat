package backend.apigateway;

import backend.apigateway.models.Command;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.server.WebSocketService;
import org.springframework.web.reactive.socket.server.support.HandshakeWebSocketService;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;
import org.springframework.web.reactive.socket.server.upgrade.TomcatRequestUpgradeStrategy;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableBinding({EventsStream.class, CommandsStream.class})
@EnableScheduling
public class AppConfig {

    private final WebSocketHandler webSocketHandler;

    private final CommandBuffer commandBuffer;
    private final CommandsService commandsService;

    @Autowired
    public AppConfig(WebSocketHandler webSocketHandler, CommandBuffer commandBuffer, CommandsService commandsService) {
        this.webSocketHandler = webSocketHandler;
        this.commandBuffer = commandBuffer;
        this.commandsService = commandsService;
    }

    @Bean
    public HandlerMapping webSocketHandlerMapping() {
        Map<String, WebSocketHandler> map = new HashMap<>();
        map.put("/eventEmitter", webSocketHandler);

        SimpleUrlHandlerMapping handlerMapping = new SimpleUrlHandlerMapping();
        handlerMapping.setOrder(-1);
        handlerMapping.setUrlMap(map);
        return handlerMapping;
    }

    @Bean
    public WebSocketHandlerAdapter handlerAdapter() {
        return new WebSocketHandlerAdapter();
    }

    @Scheduled(fixedDelay = 500)
    public void webSocketKafkaBridge() {
        Command command = commandBuffer.take();
        if (command != null) {
            commandsService.sendCommand(command);
        }
    }
}