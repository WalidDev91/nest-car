package com.mvpnest.fleetmanagement.service;

import com.mvpnest.fleetmanagement.entity.Mission;
import com.mvpnest.fleetmanagement.exception.ResourceNotFoundException;
import com.mvpnest.fleetmanagement.repository.MissionRepository;
import com.mvpnest.fleetmanagement.service.impl.MissionServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MissionServiceImplTest {

    @Mock
    private MissionRepository missionRepository;

    @InjectMocks
    private MissionServiceImpl missionService;

    @Test
    void deleteMission_whenMissionExists_deletesIt() {

        UUID id = UUID.randomUUID();

        Mission mission = Mission.builder().id(id).title("Tunis Delivery").build();

        when(missionRepository.findById(id)).thenReturn(Optional.of(mission));

        missionService.deleteMission(id);

        verify(missionRepository, times(1)).delete(mission);

    }

    @Test
    void deleteMission_whenMissionDoesNotExist_throwsResourceNotFoundException() {

        UUID id = UUID.randomUUID();

        when(missionRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> missionService.deleteMission(id)).isInstanceOf(ResourceNotFoundException.class).hasMessage("Mission not found");

        verify(missionRepository, never()).delete(any());

    }

}