package net.perata.fleet_manager.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "brand_gen_seq")
    @SequenceGenerator(name = "brand_gen_seq", sequenceName = "brand_gen")
    private Long id;

    private String name;

    private String model;

    @Enumerated(EnumType.STRING)
    private EngineType engineType;

    private String color;

    private String plate;

    @OneToMany(mappedBy = "brand")
    @OrderBy("name ASC")
    private List<Vehicle> vehicle;

    
}