package org.example.acync.boundary;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("async")
public class AsyncResource {

    @GET
    public void get(@Suspended AsyncResponse response) {
        response.resume(this.doSomeWork());
    }

    private String doSomeWork() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Logger.getLogger(AsyncResource.class.getName()).log(Level.SEVERE, null, e);
            e.printStackTrace();
        }

        return "+" + System.currentTimeMillis();
    }
}
