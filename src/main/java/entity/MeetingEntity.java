package entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

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

    @ManyToMany
    public List<AppUserEntity> participants;

    @ManyToOne
    @JoinColumn(name = "host_id", nullable = false)
    public AppUserEntity host;

}
