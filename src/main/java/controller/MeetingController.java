package controller;

import dto.AppUserResponse;
import entity.AppUserEntity;
import exception.MeetingNotFoundException;
import exception.UserNotFoundException;
import io.quarkus.security.Authenticated;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;

import java.util.Optional;

@Path("/meeting")
@Authenticated
@SecurityRequirement(name = "keycloak")
public class MeetingController {

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Recupera le informazioni su un meeting",
            description = "Recupera le informazioni su un meeting tramite il suo id"
    )
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Operazione completata con successo", content = @Content(mediaType = MediaType.APPLICATION_JSON)),
            @APIResponse(responseCode = "401", description = "Chi ha fatto la richiesta non si è autenticato", content = @Content(mediaType = MediaType.APPLICATION_JSON)),
            @APIResponse(responseCode = "404", description = "Il meeting con id inserito non esiste", content = @Content(mediaType = MediaType.APPLICATION_JSON)),
    })
    public Response getMeeting(
            final @PathParam("id") Long id
    ) {
        final var optional = AppUserEntity.findByIdOptional(id);
        if (optional.isPresent())
            return Response.ok(optional.get()).build();
        else
            throw new MeetingNotFoundException();
    }


}
