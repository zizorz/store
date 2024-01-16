package store.shoppingservice;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import store.contracts.shopping.GetProductsRequest;
import store.contracts.shopping.GetProductsResponse;
import store.contracts.shopping.Product;
import store.contracts.shopping.ShoppingServiceGrpc.ShoppingServiceImplBase;


@GrpcService
public class ShoppingService extends ShoppingServiceImplBase {

    @Override
    public void getProducts(GetProductsRequest request, StreamObserver<GetProductsResponse> responseObserver) {
        System.out.println(Thread.currentThread().getName());
        System.out.println(Thread.currentThread().isVirtual());

        var products = GetProductsResponse.newBuilder()
                .addProducts(Product.newBuilder()
                        .setId("1")
                        .setName("Product 1")
                        .build())
                .addProducts(Product.newBuilder()
                        .setId("2")
                        .setName("Product 2"))
                .addProducts(Product.newBuilder()
                        .setId("3")
                        .setName("Product 3")
                        .build());


        responseObserver.onNext(products.build());
        responseObserver.onCompleted();
    }
}
