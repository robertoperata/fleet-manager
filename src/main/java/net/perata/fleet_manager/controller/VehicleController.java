package net.perata.fleet_manager.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import net.perata.fleet_manager.controller.dto.VehicleDTO;
import net.perata.fleet_manager.controller.mapper.VehicleMapper;
import net.perata.fleet_manager.domain.Vehicle;
import net.perata.fleet_manager.service.VehicleService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;
    private final VehicleMapper vehicleMapper;

    @Operation(summary = "Get all vehicles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of vehicles",
                    content = @Content(schema = @Schema(implementation = VehicleDTO.class)))
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<VehicleDTO>> getVehicles() {
        var vehicles = vehicleService.getVehicles();
        return ResponseEntity.ok(vehicleMapper.toDto(vehicles));
    }

    @Operation(summary = "Get details of a Vehicle by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Details of a Vehicle",
                    content = {@Content(schema = @Schema(implementation = VehicleDTO.class))})
    })
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<VehicleDTO> getVehicleById(@PathVariable("id") Long id) {
        var vehicle = vehicleService.getVehicleById(id);
        return ResponseEntity.ok(vehicleMapper.toDto(vehicle));
    }

    @Operation(summary = "Create a Vehicle")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Details of the created vehicle",
                    content = {@Content(schema = @Schema(implementation = VehicleDTO.class))},
                    headers = {@Header(name = HttpHeaders.LOCATION)}),
            @ApiResponse(responseCode = "422", description = "Entity not valid", content = @Content)
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<VehicleDTO> createVehicle(@RequestBody VehicleDTO vehicleDTO, HttpServletRequest req) {
        Vehicle vehicle = vehicleMapper.toEntity(vehicleDTO);
        var vehicleCreated = vehicleService.createVehicle(vehicle);
        String path = req.getServletPath();
        return ResponseEntity
                .created(URI.create(String.format("%s/%d", path, vehicleCreated.getId())))
                .body(vehicleMapper.toDto(vehicleCreated));
    }

    @Operation(summary = "Delete a vehicle")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "/{id}")
    public void deleteVehicle(@PathVariable("id") Long id) {
        vehicleService.deleteVehicleById(id);
    }
}
