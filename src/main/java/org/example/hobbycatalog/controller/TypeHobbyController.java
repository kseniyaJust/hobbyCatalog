package org.example.hobbycatalog.controller;

import jakarta.validation.Valid;
import org.example.hobbycatalog.DTO.TypeHobbiesDTO;
import org.example.hobbycatalog.DTO.UpdateTypeHobbyDTO;
import org.example.hobbycatalog.service.TypeHobbyService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/hobbies/types")
public class TypeHobbyController {

    private TypeHobbyService typeHobbiesService;

    public TypeHobbyController(TypeHobbyService typeHobbiesService) {
        this.typeHobbiesService = typeHobbiesService;
    }

    @PostMapping
    public TypeHobbiesDTO createTypeHobby(@Valid @RequestBody TypeHobbiesDTO typeHobbiesDTO) {
        return typeHobbiesService.createTypeHobby(typeHobbiesDTO);
    }

    @GetMapping
    public List<TypeHobbiesDTO> getAllTypeHobbies() {
        return typeHobbiesService.getAllTypeHobbies();
    }

    @GetMapping("/{id}")
    public TypeHobbiesDTO getTypeHobbyById(@PathVariable Long id) {
        return typeHobbiesService.getTypeHobbyById(id);
    }

    @PutMapping("/{id}")
    public TypeHobbiesDTO updateTypeHobby(@PathVariable Long id, @Valid @RequestBody UpdateTypeHobbyDTO typeHobbiesDTO) {
        return typeHobbiesService.updateTypeHobby(id, typeHobbiesDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteTypeHobby(@PathVariable Long id) {
        typeHobbiesService.deleteTypeHobby(id);
    }
}
