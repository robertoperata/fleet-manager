package net.perata.fleet_manager.controller.mapper;

import net.perata.fleet_manager.controller.dto.VehicleDTO;
import net.perata.fleet_manager.domain.Vehicle;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VehicleMapper extends EntityMapper<VehicleDTO, Vehicle> {


}
