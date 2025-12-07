package entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.LocalDateTime;

@Entity
@Schema(description = "Rappresentazione di un meeting")
public class MeetingEntity extends PanacheEntity {

    @Column(nullable = false)
    public String title;

    @Column(nullable = false)
    public String description;

    @Column(nullable = false)
    public LocalDateTime startDate;

    @Column(nullable = false)
    public int duration;

    @Column(nullable = false)
    public int maxParticipants;

    @Column(nullable = false)
    public int participants;

    @ManyToOne
    public AppUserEntity host;

}
