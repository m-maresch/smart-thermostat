package backend.iothub;

import backend.iothub.models.Command;
import backend.iothub.models.Event;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.io.IOException;

@Component
public class MqttHandler implements MqttCallback {

    @Value("${mqtt.vernemq.brokers}")
    private String broker;
    @Value("${mqtt.client.id}")
    private String clientId;
    @Value("${mqtt.events.topic.name}")
    private String eventsTopic;
    @Value("${mqtt.commands.topic.name}")
    private String commandsTopic;
    private int qos = 2;
    private MqttClient mqttClient;

    private final EventBuffer buffer;

    @Autowired
    public MqttHandler(EventBuffer buffer) {
        this.buffer = buffer;
    }

    public void start() {
        try {
            MemoryPersistence persistence = new MemoryPersistence();

            mqttClient = new MqttClient(broker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            mqttClient.connect(connOpts);
            mqttClient.setCallback(this);
            mqttClient.subscribe(eventsTopic);
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }

    }

    public void publish(Command command) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            MqttMessage message = new MqttMessage(mapper.writeValueAsString(command).getBytes());
            message.setQos(qos);
            mqttClient.publish(commandsTopic, message);
        } catch (JsonProcessingException | MqttException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            Event event = mapper.readValue(new String(message.getPayload()), Event.class);
            buffer.buffer(event);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @PreDestroy
    public void stop() {
        try {
            mqttClient.disconnect();
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }

    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {}

    public void connectionLost(Throwable throwable) {}
}
