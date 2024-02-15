package store.api;

import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import store.api.dtos.ProductDto;
import store.contracts.shopping.GetProductsRequest;
import store.contracts.shopping.ShoppingServiceGrpc.ShoppingServiceBlockingStub;

import java.util.List;

@RestController()
@CrossOrigin
public class ProductResource {

    @GrpcClient("shopping-service")
    private ShoppingServiceBlockingStub shoppingServiceStub;

    @GetMapping("/products")
    public List<ProductDto> products() {
        System.out.println(Thread.currentThread().getName());
        System.out.println(Thread.currentThread().isVirtual());

        var request = GetProductsRequest.newBuilder().setLimit(5).build();
        var response = shoppingServiceStub.getProducts(request);

        return response.getProductsList()
                .stream()
                .map(product -> new ProductDto(product.getId(), product.getName()))
                .toList();
    }
}
