package plants.domain;

import java.util.UUID;

public record Plant(
        UUID id,
        String name
) {
}
