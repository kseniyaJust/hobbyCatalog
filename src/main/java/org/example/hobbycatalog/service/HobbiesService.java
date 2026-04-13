package org.example.hobbycatalog.service;

import org.example.hobbycatalog.DTO.HobbyDTO;
import org.example.hobbycatalog.DTO.PagedHobbiesResponseDTO;
import org.example.hobbycatalog.DTO.UpdateHobbyDTO;
import org.example.hobbycatalog.entity.Hobbies;
import org.example.hobbycatalog.entity.TypeHobbies;
import org.example.hobbycatalog.exceptions.ItemNotFoundException;
import org.example.hobbycatalog.mapper.HobbiesMapper;
import org.example.hobbycatalog.repository.HobbiesRepository;
import org.example.hobbycatalog.repository.TypeHobbyRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HobbiesService {

    private final HobbiesRepository hobbiesRepository;
    private final HobbiesMapper hobbiesMapper;
    private final TypeHobbyRepository typeHobbiesRepository;

    public HobbiesService(HobbiesRepository hobbiesRepository,
                          HobbiesMapper hobbiesMapper,
                          TypeHobbyRepository typeHobbiesRepository) {
        this.hobbiesRepository = hobbiesRepository;
        this.hobbiesMapper = hobbiesMapper;
        this.typeHobbiesRepository = typeHobbiesRepository;
    }

    @Transactional
    public HobbyDTO createNewHobby(HobbyDTO hobbyDTO) {
        TypeHobbies typeHobbies = typeHobbiesRepository.findByTypeName(hobbyDTO.getTypeName())
                .orElseThrow(() -> new RuntimeException(
                        "Type hobby not found with name: " + hobbyDTO.getTypeName()
                ));

        Hobbies hobbies = hobbiesMapper.toEntity(hobbyDTO);
        hobbies.setTypeAndHobbies(typeHobbies);
        Hobbies savedHobby = hobbiesRepository.save(hobbies);

        return hobbiesMapper.toDTO(savedHobby);
    }

    @Transactional
    public HobbyDTO updateNewHobby(Long id, UpdateHobbyDTO updateHobbyDTO) {
        Hobbies existingHobby = hobbiesRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Hobby with id: " + id + " not found"));

        if (updateHobbyDTO.getName() != null) {
            existingHobby.setName(updateHobbyDTO.getName());
        }
        if (updateHobbyDTO.getCreator() != null) {
            existingHobby.setCreator(updateHobbyDTO.getCreator());
        }
        if (updateHobbyDTO.getPrice() > 0) {
            existingHobby.setPrice(updateHobbyDTO.getPrice());
        }
        if (updateHobbyDTO.getNameType() != null) {
            TypeHobbies newType = typeHobbiesRepository.findByTypeName(updateHobbyDTO.getNameType())
                    .orElseThrow(() -> new RuntimeException(
                            "Type hobby not found with name: " + updateHobbyDTO.getNameType()
                    ));
            existingHobby.setTypeAndHobbies(newType);
        }

        Hobbies updatedHobby = hobbiesRepository.save(existingHobby);
        return hobbiesMapper.toDTO(updatedHobby);
    }

    public Hobbies getHobbyById(Long id) {
        return hobbiesRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Hobby with id: " + id + " not found"));
    }

    @Transactional(readOnly = true)
    public PagedHobbiesResponseDTO searchHobbies(
            String name,
            Double minPrice,
            Double maxPrice,
            String typeName,
            Integer page,
            Integer size,
            String sortBy,
            String sortDirection
    ) {
        int currentPage = (page != null && page >= 0) ? page : 0;
        int pageSize = (size != null && size > 0) ? size : 10;
        String sortField = (sortBy != null && !sortBy.isEmpty()) ? sortBy : "idHobby";
        Sort.Direction direction = (sortDirection != null && sortDirection.equalsIgnoreCase("desc"))
                ? Sort.Direction.DESC : Sort.Direction.ASC;

        Sort sort = Sort.by(direction, sortField);
        Pageable pageable = PageRequest.of(currentPage, pageSize, sort);

        Page<Hobbies> hobbiesPage = hobbiesRepository.searchHobbies(name, minPrice, maxPrice, typeName, pageable);

        List<HobbyDTO> hobbyDTOs = hobbiesPage.getContent()
                .stream()
                .map(hobbiesMapper::toDTO)
                .collect(Collectors.toList());

        return new PagedHobbiesResponseDTO(
                hobbyDTOs,
                hobbiesPage.getNumber(),
                hobbiesPage.getSize(),
                hobbiesPage.getTotalElements(),
                hobbiesPage.getTotalPages(),
                hobbiesPage.isFirst(),
                hobbiesPage.isLast(),
                hobbiesPage.isEmpty(),
                hobbiesPage.getNumberOfElements()
        );
    }

    public String deleteHobbyById(Long id) {
        if (hobbiesRepository.existsById(id)) {
            hobbiesRepository.deleteById(id);
            return "Hobby with id: " + id + " deleted";
        }
        throw new ItemNotFoundException("Hobby with id: " + id + " not found");
    }

}