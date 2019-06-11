package com.example.mecroservices;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BasicsTest {

    private void display() {
        System.out.println("ha-ha-ha");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Logger.getLogger(BasicsTest.class.getName()).log(Level.SEVERE, null, e);
            e.printStackTrace();
        }
    }

    private String message() {
        return "ha-ha-ha" + System.currentTimeMillis();
    }

    private void duration(long start) {
        System.out.println("-- took: " + (System.currentTimeMillis() - start));
    }

    private void onOverload(Runnable r, ThreadPoolExecutor executor) {
        System.out.println("-- runnable " + r + " executor " + executor.getActiveCount());
    }

    @Test
    public void references() {
        Runnable run = this::display;

        new Thread(run).start();
    }

    @Test
    public void callable() throws ExecutionException, InterruptedException {
        Callable<String> messageProvider = this::message;
        ExecutorService threadPool = Executors.newFixedThreadPool(2);
        List<Future<String>> futures = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Future<String> futureResult = threadPool.submit(messageProvider);
            futures.add(futureResult);
        }

        for (Future<String> future : futures) {
            String result = future.get();
            System.out.println("Result: " + result);
        }
    }

    @Test
    public void backPressure() {
        BlockingQueue<Runnable> queue = new LinkedBlockingDeque<>(2);

        ThreadPoolExecutor tp = new ThreadPoolExecutor(
                1, 1,
                60, TimeUnit.SECONDS,
                queue, this::onOverload);

        long start = System.currentTimeMillis();
        tp.submit(this::display);
        duration(start);
        tp.submit(this::display);
        duration(start);
        tp.submit(this::display);
        duration(start);
        tp.submit(this::display);
        duration(start);
    }

    @Test
    public void threads() throws InterruptedException {
        List<Thread> pool = new ArrayList<>();
        for (int i = 0; i < 10_000; i++) {
            Runnable run = this::display;
            Thread t = new Thread(run);
            pool.add(t);
            t.setName("My Thread");
            t.start();
            Thread.sleep(10);
        }
    }

    @Test
    public void threadPool() throws InterruptedException {
        ExecutorService threadPool = Executors.newFixedThreadPool(5);

        for (int i = 0; i < 10_000; i++) {
            Runnable run = this::display;
            threadPool.submit(run);
            Thread.sleep(10);
        }
    }
}
