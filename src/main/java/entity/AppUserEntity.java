package entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;

@Entity
@Schema(description = "Rappresentazione di un utente applicativo")
public class AppUserEntity extends PanacheEntityBase {

    @Id
    public String guid;

    @Column(nullable = false)
    public String name;

    @Column(nullable = false)
    public String surname;

    @Column(nullable = false)
    public LocalDate birthday;

    @Column(nullable = true)
    public String iconHash;

    @OneToMany
    public List<MeetingEntity> meetings;

    @ManyToOne
    public LanguageEntity motherLanguage;

    @ManyToOne
    public LanguageEntity learningLanguage;
}
