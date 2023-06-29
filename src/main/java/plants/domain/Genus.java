package plants.domain;

import lombok.Builder;

import java.util.UUID;

public record Genus(
        UUID id,
        String name
) {
    @Builder public Genus {}
}
