package provider;

import exception.ConflictingResourceException;
import exception.UserNotFoundException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.time.Instant;
import java.util.Map;

@Provider
public class ConflictingResourceExceptionProvider implements ExceptionMapper<ConflictingResourceException> {

    @Override
    public Response toResponse(ConflictingResourceException exception) {

        final Map<String, Object> body = Map.of(
                "error", "Resource still exists",
                "timestamp", Instant.now().toString()
        );

        return Response.status(Response.Status.CONFLICT)
                .entity(body)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
