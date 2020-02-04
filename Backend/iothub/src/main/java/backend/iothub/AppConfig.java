package backend.iothub;

import backend.iothub.models.Command;
import backend.iothub.models.Event;
import backend.iothub.models.EventType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Random;

@Configuration
@EnableBinding({EventsStream.class, CommandsStream.class})
@EnableScheduling
public class AppConfig {

    private boolean mqttConnected = false;
    private final MqttHandler mqttHandler;
    private final EventBuffer eventBuffer;
    private final CommandBuffer commandBuffer;
    private final EventsService eventsService;

    @Autowired
    public AppConfig(MqttHandler mqttHandler, EventBuffer eventBuffer, CommandBuffer commandBuffer, EventsService eventsService) {
        this.mqttHandler = mqttHandler;
        this.eventBuffer = eventBuffer;
        this.commandBuffer = commandBuffer;
        this.eventsService = eventsService;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void connectMqtt() {
        if (!mqttConnected) {
            mqttHandler.start();
            mqttConnected = true;
        }
    }

    @Scheduled(fixedDelay = 500)
    public void mqttKafkaBridge() {
        Event event = eventBuffer.take();
        if (event != null) {
            eventsService.sendEvent(event);
        }
    }

    @Scheduled(fixedDelay = 500)
    public void kafkaMqttBridge() {
        Command command = commandBuffer.take();
        if (command != null && mqttConnected) {
            mqttHandler.publish(command);
        }
    }
}