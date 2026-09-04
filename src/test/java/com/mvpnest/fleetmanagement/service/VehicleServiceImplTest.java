package com.mvpnest.fleetmanagement.service;

import com.mvpnest.fleetmanagement.dto.vehicle.CreateVehicleRequest;
import com.mvpnest.fleetmanagement.dto.vehicle.UpdateVehicleRequest;
import com.mvpnest.fleetmanagement.dto.vehicle.VehicleDTO;
import com.mvpnest.fleetmanagement.entity.User;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

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

    @Test
    void getAllVehicles_returnsMappedVehicles() {

        Vehicle vehicle = Vehicle.builder().id(UUID.randomUUID()).plateNumber("123 TUN 456").brand("Mercedes").model("Actros").year(2022).build();

        VehicleDTO dto = new VehicleDTO();
        dto.setPlateNumber("123 TUN 456");

        when(vehicleRepository.findAll()).thenReturn(List.of(vehicle));
        when(vehicleMapper.toDTO(vehicle)).thenReturn(dto);

        List<VehicleDTO> result = vehicleService.getAllVehicles();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getPlateNumber()).isEqualTo("123 TUN 456");
    }

    @Test
    void deleteVehicle_whenVehicleExists_deletesIt() {

        UUID id = UUID.randomUUID();

        Vehicle vehicle = Vehicle.builder().id(id).plateNumber("123 TUN 456").build();

        when(vehicleRepository.findById(id)).thenReturn(Optional.of(vehicle));

        vehicleService.deleteVehicle(id);

        verify(vehicleRepository).delete(vehicle);
    }


    @Test
    void getVehiclesByAdmin_returnsMappedVehicles() {

        UUID adminId = UUID.randomUUID();

        Vehicle vehicle = Vehicle.builder().id(UUID.randomUUID()).plateNumber("123 TUN 456").build();

        VehicleDTO dto = new VehicleDTO();

        when(vehicleRepository.findByAdminId(adminId)).thenReturn(List.of(vehicle));

        when(vehicleMapper.toDTO(vehicle)).thenReturn(dto);

        List<VehicleDTO> result = vehicleService.getVehiclesByAdmin(adminId);

        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isSameAs(dto);
    }

    @Test
    void deleteVehicle_whenVehicleDoesNotExist_throwsException() {

        UUID id = UUID.randomUUID();

        when(vehicleRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> vehicleService.deleteVehicle(id)).isInstanceOf(RuntimeException.class).hasMessage("Vehicle not found");

        verify(vehicleRepository, never()).delete(any());
    }

    @Test
    void createVehicle_savesVehicle() {

        User user = User.builder().id(UUID.randomUUID()).build();

        SecurityContext context = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);

        when(authentication.getPrincipal()).thenReturn(user);

        SecurityContextHolder.setContext(context);
        when(context.getAuthentication()).thenReturn(authentication);

        CreateVehicleRequest request = new CreateVehicleRequest();
        request.setPlateNumber("123 TUN 456");
        request.setBrand("Mercedes");
        request.setModel("Actros");
        request.setYear(2022);

        Vehicle saved = Vehicle.builder().id(UUID.randomUUID()).plateNumber("123 TUN 456").build();

        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(saved);

        when(vehicleMapper.toDTO(saved)).thenReturn(new VehicleDTO());

        vehicleService.createVehicle(request);

        verify(vehicleRepository).save(any(Vehicle.class));

        SecurityContextHolder.clearContext();
    }


    @Test
    void updateVehicle_updatesVehicle() {

        UUID id = UUID.randomUUID();

        Vehicle vehicle = Vehicle.builder().id(id).plateNumber("OLD").build();

        User admin = User.builder().id(UUID.randomUUID()).build();

        SecurityContext context = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);

        when(context.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(admin);

        SecurityContextHolder.setContext(context);

        UpdateVehicleRequest request = new UpdateVehicleRequest();
        request.setPlateNumber("NEW");
        request.setBrand("Mercedes");
        request.setModel("Actros");
        request.setYear(2025);

        when(vehicleRepository.findById(id)).thenReturn(Optional.of(vehicle));

        when(vehicleRepository.save(vehicle)).thenReturn(vehicle);

        when(vehicleMapper.toDTO(vehicle)).thenReturn(new VehicleDTO());

        vehicleService.updateVehicle(id, request);

        assertThat(vehicle.getPlateNumber()).isEqualTo("NEW");
        assertThat(vehicle.getBrand()).isEqualTo("Mercedes");
        assertThat(vehicle.getModel()).isEqualTo("Actros");
        assertThat(vehicle.getYear()).isEqualTo(2025);

        SecurityContextHolder.clearContext();
    }

    @Test
    void getVehiclesByAdmin_whenEmpty_returnsEmptyList() {

        UUID adminId = UUID.randomUUID();

        when(vehicleRepository.findByAdminId(adminId)).thenReturn(List.of());

        List<VehicleDTO> result = vehicleService.getVehiclesByAdmin(adminId);

        assertThat(result).isEmpty();
    }

}