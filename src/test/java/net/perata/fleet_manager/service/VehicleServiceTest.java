package net.perata.fleet_manager.service;

import net.perata.fleet_manager.domain.Vehicle;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VehicleServiceTest {


    @Test
    void test() {
        Vehicle vehicle = Instancio.create(Vehicle.class);
        Long id = vehicle.getId();
        assertNotNull(id);
    }
}