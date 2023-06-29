package plants.data;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "genus")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GenusEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    String name;
}

