package org.example.hobbycatalog.controller;

import jakarta.validation.Valid;
import org.example.hobbycatalog.DTO.HobbyDTO;
import org.example.hobbycatalog.DTO.PagedHobbiesResponseDTO;
import org.example.hobbycatalog.DTO.UpdateHobbyDTO;
import org.example.hobbycatalog.entity.Hobbies;
import org.example.hobbycatalog.service.HobbiesService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hobbies/catalog")
public class HobbiesController {

    private final HobbiesService hobbiesService;

    public HobbiesController(HobbiesService hobbiesService) {
        this.hobbiesService = hobbiesService;
    }

    @GetMapping
    public PagedHobbiesResponseDTO getHobbies(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) String typeName,

            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,

            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortDirection
    ) {
        return hobbiesService.searchHobbies(name, minPrice, maxPrice, typeName,
                page, size, sortBy, sortDirection);
    }

    @GetMapping("/{id}")
    public Hobbies getOneHobby(@PathVariable Long id) {
        return hobbiesService.getHobbyById(id);
    }

    @PostMapping
    public HobbyDTO createHobby(@Valid @RequestBody HobbyDTO hobbies) {
        return hobbiesService.createNewHobby(hobbies);
    }

    @PutMapping("/{id}")
    public HobbyDTO changeHobby(@PathVariable Long id, @RequestBody @Valid UpdateHobbyDTO updateHobbyDTO) {
        return hobbiesService.updateNewHobby(id, updateHobbyDTO);
    }

    @DeleteMapping("/{id}")
    public String deleteHobby(@PathVariable Long id) {
        return hobbiesService.deleteHobbyById(id);
    }
}