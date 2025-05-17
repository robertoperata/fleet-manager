package net.perata.fleet_manager.controller.dto;

import lombok.Getter;
import lombok.Setter;
import net.perata.fleet_manager.domain.Brand;
import net.perata.fleet_manager.domain.EngineType;

@Getter
@Setter
public class VehicleDTO {
    private Long id;

    private String name;

    private Brand brand;

    private EngineType engineType;

    private boolean wheelChair;

    private Integer maxPassengers;
}
