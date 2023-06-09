package plants.domain;

import lombok.*;

import java.util.UUID;

public record Plant(
        UUID id,
        String name,
        Genus genus
) {
    @Builder public Plant {}
}
