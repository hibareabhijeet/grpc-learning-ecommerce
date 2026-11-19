
package com.ecommerce.payment;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;

import java.util.UUID;

public class PaymentServiceImpl
        extends PaymentServiceGrpc.PaymentServiceImplBase {

    @Override
    public void processPayment(
            PaymentRequest request,
            StreamObserver<PaymentResponse> responseObserver) {

        System.out.println("Received payment request:");
        System.out.println("Order ID: " + request.getOrderId());
        System.out.println("Amount: " + request.getAmount());
        System.out.println("Currency: " + request.getCurrency());

        // Basic request validation
        if (request.getOrderId().isBlank()) {
            responseObserver.onError(
                    Status.INVALID_ARGUMENT
                            .withDescription("Order ID cannot be empty")
                            .asRuntimeException()
            );
            return;
        }

        if (request.getAmount() <= 0) {
            responseObserver.onError(
                    Status.INVALID_ARGUMENT
                            .withDescription("Payment amount must be greater than zero")
                            .asRuntimeException()
            );
            return;
        }

        if (request.getCurrency().isBlank()) {
            responseObserver.onError(
                    Status.INVALID_ARGUMENT
                            .withDescription("Currency cannot be empty")
                            .asRuntimeException()
            );
            return;
        }

        // Simulate payment processing
        String paymentId = UUID.randomUUID().toString();

        PaymentResponse response = PaymentResponse.newBuilder()
                .setPaymentId(paymentId)
                .setOrderId(request.getOrderId())
                .setStatus("SUCCESS")
                .setMessage("Payment processed successfully")
                .build();

        System.out.println("Payment successful: " + paymentId);

        // Send response to the client
        responseObserver.onNext(response);

        // Signal that the response is complete
        responseObserver.onCompleted();
    }
}
