package store.api;

import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import store.api.dtos.PopularProductDto;
import store.api.dtos.PopularProductsDto;
import store.contracts.analytics.AnalyticsServiceGrpc.AnalyticsServiceBlockingStub;
import store.contracts.analytics.PopularProductsRequest;

import java.util.Comparator;

import static java.util.Comparator.comparing;

@RestController()
@RequestMapping("/analytics")
public class AnalyticsResource {

    @GrpcClient("analytics-service")
    private AnalyticsServiceBlockingStub analyticsService;

    @GetMapping(value = "/popular-products", produces = "application/json")
    public ResponseEntity<PopularProductsDto> popularProducts() {
        var request = PopularProductsRequest.newBuilder().build();
        var response = analyticsService.getPopularProducts(request);

        var popularProducts = response.getProductCountsMap()
                .entrySet()
                .stream()
                .map(entry -> new PopularProductDto(entry.getKey(), entry.getValue()))
                .sorted(comparing(PopularProductDto::purchasedCount).reversed())
                .toList();

        return ResponseEntity.ok(new PopularProductsDto(popularProducts));
    }
}
