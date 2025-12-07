package dto;

import entity.AppUserEntity;
import entity.LanguageEntity;
import entity.MeetingEntity;
import lombok.Builder;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import java.util.List;

@Data
@Builder
@Schema(description = "Risposta alla richiesta di informazioni su utente applicativo")
public class AppUserResponse {

    private final String guid;
    private final String name;
    private final String surname;
    private final List<MeetingEntity> meetings;
    private final String motherLanguage;
    private final String learningLanguage;


    public static AppUserResponse fromEntity (final AppUserEntity entity) {
        return AppUserResponse.builder()
                .guid(entity.guid)
                .name(entity.name)
                .surname(entity.surname)
                .meetings(entity.meetings)
                .motherLanguage(entity.motherLanguage.code)
                .learningLanguage(entity.learningLanguage.code)
                .build();
    }
}
