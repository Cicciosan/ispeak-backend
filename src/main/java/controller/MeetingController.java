package controller;

import dto.MeetingCreationRequest;
import entity.AppUserEntity;
import entity.MeetingEntity;
import exception.ConflictingResourceException;
import exception.MeetingNotFoundException;
import exception.UserNotRegisteredException;
import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import service.AppUserService;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Path("/meeting")
@Authenticated
@SecurityRequirement(name = "keycloak")
public class MeetingController {

    @Inject
    JsonWebToken jwt;
    @Inject
    AppUserService appUserService;

    @POST
    @Operation(
            summary = "Crea il tuo meeting",
            description = "Crea il tuo meeting, puoi avere un solo meeting alla volta"
    )
    @APIResponses({
            @APIResponse(responseCode = "204", description = "Operazione completata con successo"),
            @APIResponse(responseCode = "400", description = "Uno o più campi non sono compilati correttamente", content = @Content(mediaType = MediaType.APPLICATION_JSON)),
            @APIResponse(responseCode = "401", description = "Chi ha fatto la richiesta non si è autenticato", content = @Content(mediaType = MediaType.APPLICATION_JSON)),
            @APIResponse(responseCode = "409", description = "L'utente ha già un meeting pianificato", content = @Content(mediaType = MediaType.APPLICATION_JSON)),
    })
    @Transactional
    public Response registerUser(
            @Valid @RequestBody MeetingCreationRequest body
    ) {

        for (var entity : MeetingEntity.listAll()) {
            final var meeting = (MeetingEntity) entity;
            if (meeting.startDate.isAfter(LocalDateTime.now()))
                throw new ConflictingResourceException();
        }

        final var me = appUserService.getMySelf();

        final MeetingEntity meeting = new MeetingEntity();
        meeting.host = me;
        meeting.title = body.getTitle();
        meeting.description = body.getDescription();
        meeting.startDate = body.getStartDate();
        meeting.duration = body.getDuration();
        meeting.maxParticipants = body.getMaxParticipants();
        meeting.participants = new ArrayList<>();
        meeting.participants.add(me);
        meeting.persist();

        return Response.noContent().build();
    }

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

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Recupera le informazioni sul proprio meeting",
            description = "Recupera le informazioni sul proprio meeting"
    )
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Operazione completata con successo", content = @Content(mediaType = MediaType.APPLICATION_JSON)),
            @APIResponse(responseCode = "401", description = "Chi ha fatto la richiesta non si è autenticato", content = @Content(mediaType = MediaType.APPLICATION_JSON)),
            @APIResponse(responseCode = "404", description = "Attualmente non hai meeting organizzati", content = @Content(mediaType = MediaType.APPLICATION_JSON)),
    })
    public Response getMeeting(

    ) {

        final var me = appUserService.getMySelf();

        for (var meeting : me.meetings) {
            if (meeting.startDate.isAfter(LocalDateTime.now()))
                return Response.ok(meeting).build();
        }
        throw new MeetingNotFoundException();
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Revoca il meeting",
            description = "Revoca il meeting organizzato"
    )
    @APIResponses({
            @APIResponse(responseCode = "204", description = "Operazione completata con successo", content = @Content(mediaType = MediaType.APPLICATION_JSON)),
            @APIResponse(responseCode = "401", description = "Chi ha fatto la richiesta non si è autenticato", content = @Content(mediaType = MediaType.APPLICATION_JSON)),
            @APIResponse(responseCode = "404", description = "Al momento non si hanno meeting attivi", content = @Content(mediaType = MediaType.APPLICATION_JSON)),
    })
    @Transactional
    public Response deleteMeeting(

    ) {
        final var me = appUserService.getMySelf();

        for (var meeting : me.meetings) {
            if (meeting.startDate.isAfter(LocalDateTime.now())) {
                MeetingEntity.deleteById(meeting.id);
                return Response.noContent().build();
            }
        }
        throw new MeetingNotFoundException();
    }
}
