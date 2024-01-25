package store.analyticsservice.serdes;

import com.google.protobuf.InvalidProtocolBufferException;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;
import store.contracts.shopping.Purchase;

public class PurchaseSerde implements Serde<Purchase> {

    @Override
    public Serializer<Purchase> serializer() {
        return (s, t) -> t.toByteArray();
    }

    @Override
    public Deserializer<Purchase> deserializer() {
        return (s, bytes) -> {
            try {
                return Purchase.parseFrom(bytes);
            } catch (InvalidProtocolBufferException e) {
                throw new RuntimeException(e);
            }
        };
    }

    public static PurchaseSerde create() {
        return new PurchaseSerde();
    }
}
