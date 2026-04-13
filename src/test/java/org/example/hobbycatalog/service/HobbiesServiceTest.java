package org.example.hobbycatalog.service;

import org.example.hobbycatalog.DTO.HobbyDTO;
import org.example.hobbycatalog.DTO.UpdateHobbyDTO;
import org.example.hobbycatalog.entity.Hobbies;
import org.example.hobbycatalog.entity.TypeHobbies;
import org.example.hobbycatalog.mapper.HobbiesMapper;
import org.example.hobbycatalog.repository.HobbiesRepository;
import org.example.hobbycatalog.repository.TypeHobbyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HobbiesServiceTest {

    @Mock
    private HobbiesRepository hobbiesRepository;

    @Mock
    private HobbiesMapper hobbiesMapper;

    @Mock
    private TypeHobbyRepository typeHobbiesRepository;

    @InjectMocks
    private HobbiesService hobbiesService;

    private Hobbies testHobby;
    private HobbyDTO testHobbyDTO;
    private TypeHobbies testType;

    @BeforeEach
    void setUp() {
        testType = new TypeHobbies();
        testType.setIdType(1L);
        testType.setTypeName("Board Game");

        testHobby = new Hobbies();
        testHobby.setIdHobby(1L);
        testHobby.setName("Test Hobby");
        testHobby.setPrice(100);
        testHobby.setCreator("Test Creator");
        testHobby.setTypeAndHobbies(testType);

        testHobbyDTO = new HobbyDTO();
        testHobbyDTO.setName("Test Hobby");
        testHobbyDTO.setPrice(100);
        testHobbyDTO.setCreator("Test Creator");
        testHobbyDTO.setTypeName("Board Game");
    }

    @Test
    void createNewHobby_Success() {
        when(typeHobbiesRepository.findByTypeName("Board Game")).thenReturn(Optional.of(testType));
        when(hobbiesMapper.toEntity(testHobbyDTO)).thenReturn(testHobby);
        when(hobbiesRepository.save(any(Hobbies.class))).thenReturn(testHobby);
        when(hobbiesMapper.toDTO(testHobby)).thenReturn(testHobbyDTO);

        HobbyDTO result = hobbiesService.createNewHobby(testHobbyDTO);

        assertNotNull(result);
        assertEquals("Test Hobby", result.getName());
        verify(hobbiesRepository, times(1)).save(any(Hobbies.class));
    }

    @Test
    void createNewHobby_TypeNotFound_ThrowsException() {
        when(typeHobbiesRepository.findByTypeName("Board Game")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> hobbiesService.createNewHobby(testHobbyDTO));
        verify(hobbiesRepository, never()).save(any(Hobbies.class));
    }

    @Test
    void getHobbyById_Success() {
        when(hobbiesRepository.findById(1L)).thenReturn(Optional.of(testHobby));

        Hobbies result = hobbiesService.getHobbyById(1L);

        assertNotNull(result);
        assertEquals("Test Hobby", result.getName());
    }

    @Test
    void getHobbyPagedResponse_Success() {
        when(hobbiesRepository.findAll()).thenReturn(Arrays.asList(testHobby));
        when(hobbiesMapper.toDTO(testHobby)).thenReturn(testHobbyDTO);

        List<HobbyDTO> result = hobbiesService.getHobbyPagedResponse();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void deleteHobbyById_Success() {
        when(hobbiesRepository.findById(1L)).thenReturn(Optional.of(testHobby));
        doNothing().when(hobbiesRepository).deleteById(1L);

        String result = hobbiesService.deleteHobbyById(1L);

        assertEquals("Hobby with id: 1 deleted", result);
        verify(hobbiesRepository, times(1)).deleteById(1L);
    }
}