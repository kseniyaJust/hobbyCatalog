package org.example.hobbycatalog.service;

import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.example.hobbycatalog.DTO.AuthRequestDTO;
import org.example.hobbycatalog.DTO.AuthResponseDTO;
import org.example.hobbycatalog.DTO.RegistUserDTO;
import org.example.hobbycatalog.entity.UsersInfo;
import org.example.hobbycatalog.exceptions.ConflictException;
import org.example.hobbycatalog.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Slf4j
@Service
public class SecurityTokenService {

    private final UsersInfoService  usersInfoService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public SecurityTokenService(UsersInfoService usersInfoService,  JwtService jwtService,AuthenticationManager authenticationManager) {
        this.usersInfoService = usersInfoService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;

    }

    public AuthResponseDTO loginUser(AuthRequestDTO authRequestDTO)
    {
        log.info("Before authenticate");

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequestDTO.getEmail(), authRequestDTO.getPassword())
        );

        UsersInfo userInfo = usersInfoService.getUserByEmail(authRequestDTO.getEmail());
        String accessToken = jwtService.generateAccessJwtToken(userInfo);
        String refreshToken = jwtService.generateRefreshToken(userInfo);

        log.info("Security token passed");
        return new AuthResponseDTO(accessToken, refreshToken);
    }

    public String registerUser(RegistUserDTO requestDTO)
    {
        return usersInfoService.register(requestDTO);
    }

    @Transactional
    public AuthResponseDTO refreshUser(Map<String, String> token)
    {
        String refreshToken = token.get("refreshToken");

        validateRefreshToken(refreshToken);

        String email = jwtService.extractEmail(refreshToken);
        UsersInfo userInfo = usersInfoService.getUserByEmail(email);

        String newAccessToken = jwtService.generateAccessJwtToken(userInfo);

        return new AuthResponseDTO(newAccessToken, refreshToken);

    }
    public void validateRefreshToken(String refreshToken) {

        if (!isValidJwtFormat(refreshToken)) {
            throw new ConflictException("Invalid JWT format");
        }

        if (!jwtService.isTokenValid(refreshToken)) {
            throw new JwtException("Invalid or expired refresh token");
        }
    }

    public boolean isValidJwtFormat(String token) {
        String[] parts = token.split("\\.");
        return parts.length == 3;
    }
}
