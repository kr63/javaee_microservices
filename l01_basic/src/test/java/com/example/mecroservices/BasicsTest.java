package com.example.mecroservices;

import org.junit.Test;

public class BasicsTest {

    @Test
    public void references() {
        Runnable run = this::display;

        new Thread(run).start();
    }

    private void display() {
        System.out.println("ha-ha-ha");
    }

}
