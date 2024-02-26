package store.api;


import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import store.api.dtos.PurchaseDto;
import store.contracts.creditcard.AuthorizeCreditCardRequest;
import store.contracts.creditcard.CreditCardServiceGrpc.CreditCardServiceBlockingStub;
import store.contracts.shopping.PurchaseProductRequest;
import store.contracts.shopping.ShoppingServiceGrpc.ShoppingServiceBlockingStub;

@RestController()
@CrossOrigin(origins = "*")
public class PurchaseResource {

    @GrpcClient("creditcard-service")
    private CreditCardServiceBlockingStub creditCardServiceStub;

    @GrpcClient("shopping-service")
    private ShoppingServiceBlockingStub shoppingServiceStub;


    @PostMapping(value = "/purchases", consumes = "application/json", produces = "application/json")
    public PurchaseDto purchaseProduct(@RequestBody PurchaseRequest request) {
        var creditCardRequest = AuthorizeCreditCardRequest
                .newBuilder()
                .setCreditCardNumber(request.creditCardNumber())
                .build();

        var creditCardResponse = creditCardServiceStub.authorizeCreditCard(creditCardRequest);

        if (!creditCardResponse.getAuthorized()) {
            throw new ResponseStatusException(HttpStatus.PAYMENT_REQUIRED, "Credit card not authorized");
        }

        var purchaseRequest = PurchaseProductRequest.newBuilder()
                .setProductId(request.productId())
                .build();

        var purchaseResponse = shoppingServiceStub.purchaseProduct(purchaseRequest);

        return new PurchaseDto(purchaseResponse.getPurchaseId(), purchaseResponse.getProductId());
    }

}
