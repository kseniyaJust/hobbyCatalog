package org.example.hobbycatalog.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.hobbycatalog.DTO.UserAddressDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "User Address Controller",description = "Controller that allow to make CRUD operations with address. Only owner of address can make manipulation with it")
public interface UserAdressSwagger {

    @Operation(summary = "Method that gives a list of all existing addresses of current user")
    @ApiResponse(responseCode = "200", description = "All addresses are displayed successfully")
    @ApiResponse(responseCode = "403", description = "Unauthorised user")
    @ApiResponse(responseCode = "409", description = "Database conflict")
    @ApiResponse(responseCode = "500", description = "Server error")
    public String getAllAdresses();

    @Operation(summary = "Method that allow to create new address of current user")
    @ApiResponse(responseCode = "200", description = "New address wa created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid dto was given")
    @ApiResponse(responseCode = "403", description = "Unauthorised user")
    @ApiResponse(responseCode = "409", description = "Database conflict")
    @ApiResponse(responseCode = "500", description = "Server error")
    @Parameter(name = "Adress.class",description = "Class for creating address")
    public String addAdress();

    @Operation(summary = "Method that allow to update 1 address for given id of current user")
    @ApiResponse(responseCode = "200", description = "Address was updated successfully")
    @ApiResponse(responseCode = "400", description = "Invalid dto was given")
    @ApiResponse(responseCode = "403", description = "Unauthorised user")
    @ApiResponse(responseCode = "409", description = "Database conflict")
    @ApiResponse(responseCode = "500", description = "Server error")
    @Parameter(name = "UserAddressUpdateDTO",description = "DTO for updating address")
    public String updateAdress(@RequestBody UserAddressDTO userAdress);

    @Operation(summary = "Method that allow to delte 1 address for given id of current user")
    @ApiResponse(responseCode = "200", description = "Address was deleted successfully")
    @ApiResponse(responseCode = "400", description = "Invalid dto was given")
    @ApiResponse(responseCode = "403", description = "Unauthorised user")
    @ApiResponse(responseCode = "409", description = "Database conflict")
    @ApiResponse(responseCode = "500", description = "Server error")
    @Parameter(name = "Long id",description = "Id of address to delete")
    public String deleteAdress(@RequestParam Long id_adress);
}
