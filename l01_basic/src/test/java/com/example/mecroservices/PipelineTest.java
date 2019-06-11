package com.example.mecroservices;

import org.junit.Test;

import java.util.concurrent.CompletableFuture;

public class PipelineTest {

    @Test
    public void pipeline() {
        CompletableFuture.supplyAsync(this::message)
                .thenApply(this::beautify)
                .thenAccept(this::consumeMessage)
                .thenRun(this::finalAction);
    }

    @Test
    public void combiningPipelines() {
        CompletableFuture<String> first = CompletableFuture.supplyAsync(this::message);
        CompletableFuture<String> second = CompletableFuture.supplyAsync(this::greetings);
        first.thenCombine(second, this::combinator)
                .thenApply(this::beautify)
                .thenAccept(this::consumeMessage);
    }

    private String message() {
        return "(ha-ha-ha: " + System.currentTimeMillis() + ")";
    }

    private String greetings() {
        return "{good morning!}";
    }

    private String combinator(String first, String second) {
        return "C: " + first + " -- " + second + " :C";
    }

    private String beautify(String input) {
        return "[+ " + input + " +]";

    }

    private void consumeMessage(String message) {
        System.out.println("Consume message: " + message);
    }

    private void finalAction() {
        System.out.println("Clean up!");
    }
}
