package org.example.hobbycatalog.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name="User controller",description = "Controller that responsible for authorisation/authentification of user")
public interface UserSwagger {

    @Operation(summary = "Method, that register new user")
    @ApiResponse(responseCode = "200", description = "User registered successfully")
    @ApiResponse(responseCode = "400", description = "Invalid data dto given")
    @ApiResponse(responseCode = "409", description = "Database conflict")
    @ApiResponse(responseCode = "500", description = "Server error")
    @Parameter(name = "AuthRequestDTO",description = "DTO for user registration")
    public String registUser();

    @Operation(summary = "Method that log in existing user")
    @ApiResponse(responseCode = "200", description = "User log in successfully")
    @ApiResponse(responseCode = "400", description = "No user found")
    @ApiResponse(responseCode = "409", description = "Database conflict")
    @ApiResponse(responseCode = "500", description = "Server error")
    @Parameter(name = "LoginRequestDTO",description = "DTO for login")
    public String loginUser();

    @Operation(summary = "Method that refresh token to continue current user session")
    @ApiResponse(responseCode = "200", description = "Session was activated successfully")
    @ApiResponse(responseCode = "400", description = "Invalid login dto given")
    @ApiResponse(responseCode = "403", description = "Session is expired")
    @ApiResponse(responseCode = "409", description = "Database conflict")
    @ApiResponse(responseCode = "500", description = "Server error")
    @Parameter(name = "LoginRequestDTO",description = "DTO with login fields for refresh tokens")
    public String refreshToken();
}
