
package com.ecommerce.payment;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

import java.util.concurrent.TimeUnit;

public class PaymentClient {

    public static void main(String[] args) throws InterruptedException {

        // Establish connection to Payment Service
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("localhost", 9091)
                .usePlaintext()
                .build();

        try {

            // Create a blocking stub
            PaymentServiceGrpc.PaymentServiceBlockingStub paymentStub =
                    PaymentServiceGrpc.newBlockingStub(channel);

            // Prepare payment request
            PaymentRequest request = PaymentRequest.newBuilder()
                    .setOrderId("ORD-1001")
                    .setAmount(0)
                    .setCurrency("SEK")
                    .build();

            System.out.println("Sending payment request...");

            // Invoke remote procedure
            PaymentResponse response =
                    paymentStub.processPayment(request);

            System.out.println("\nPayment Response:");
            System.out.println("Payment ID: " + response.getPaymentId());
            System.out.println("Order ID: " + response.getOrderId());
            System.out.println("Status: " + response.getStatus());
            System.out.println("Message: " + response.getMessage());

        } catch (StatusRuntimeException e) {

            System.err.println("gRPC request failed!");
            System.err.println("Status: " + e.getStatus());
            System.err.println("Description: "
                    + e.getStatus().getDescription());

        } finally {

            // Close connection
            channel.shutdown();

            if (!channel.awaitTermination(5, TimeUnit.SECONDS)) {
                channel.shutdownNow();
            }

            System.out.println("\nClient connection closed.");
        }
    }
}
