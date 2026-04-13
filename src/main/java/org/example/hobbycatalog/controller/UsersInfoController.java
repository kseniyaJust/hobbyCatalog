package org.example.hobbycatalog.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.extern.slf4j.Slf4j;
import org.example.hobbycatalog.DTO.AuthRequestDTO;
import org.example.hobbycatalog.DTO.AuthResponseDTO;
import org.example.hobbycatalog.DTO.RegistUserDTO;
import org.example.hobbycatalog.service.SecurityTokenService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users/info")
public class UsersInfoController {

    SecurityTokenService securityTokenService;

    public UsersInfoController(SecurityTokenService securityTokenService) {
        this.securityTokenService = securityTokenService;
    }

    @PostMapping("/regist")
    public String registUser( @Valid @RequestBody RegistUserDTO usersInfoDTO) {
        return securityTokenService.registerUser(usersInfoDTO);
    }

    @PostMapping("/login")
    public AuthResponseDTO loginUser(@Valid @RequestBody AuthRequestDTO authRequestDTO) {

        return securityTokenService.loginUser(authRequestDTO);
    }

    @PostMapping("/refresh")
    public AuthResponseDTO refreshToken(@NotEmpty @RequestBody Map<String, String> refreshToken) {
        return securityTokenService.refreshUser(refreshToken);
    }
}
