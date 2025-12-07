package provider;

import exception.LanguageNotSupportedException;
import exception.UserNotFoundException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.time.Instant;
import java.util.Map;

@Provider
public class LanguageNotSupportedExceptionProvider implements ExceptionMapper<LanguageNotSupportedException> {

    @Override
    public Response toResponse(LanguageNotSupportedException exception) {

        final Map<String, Object> body = Map.of(
                "error", "Language not supported",
                "timestamp", Instant.now().toString()
        );

        return Response.status(Response.Status.NOT_FOUND)
                .entity(body)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

}
