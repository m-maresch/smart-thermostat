package mqttdemo;

import lombok.SneakyThrows;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttHandler implements MqttCallback {

    private MqttClient sampleClient;
    private String topic = "MQTT Examples";

    @SneakyThrows
    public void start() {
        String broker = "tcp://192.168.99.100:1883";
        String clientId = "JavaSample";
        MemoryPersistence persistence = new MemoryPersistence();

        sampleClient = new MqttClient(broker, clientId, persistence);
        MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setCleanSession(true);
        System.out.println("Connecting to broker: "+broker);
        sampleClient.connect(connOpts);
        sampleClient.setCallback(this);
        sampleClient.subscribe(topic);
        System.out.println("Connected");
    }

    @SneakyThrows
    public void publish() {
        int qos = 2;
        String content = "Message from MqttPublishSample";

        System.out.println("Publishing message: "+content);
        MqttMessage message = new MqttMessage(content.getBytes());
        message.setQos(qos);
        sampleClient.publish(topic, message);
    }

    public void connectionLost(Throwable throwable) {

    }

    public void messageArrived(String topic, MqttMessage message) {
        System.out.println("Received: " + message);
    }

    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }

    @SneakyThrows
    public void stop() {
        sampleClient.disconnect();
        System.out.println("Disconnected");
        System.exit(0);
    }
}
