package controller;

import dto.AppUserRegistrationRequest;
import entity.AppUserEntity;
import entity.LanguageEntity;
import exception.ConflictingResourceException;
import exception.LanguageNotSupportedException;
import exception.UserNotFoundException;
import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import dto.AppUserResponse;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;

import java.io.IOException;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

@Path("/user")
@Authenticated
@SecurityRequirement(name = "keycloak")
public class AppUserController {

    @Inject
    JsonWebToken jwt;

    @ConfigProperty(name = "app.paths.user-icons")
    String iconsPath;

    @GET
    @Path("/me")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Recupera le informazioni su te stesso",
            description = "Recupera le informazioni su te stesso"
    )
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Operazione completata con successo: sei registrato", content = @Content(mediaType = MediaType.APPLICATION_JSON)),
            @APIResponse(responseCode = "204", description = "Operazione completata con successo: non sei registrato", content = @Content(mediaType = MediaType.APPLICATION_JSON)),
            @APIResponse(responseCode = "401", description = "Chi ha fatto la richiesta non si è autenticato", content = @Content(mediaType = MediaType.APPLICATION_JSON)),
    })
    public Response getMyUser(
    ) {
        final String guid = jwt.getSubject();
        final Optional<AppUserEntity> user = AppUserEntity.findByIdOptional(guid);
        if (user.isPresent()) {
            final AppUserResponse response = AppUserResponse.fromEntity(user.get());
            return Response.ok(response).build();
        }
        else return Response.noContent().build();
    }

    @POST
    @Path("me/register")
    @Operation(
            summary = "Registrati sul sistema",
            description = "Registra per la prima volta un utente sul sistema"
    )
    @APIResponses({
            @APIResponse(responseCode = "204", description = "Operazione completata con successo"),
            @APIResponse(responseCode = "400", description = "Uno o più campi non sono compilati correttamente", content = @Content(mediaType = MediaType.APPLICATION_JSON)),
            @APIResponse(responseCode = "401", description = "Chi ha fatto la richiesta non si è autenticato", content = @Content(mediaType = MediaType.APPLICATION_JSON)),
            @APIResponse(responseCode = "404", description = "Lingua inserita non supportata", content = @Content(mediaType = MediaType.APPLICATION_JSON)),
            @APIResponse(responseCode = "409", description = "L'utente è già registrato", content = @Content(mediaType = MediaType.APPLICATION_JSON)),
    })
    @Transactional
    public Response registerUser(
            @Valid @RequestBody AppUserRegistrationRequest body
            ) {

        final String guid = jwt.getSubject();
        AppUserEntity.findByIdOptional(guid).ifPresent((entity) -> {throw new ConflictingResourceException();});

        final var motherLanguage = (LanguageEntity) LanguageEntity.findByIdOptional(body.getMotherLanguageCode()).orElseThrow(LanguageNotSupportedException::new);
        final var learningLanguage = (LanguageEntity) LanguageEntity.findByIdOptional(body.getLearningLanguageCode()).orElseThrow(LanguageNotSupportedException::new);

        final var user = new AppUserEntity();
        user.guid = guid;
        user.name = body.getName();
        user.surname = body.getSurname();
        user.birthday = body.getBirthday();
        user.motherLanguage = motherLanguage;
        user.learningLanguage = learningLanguage;
        AppUserEntity.persist(user);

        return Response.noContent().build();
    }


    @GET
    @Path("/{guid}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Recupera le informazioni su un utente",
            description = "Recupera le informazioni su un utente applicativo"
    )
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Operazione completata con successo", content = @Content(mediaType = MediaType.APPLICATION_JSON)),
            @APIResponse(responseCode = "401", description = "Chi ha fatto la richiesta non si è autenticato", content = @Content(mediaType = MediaType.APPLICATION_JSON)),
            @APIResponse(responseCode = "404", description = "L'utente con guid inserito non esiste", content = @Content(mediaType = MediaType.APPLICATION_JSON)),
    })
    public Response getUser(
            final @PathParam("guid") String guid
    ) {
        final Optional<AppUserEntity> user = AppUserEntity.findByIdOptional(guid);
        if (user.isPresent()) {
            final AppUserResponse response = AppUserResponse.fromEntity(user.get());
            return Response.ok(response).build();
        }
        else throw new UserNotFoundException();
    }

    @GET
    @Path("/{guid}/icon")
    @Produces("image/webp")
    @Operation(
            summary = "Recupera l'icona di un utente",
            description = "Recupera l'icona di un utente applicativo"
    )
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Operazione completata con successo: l'utente ha un icona", content = @Content(mediaType = "image/webp")),
            @APIResponse(responseCode = "204", description = "Operazione completata con successo: l'utente non ha un'icona"),
            @APIResponse(responseCode = "401", description = "Chi ha fatto la richiesta non si è autenticato", content = @Content(mediaType = MediaType.APPLICATION_JSON)),
            @APIResponse(responseCode = "404", description = "L'utente con guid inserito non esiste", content = @Content(mediaType = MediaType.APPLICATION_JSON)),
    })
    public Response getUserIcon(
            final @PathParam("guid") String guid
    ) throws IOException {

        final Optional<AppUserEntity> userOptional = AppUserEntity.findByIdOptional(guid);
        if (userOptional.isPresent()) {
            final AppUserEntity user = userOptional.get();
            final java.nio.file.Path iconPath = Paths.get(iconsPath, user.iconHash);
            if (iconPath.toFile().exists()) {
                final byte[] bytes = Files.readAllBytes(iconPath);
                return Response.ok(bytes).build();
            }
            else
                return Response.noContent().build();
        }
        else throw new UserNotFoundException();
    }

    @POST
    @Path("me/icon")
    @Operation(
            summary = "Imposta la tua icona",
            description = "Imposta l'icona dell'utente corrente"
    )
    @APIResponses({
            @APIResponse(responseCode = "204", description = "Operazione completata con successo"),
            @APIResponse(responseCode = "401", description = "Chi ha fatto la richiesta non si è autenticato", content = @Content(mediaType = MediaType.APPLICATION_JSON)),
            @APIResponse(responseCode = "404", description = "Non sei ancora registrato", content = @Content(mediaType = MediaType.APPLICATION_JSON))
    })
    public Response setIcon(

    ) throws IOException {

        final String guid = jwt.getSubject();
        final AppUserEntity user = AppUserEntity.findById(guid);
        final java.nio.file.Path iconPath = Paths.get(iconsPath, user.iconHash);

        // TODO
        throw new AtomicMoveNotSupportedException("p", "r", "n");
    }
}
