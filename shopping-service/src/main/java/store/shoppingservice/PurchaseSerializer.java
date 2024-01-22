package store.shoppingservice;

import org.apache.kafka.common.serialization.Serializer;
import store.contracts.shopping.Purchase;

public class PurchaseSerializer implements Serializer<Purchase> {

    @Override
    public byte[] serialize(String s, Purchase purchase) {
        return purchase.toByteArray();
    }

}
