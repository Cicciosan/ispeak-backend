package dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.LocalDateTime;

@Builder
@Data
@Schema(description = "Richiesta di registrazione di un utente")
public class MeetingCreationRequest {

    @NotBlank(message = "Title is required")
    @Size(min = 2, max = 50, message = "Name length must be within 2 and 50 characters")
    @Schema(examples = "Title")
    private final String title;

    @NotBlank(message = "Description is required")
    @Size(min = 2, max = 50, message = "Name length must be within 2 and 50 characters")
    @Schema(examples = "Description")
    private final String description;

    @NotNull (message = "Start date is required")
    @Future (message = "Start date must be in the future")
    @Schema(examples = "2032-12-09T00:00:00")
    private final LocalDateTime startDate;

    @NotNull (message = "Duration is required")
    @Min(value = 30, message = "The duration is too short")
    @Max(value = 300, message = "The duration is too long")
    @Schema(examples = "60")
    private final int duration;

    @NotNull (message = "Max participants is required")
    @Min(value = 2, message = "Maximum participants must be at least 2")
    @Max(value = 100, message = "Maximum participants can't be so many")
    @Schema(examples = "12")
    private final int maxParticipants;

}
