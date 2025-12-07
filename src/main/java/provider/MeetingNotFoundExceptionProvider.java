package provider;

import exception.MeetingNotFoundException;
import exception.UserNotFoundException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.time.Instant;
import java.util.Map;

@Provider
public class MeetingNotFoundExceptionProvider implements ExceptionMapper<MeetingNotFoundException> {

    @Override
    public Response toResponse(MeetingNotFoundException exception) {

        final Map<String, Object> body = Map.of(
                "error", "Meeting not found",
                "timestamp", Instant.now().toString()
        );

        return Response.status(Response.Status.NOT_FOUND)
                .entity(body)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

}
