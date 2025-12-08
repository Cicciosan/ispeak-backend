package provider;

import exception.UserNotFoundException;
import exception.UserNotRegisteredException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.time.Instant;
import java.util.Map;

@Provider
public class UserNotRegisteredExceptionProvider implements ExceptionMapper<UserNotRegisteredException> {

    @Override
    public Response toResponse(UserNotRegisteredException exception) {

        final Map<String, Object> body = Map.of(
                "error", "User not registered",
                "timestamp", Instant.now().toString()
        );

        return Response.status(Response.Status.UNAUTHORIZED)
                .entity(body)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

}
