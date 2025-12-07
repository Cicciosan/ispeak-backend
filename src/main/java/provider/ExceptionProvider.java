package provider;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.Map;

@Provider
@Slf4j
public class ExceptionProvider implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception exception) {

        log.error("Errore durante la richiesta: {}", exception.getMessage());

        final Map<String, Object> body = Map.of(
                "timestamp", Instant.now().toString()
        );

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(body)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
