package starter;

import entity.LanguageEntity;
import io.quarkus.runtime.Startup;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.event.Observes;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Startup
@RequiredArgsConstructor
public class LanguageStarter {

    private final EntityManager em;

    private static final List<LanguageEntity> languages = new ArrayList<>();

    @Transactional
    public void init (@Observes StartupEvent ev) {

        final var english = new LanguageEntity();
        english.code = "en";
        english.name = "English";
        languages.add(english);

        final var italian = new LanguageEntity();
        italian.code = "it";
        italian.name = "Italian";
        languages.add(italian);

        final var spanish = new LanguageEntity();
        spanish.code = "es";
        spanish.name = "Spanish";
        languages.add(spanish);

        final var french = new LanguageEntity();
        french.code = "fr";
        french.name = "French";
        languages.add(french);

        final var portuguese = new LanguageEntity();
        portuguese.code = "pt";
        portuguese.name = "Portuguese";
        languages.add(portuguese);

        final var german = new LanguageEntity();
        german.code = "de";
        german.name = "German";
        languages.add(german);

        for (LanguageEntity language : languages) {
            em.persist(language);
        }
    }
}
