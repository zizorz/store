package store.shoppingservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import store.contracts.shopping.Purchase;

import java.util.concurrent.ExecutionException;

@Component
public class PurchasesProducer {

    @Value(value = "${kafka.topic.purchases}")
    private String topic;


    private final KafkaTemplate<String, Purchase> kafkaTemplate;

    public PurchasesProducer(KafkaTemplate<String, Purchase> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String key, Purchase message) {
        try {
            kafkaTemplate.send(topic, key, message).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

}
