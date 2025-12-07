package dto;

import entity.LanguageEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.LocalDate;

@Builder
@Data
@Schema(description = "Richiesta di registrazione di un utente")
public class AppUserRegistrationRequest {

    @NotBlank (message = "Name is required")
    @Size(min = 2, max = 50, message = "Name length must be within 2 and 50 characters")
    @Schema(examples = "John")
    private final String name;

    @NotBlank (message = "Surname is required")
    @Size(min = 2, max = 50, message = "Surname length must be within 2 and 50 characters")
    @Schema(examples = "Smith")
    private final String surname;

    @NotNull(message = "Birthday is required")
    @Past(message = "Birthday must be in the past")
    @Schema(examples = "1999-05-10")
    private final LocalDate birthday;

    @NotBlank (message = "Mother language is required")
    @Schema(examples = "en")
    private final String motherLanguageCode;

    @NotBlank (message = "Learning language is required")
    @Schema(examples = "it")
    private final String learningLanguageCode;

}
