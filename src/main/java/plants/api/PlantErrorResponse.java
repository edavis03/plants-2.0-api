package plants.api;

import lombok.Builder;
import org.springframework.http.HttpStatus;

public record PlantErrorResponse(
        String type,
        HttpStatus status,
        String detail
) {
    @Builder public PlantErrorResponse {}
}
