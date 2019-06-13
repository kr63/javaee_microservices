package org.example.messaging.boundary;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("messages")
public class MessageResource {

    @GET
    public String message() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Logger.getLogger(MessageResource.class.getName()).log(Level.SEVERE, null, e);
        }
        return "hey duke " + System.currentTimeMillis();
    }

    @POST
    public void message(String message) {
        System.out.println("(POST in MessageResource.class) message = " + message);
    }

}
