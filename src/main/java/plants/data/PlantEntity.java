package plants.data;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "plant")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlantEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    String name;

    @ManyToOne
    GenusEntity genus;
}

