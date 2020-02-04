package mqttdemo;

import lombok.SneakyThrows;

import java.util.concurrent.CompletableFuture;

public class Main {

    public static void main(String[] args) {
        MqttHandler handler = new MqttHandler();

        handler.start();

        handler.publish();

        CompletableFuture<Void> publisher1 = CompletableFuture.runAsync(handler::publish);
        CompletableFuture<Void> publisher2 = CompletableFuture.runAsync(handler::publish);
        CompletableFuture<Void> publisher3 = CompletableFuture.runAsync(handler::publish);
        CompletableFuture<Void> slowPublisher = CompletableFuture.runAsync(new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                Thread.sleep(5000);
                handler.publish();
            }
        });

        CompletableFuture.allOf(publisher1, publisher2, publisher3, slowPublisher)
                .thenRun(handler::stop);
    }
}
