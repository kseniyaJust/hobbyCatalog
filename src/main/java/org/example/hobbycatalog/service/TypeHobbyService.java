package org.example.hobbycatalog.service;

import lombok.extern.slf4j.Slf4j;
import org.example.hobbycatalog.DTO.TypeHobbiesDTO;
import org.example.hobbycatalog.DTO.UpdateTypeHobbyDTO;
import org.example.hobbycatalog.entity.TypeHobbies;
import org.example.hobbycatalog.mapper.TypeHobbiesMapper;
import org.example.hobbycatalog.repository.TypeHobbyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TypeHobbyService {
    private TypeHobbyRepository typeHobbiesRepository;

    private TypeHobbiesMapper typeHobbiesMapper;

    public TypeHobbyService(TypeHobbyRepository typeHobbiesRepository, TypeHobbiesMapper typeHobbiesMapper) {
        this.typeHobbiesRepository = typeHobbiesRepository;
        this.typeHobbiesMapper = typeHobbiesMapper;
    }

    @Transactional
    public TypeHobbiesDTO createTypeHobby(TypeHobbiesDTO typeHobbiesDTO) {

        if (typeHobbiesRepository.existsByTypeName(typeHobbiesDTO.getTypeName())) {
            throw new RuntimeException("Type hobby with name '" + typeHobbiesDTO.getTypeName() + "' already exists");
        }

        TypeHobbies typeHobbies = typeHobbiesMapper.toEntity(typeHobbiesDTO);

        TypeHobbies savedType = typeHobbiesRepository.save(typeHobbies);

        return typeHobbiesMapper.toDTO(savedType);
    }

    @Transactional(readOnly = true)
    public List<TypeHobbiesDTO> getAllTypeHobbies() {
        return typeHobbiesRepository.findAll()
                .stream()
                .map(typeHobbiesMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TypeHobbiesDTO getTypeHobbyById(Long id) {
        TypeHobbies typeHobbies = typeHobbiesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Type hobby not found with id: " + id));
        return typeHobbiesMapper.toDTO(typeHobbies);
    }

    @Transactional
    public TypeHobbiesDTO updateTypeHobby(Long id, UpdateTypeHobbyDTO typeHobbiesDTO) {
        TypeHobbies existingType = typeHobbiesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Type hobby not found with id: " + id));

        if(typeHobbiesDTO.getTypeName() != null){
            existingType.setTypeName(typeHobbiesDTO.getTypeName());
        }
        if(typeHobbiesDTO.getCountPlayers() >= 2)
            existingType.setCountPlayers(typeHobbiesDTO.getCountPlayers());
        if(typeHobbiesDTO.getSummary() != null)
            existingType.setSummary(typeHobbiesDTO.getSummary());

        TypeHobbies updatedType = typeHobbiesRepository.save(existingType);
        return typeHobbiesMapper.toDTO(updatedType);
    }

    @Transactional
    public void deleteTypeHobby(Long id) {
        if (!typeHobbiesRepository.existsById(id)) {
            throw new RuntimeException("Type hobby not found with id: " + id);
        }
        typeHobbiesRepository.deleteById(id);
    }
}
