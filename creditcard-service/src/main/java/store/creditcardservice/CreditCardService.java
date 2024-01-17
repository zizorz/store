package store.creditcardservice;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import store.contracts.creditcard.AuthorizeCreditCardRequest;
import store.contracts.creditcard.AuthorizeCreditCardResponse;
import store.contracts.creditcard.CreditCardServiceGrpc.CreditCardServiceImplBase;

import java.util.Set;

@GrpcService
public class CreditCardService extends CreditCardServiceImplBase {


    private final Set<String> validCreditCards = Set.of("1234-1234-1234-1234", "5678-5678-5678-5678");

    @Override
    public void authorizeCreditCard(AuthorizeCreditCardRequest request, StreamObserver<AuthorizeCreditCardResponse> responseObserver) {
        var authorized = validCreditCards.contains(request.getCreditCardNumber());

        var response = AuthorizeCreditCardResponse.newBuilder()
                .setAuthorized(authorized)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
