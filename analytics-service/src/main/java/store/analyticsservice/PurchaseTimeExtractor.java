package store.analyticsservice;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.streams.processor.TimestampExtractor;
import store.contracts.shopping.Purchase;

import java.time.Instant;

public class PurchaseTimeExtractor implements TimestampExtractor {
    @Override
    public long extract(ConsumerRecord<Object, Object> consumerRecord, long l) {
        var purchase = (Purchase) consumerRecord.value();

        var seconds = purchase.getTimestamp().getSeconds();
        var nanos = purchase.getTimestamp().getNanos();

        var time = Instant.ofEpochSecond(seconds, nanos);

        return time.toEpochMilli();
    }
}
