package plants.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import plants.domain.InvalidPlantException;

@RestControllerAdvice
@Slf4j
public class PlantControllerAdvice {
    public static final String INVALID_PLANT_DETAIL = "Name is a required property of a plant.";

    @ExceptionHandler(value = { InvalidPlantException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<PlantErrorResponse> badRequest(Exception ex)
    {
        log.warn("someone tried to post a poopy plant");
        var body = PlantErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST)
                .type(InvalidPlantException.class.getName())
                .detail(INVALID_PLANT_DETAIL)
                .build();
        return new ResponseEntity<>(body, body.status());
    }
}

