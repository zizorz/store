package store.shoppingservice;

import com.google.protobuf.Timestamp;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import store.contracts.shopping.*;
import store.contracts.shopping.ShoppingServiceGrpc.ShoppingServiceImplBase;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;


@GrpcService
public class ShoppingService extends ShoppingServiceImplBase {


    private final PurchasesProducer purchasesProducer;

    private final Set<Product> products = Set.of(
            Product.newBuilder()
                    .setId("1")
                    .setName("Product 1")
                    .build(),
            Product.newBuilder()
                    .setId("2")
                    .setName("Product 2")
                    .build(),
            Product.newBuilder()
                    .setId("3")
                    .setName("Product 3")
                    .build()
    );

    public ShoppingService(PurchasesProducer purchasesProducer) {
        this.purchasesProducer = purchasesProducer;
    }


    @Override
    public void getProducts(GetProductsRequest request, StreamObserver<GetProductsResponse> responseObserver) {
        System.out.println(Thread.currentThread().getName());
        System.out.println(Thread.currentThread().isVirtual());

        var products = GetProductsResponse.newBuilder()
                .addAllProducts(this.products);

        responseObserver.onNext(products.build());
        responseObserver.onCompleted();
    }

    @Override
    public void purchaseProduct(PurchaseProductRequest request, StreamObserver<PurchaseProductResponse> responseObserver) {
        var product = products.stream().filter(p -> p.getId().equals(request.getProductId())).findFirst();

        if (product.isEmpty()) {
            responseObserver.onError(new IllegalArgumentException("Product not found"));
            responseObserver.onCompleted();
            return;
        }

        var purchaseId = UUID.randomUUID();


        var now = Instant.now();
        var purchase = Purchase.newBuilder()
                .setId(purchaseId.toString())
                .setProductId(product.get().getId())
                .setTimestamp(Timestamp.newBuilder().setSeconds(now.getEpochSecond()).setNanos(now.getNano()).build())
                .build();

        purchasesProducer.sendMessage(purchaseId.toString(), purchase);

        var response = PurchaseProductResponse.newBuilder()
                .setProductId(product.get().getId())
                .setPurchaseId(purchaseId.toString())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
