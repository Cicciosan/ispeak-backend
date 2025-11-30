package controller;

import io.quarkus.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("/api-context")
@Authenticated
public class TempController {

    @GET
    @Path("/api-call")
    public Response getTemp() {
        return Response.ok("Le API funzionano?").build();
    }

}
