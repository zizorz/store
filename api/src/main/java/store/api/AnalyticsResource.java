package store.api;

import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import store.api.dtos.PopularProductDto;
import store.api.dtos.PopularProductsDto;
import store.contracts.analytics.AnalyticsServiceGrpc.AnalyticsServiceBlockingStub;
import store.contracts.analytics.PopularProductsRequest;
import store.contracts.shopping.GetProductsRequest;
import store.contracts.shopping.Product;
import store.contracts.shopping.ShoppingServiceGrpc;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;

@RestController()
@RequestMapping("/analytics")
@CrossOrigin
public class AnalyticsResource {

    @GrpcClient("analytics-service")
    private AnalyticsServiceBlockingStub analyticsService;

    @GrpcClient("shopping-service")
    private ShoppingServiceGrpc.ShoppingServiceBlockingStub shoppingServiceStub;

    @GetMapping(value = "/popular-products", produces = "application/json")
    public ResponseEntity<PopularProductsDto> popularProducts() throws ExecutionException, InterruptedException {
        var request = PopularProductsRequest.newBuilder().build();
        var productsRequest = GetProductsRequest.newBuilder().build();

        var popularProductFuture  = CompletableFuture.supplyAsync(() -> analyticsService.getPopularProducts(request));
        var productsFuture = CompletableFuture.supplyAsync(() -> shoppingServiceStub.getProducts(productsRequest));

        var combinedFuture = CompletableFuture.allOf(popularProductFuture, productsFuture);

        combinedFuture.join();

        var popularProductsResponse = popularProductFuture.get();
        var productsResponse = productsFuture.get();

        var productMap = productsResponse.getProductsList().stream().collect(Collectors.toMap(Product::getId, Product::getName));

        var popularProducts = popularProductsResponse.getProductCountsMap()
                .entrySet()
                .stream()
                .map(entry -> {
                    var name = productMap.getOrDefault(entry.getKey(), "Unknown");
                    return new PopularProductDto(entry.getKey(), name, entry.getValue());
                })
                .sorted(comparing(PopularProductDto::purchasedCount).reversed())
                .toList();


        return ResponseEntity.ok(new PopularProductsDto(popularProducts));
    }
}
