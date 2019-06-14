package org.example.acync.boundary;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.concurrent.RejectedExecutionException;

@Provider
public class ServerOverloadHandler implements ExceptionMapper<RejectedExecutionException> {

    @Override
    public Response toResponse(RejectedExecutionException exception) {
        return Response
                .status(Response.Status.SERVICE_UNAVAILABLE)
                .header("overloaded-reason", exception.toString())
                .build();
    }
}
