package org.example.messages;

import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

public class ClientTest {

    private Client client;
    private WebTarget tut;
    private WebTarget processor;

    @Before
    public void setUp() {
        this.client = ClientBuilder.newClient();
        this.tut = this.client.target("http://localhost:8080/supplier/resources/messages");
        this.processor = this.client.target("http://localhost:8080/processor/resources/processors/beautification");
    }

    @Test
    public void fetchMessage() throws ExecutionException, InterruptedException {
        String messageSingle= this.tut.request().get(String.class);
        System.out.println("Single message: " + messageSingle);

        Supplier<String> messageSupplier = () -> this.tut.request().get(String.class);

        CompletableFuture.supplyAsync(messageSupplier)
                .thenAccept(this::consume)
                .get();

        CompletableFuture.supplyAsync(messageSupplier)
                .thenAccept(this::post)
                .get();
    }

    @Test
    public void fetchAndProcess() throws ExecutionException, InterruptedException {
        Supplier<String> messageSupplier = () -> this.tut.request().get(String.class);
        CompletableFuture.supplyAsync(messageSupplier)
                .thenApply(this::process)
                .thenAccept(this::post)
                .get();

    }

    private String process(String input) {
        Response response = this.processor.request().post(Entity.text(input));
        return response.readEntity(String.class);

    }

    private void consume(String message) {
        System.out.println("Consumed message: " + message);
    }

    private void post(String message) {
        this.tut.request().post(Entity.text(message));
    }
}
