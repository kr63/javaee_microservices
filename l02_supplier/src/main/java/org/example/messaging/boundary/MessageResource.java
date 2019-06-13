package org.example.messaging.boundary;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("messages")
public class MessageResource {

    @GET
    public String message() {
        return "hey duke " + System.currentTimeMillis();
    }

}
