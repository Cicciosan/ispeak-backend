package dto;

import entity.MeetingEntity;
import lombok.Builder;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.LocalDateTime;

@Data
@Builder
@Schema(description = "Risposta alla richiesta di informazioni su meeting")
public class MeetingResponse {

    private final String hostGuid;
    private final String title;
    private final String description;
    private final LocalDateTime startDate;
    private final int duration;
    private final String location;
    private final int maxParticipants;
    private final int participants;

    
    public static MeetingResponse fromEntity(MeetingEntity entity) {
        return MeetingResponse.builder()
                .hostGuid(entity.host.guid)
                .title(entity.title)
                .description(entity.description)
                .startDate(entity.startDate)
                .duration(entity.duration)
                .maxParticipants(entity.maxParticipants)
                .participants(entity.participants)
                .build();
    }

}
