package org.example.messaging.boundary;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Path("messages")
public class MessageResource {

    @GET
    public String message() {
        return "hey duke " + System.currentTimeMillis();
    }

    @POST
    public void message(String message) {
        System.out.println("(POST in MessageResource.class) message = " + message);
    }

}
