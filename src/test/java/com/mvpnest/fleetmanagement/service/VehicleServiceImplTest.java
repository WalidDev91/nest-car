package com.mvpnest.fleetmanagement.service;

import com.mvpnest.fleetmanagement.dto.vehicle.VehicleDTO;
import com.mvpnest.fleetmanagement.entity.Vehicle;
import com.mvpnest.fleetmanagement.exception.ResourceNotFoundException;
import com.mvpnest.fleetmanagement.mapper.VehicleMapper;
import com.mvpnest.fleetmanagement.repository.VehicleRepository;
import com.mvpnest.fleetmanagement.service.impl.VehicleServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VehicleServiceImplTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private VehicleMapper vehicleMapper;

    @InjectMocks
    private VehicleServiceImpl vehicleService;

    @Test
    void getVehicleById_whenVehicleExists_returnsVehicleDTO() {

        UUID id = UUID.randomUUID();

        Vehicle vehicle = Vehicle.builder().id(id).plateNumber("123 TUN 456").brand("Mercedes").model("Actros").year(2022).build();

        VehicleDTO expectedDto = new VehicleDTO();
        expectedDto.setId(id);
        expectedDto.setPlateNumber("123 TUN 456");

        when(vehicleRepository.findById(id)).thenReturn(Optional.of(vehicle));
        when(vehicleMapper.toDTO(vehicle)).thenReturn(expectedDto);

        VehicleDTO result = vehicleService.getVehicleById(id);

        assertThat(result).isNotNull();
        assertThat(result.getPlateNumber()).isEqualTo("123 TUN 456");

    }

    @Test
    void getVehicleById_whenVehicleDoesNotExist_throwsResourceNotFoundException() {

        UUID id = UUID.randomUUID();

        when(vehicleRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> vehicleService.getVehicleById(id)).isInstanceOf(ResourceNotFoundException.class).hasMessage("Vehicle not found");

    }

}