package entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Entity
@Schema(description = "Rappresentazione di una lingua supportata dal sistema")
public class LanguageEntity extends PanacheEntityBase {

    @Id
    public String code;

    @Column(nullable = false, unique = true)
    public String name;

}
