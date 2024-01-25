package store.analyticsservice;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import store.contracts.analytics.AnalyticsServiceGrpc.AnalyticsServiceImplBase;
import store.contracts.analytics.PopularProductsRequest;
import store.contracts.analytics.PopularProductsResponse;

import static store.analyticsservice.PurchasesProcessor.POPULAR_PRODUCTS_STORE;

@GrpcService
public class AnalyticsService extends AnalyticsServiceImplBase {

    private final StreamsBuilderFactoryBean streamsBuilderFactoryBean;

    @Autowired
    public AnalyticsService(StreamsBuilderFactoryBean streamsBuilderFactoryBean) {
        this.streamsBuilderFactoryBean = streamsBuilderFactoryBean;
    }

    @Override
    public void getPopularProducts(PopularProductsRequest request, StreamObserver<PopularProductsResponse> responseObserver) {
        var store = getStore();

        var responseBuilder = PopularProductsResponse.newBuilder();

        try (var iterator = store.all()) {
            while (iterator.hasNext()) {
                var keyValue = iterator.next();
                responseBuilder.putProductCounts(keyValue.key, keyValue.value);
            }
        }

        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }

    private ReadOnlyKeyValueStore<String, Long> getStore() {
        var kafkaStreams = streamsBuilderFactoryBean.getKafkaStreams();
        return kafkaStreams.store(StoreQueryParameters.fromNameAndType(POPULAR_PRODUCTS_STORE, QueryableStoreTypes.keyValueStore()));
    }
}
