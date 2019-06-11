package com.example.mecroservices;

import org.junit.Test;

import java.util.Comparator;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PipelineTest {

    @Test
    public void pipeline() {
        CompletableFuture.supplyAsync(this::message)
                .thenApply(this::beautify)
                .thenAccept(this::consumeMessage)
                .thenRun(this::finalAction);
    }

    @Test
    public void combiningPipelines() throws ExecutionException, InterruptedException {

        long start = System.currentTimeMillis();

        CompletableFuture<String> first =
                CompletableFuture.supplyAsync(this::message)
                        .thenApplyAsync(this::beautify);

        CompletableFuture<String> second =
                CompletableFuture.supplyAsync(this::greetings)
                        .thenApplyAsync(this::beautify);

        first.thenCombine(second, this::combinator)
                .thenAccept(this::consumeMessage).get();

        System.out.println("\nTake time: " + (System.currentTimeMillis() - start));
    }

    @Test
    public void composingPipelines() {
        CompletableFuture.supplyAsync(this::message)
                .thenCompose(this::compose)
                .thenAccept(this::consumeMessage);
    }

    private CompletionStage<String> compose(String input) {
        return CompletableFuture.supplyAsync(() -> input)
                .thenApply(this::beautify);
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
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Logger.getLogger(PipelineTest.class.getName()).log(Level.SEVERE, null, e);
        }
        return "[+ " + input + " +]";
    }

    private void consumeMessage(String message) {
        System.out.println("Consume message: " + message);
    }

    private void finalAction() {
        System.out.println("Clean up!");
    }
}
