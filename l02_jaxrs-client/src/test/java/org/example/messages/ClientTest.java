package org.example.messages;

import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

public class ClientTest {

    private Client client;
    private WebTarget tut;

    @Before
    public void setUp() {
        this.client = ClientBuilder.newClient();
        this.tut = this.client.target("http://localhost:8080/supplier/resources/messages");
    }

    @Test
    public void fetchMessage() throws ExecutionException, InterruptedException {
        String messageSingle= this.tut.request().get(String.class);
        System.out.println("Single message: " + messageSingle);

        Supplier<String> messageSupplier = () -> this.tut.request().get(String.class);
        CompletableFuture.supplyAsync(messageSupplier).thenAccept(this::consume).get();
    }

    private void consume(String message) {
        System.out.println("Consumed message: " + message);
    }
}
