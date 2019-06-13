package org.example.messages;

import org.glassfish.jersey.client.ClientProperties;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

public class ClientTest {

    private Client client;
    private WebTarget tut;
    private WebTarget processor;

    @Before
    public void setUp() {
        this.client = ClientBuilder.newClient();

        client.property(ClientProperties.CONNECT_TIMEOUT, 100);
        client.property(ClientProperties.READ_TIMEOUT, 500);

        this.tut = this.client.target("http://localhost:8080/supplier/resources/messages");
        this.processor = this.client.target("http://localhost:8080/processor/resources/processors/beautification");
    }

    @Test
    public void fetchMessage() throws ExecutionException, InterruptedException {
        String messageSingle = this.tut.request().get(String.class);
        System.out.println("Single message: " + messageSingle);

        Supplier<String> messageSupplier = () -> this.tut.request().get(String.class);

        CompletableFuture.supplyAsync(messageSupplier)
                .thenAccept(this::printMessage)
                .get();

        CompletableFuture.supplyAsync(messageSupplier)
                .thenAccept(this::consume)
                .get();
    }

    @Test
    public void fetchAndProcess() throws ExecutionException, InterruptedException {
        Supplier<String> messageSupplier = () -> this.tut.request().get(String.class);
        CompletableFuture.supplyAsync(messageSupplier)
                .thenApply(this::process)
                .thenAccept(this::consume)
                .get();

    }

    @Test
    public void fetchAndProcessWithPool() throws ExecutionException, InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(5);
        Supplier<String> messageSupplier = () -> this.tut.request().get(String.class);
        CompletableFuture.supplyAsync(messageSupplier, pool)
                .thenApply(this::process)
                .exceptionally(this::handle)
                .thenAccept(this::consume)
                .get();

    }

    private String handle(Throwable t) {
        return "Sorry server overloaded";
    }

    private String process(String input) {
        Response response = this.processor.request().post(Entity.text(input));
        return response.readEntity(String.class);

    }

    private void printMessage(String message) {
        System.out.println("Consumed message: " + message);
    }

    private void consume(String message) {
        this.tut.request().post(Entity.text(message));
    }
}
