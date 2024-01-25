package store.analyticsservice;


import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import store.analyticsservice.serdes.PurchaseSerde;
import store.contracts.shopping.Purchase;


@Component
public class PurchasesProcessor {

    public static final String POPULAR_PRODUCTS_STORE = "popular-products-store";

    @Value("${kafka.topic.purchases}")
    private String purchaseTopic;

    private static final PurchaseSerde PURCHASE_SERDE = PurchaseSerde.create();
    private static final Serde<String> STRING_SERDE = Serdes.String();

    @Autowired
    void buildPopularProductsStore(StreamsBuilder streamsBuilder) {
        KStream<String, Purchase> stream = streamsBuilder
                .stream(purchaseTopic, Consumed.with(STRING_SERDE, PURCHASE_SERDE).withTimestampExtractor(new PurchaseTimeExtractor()));

        KTable<String, Long> productCount = stream
                .peek((key, value) -> System.out.println("Received purchase: " + key + " - " + value.getProductId()))
                .mapValues(Purchase::getProductId)
                .groupBy((key, productId) -> productId, Grouped.with(STRING_SERDE, STRING_SERDE))
                .count()
                .filter((key, value) -> value > 10L, Materialized.<String, Long, KeyValueStore<Bytes, byte[]>>as(POPULAR_PRODUCTS_STORE).withValueSerde(Serdes.Long()).withCachingDisabled());
    }

}