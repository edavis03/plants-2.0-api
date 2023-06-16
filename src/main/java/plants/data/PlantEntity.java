package plants.data;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "plant")
public class PlantEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    String name;
}

