package com.example.mecroservices;

import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class ExceptionalPipelinesTest {

    @Test
    public void handleException() throws ExecutionException, InterruptedException {
        CompletableFuture.supplyAsync(this::exceptional)
                .exceptionally(this::transform)
                .thenAccept(this::consume)
                .get();
    }

    @Test
    public void handleNormal() throws ExecutionException, InterruptedException {
        CompletableFuture.supplyAsync(this::normalString)
                .handle(this::handle)
                .thenAccept(this::consume)
                .get();
    }

    private String handle(String valid, Throwable t) {
        return valid + " -- " + t;
    }

    private void consume(String message) {
        System.out.println("message: " + message);
    }

    private String transform(Throwable throwable) {
        return throwable.toString();
    }

    private String exceptional() {
        throw new IllegalStateException("happens");
    }

    private String normalString() {
        return "ha-ha-ha";
    }
}
