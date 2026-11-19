package com.ecommerce.payment;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class PaymentServer {

    private static final int PORT = 9091;

    private Server server;

    private void start() throws IOException {

        server = ServerBuilder
                .forPort(PORT)
                .addService(new PaymentServiceImpl())
                .build()
                .start();

        System.out.println("==================================");
        System.out.println("Payment gRPC Server started");
        System.out.println("Listening on port: " + PORT);
        System.out.println("==================================");

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down Payment Server...");

            try {
                PaymentServer.this.stop();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            System.out.println("Payment Server stopped.");
        }));
    }

    private void stop() throws InterruptedException {

        if (server != null) {
            server.shutdown();

            if (!server.awaitTermination(5, TimeUnit.SECONDS)) {
                server.shutdownNow();
            }
        }
    }

    private void blockUntilShutdown() throws InterruptedException {

        if (server != null) {
            server.awaitTermination();
        }
    }

    public static void main(String[] args)
            throws IOException, InterruptedException {

        PaymentServer paymentServer = new PaymentServer();

        paymentServer.start();

        paymentServer.blockUntilShutdown();
    }
}
