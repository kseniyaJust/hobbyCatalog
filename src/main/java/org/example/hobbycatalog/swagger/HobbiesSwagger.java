package org.example.hobbycatalog.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.hobbycatalog.DTO.UpdateHobbyDTO;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Hobbies controller",description = "Controller that holds all information about different types of hobby and their details (price, amount of players, etc.)")
public interface HobbiesSwagger {

    @Operation(summary = "Method gives list of all present hobbies. Filter is available")
    @ApiResponse(responseCode = "200", description = "All items are displayed successfully")
    @ApiResponse(responseCode = "409", description = "Database conflict")
    @ApiResponse(responseCode = "500", description = "Server error")
    @Parameters({
            @Parameter(name = "type", description = "Type of workout", example = "RUNNING"),
            @Parameter(name = "sortBy", description = "Field data will be sorted by", example = "price"),
            @Parameter(name = "sortDir", description = " Sort direction (ASC/DESC)", example = "DESC"),
            @Parameter(name = "page", description = "Number of current page", example = "0"),
            @Parameter(name = "size", description = "Size of page", example = "10")
    })
    public String getAllCatalog();

    @Operation(summary = "Method gives 1 hobby for given id")
    @ApiResponse(responseCode = "200", description = "Hobby was found")
    @ApiResponse(responseCode = "400", description = "Invalid id given")
    @ApiResponse(responseCode = "409", description = "Database conflict")
    @ApiResponse(responseCode = "500", description = "Server error")
    @Parameter(name = "Long id",description = "Id of hobby to find")
    public String getOneHobby(@PathVariable Long id);

    @Operation(summary = "Method that allows to create new hobby (only for admin)")
    @ApiResponse(responseCode = "200", description = "New hobby created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid dto data given")
    @ApiResponse(responseCode = "403", description = "Access denied (unauthorised or not an admin)")
    @ApiResponse(responseCode = "409", description = "Database conflict")
    @ApiResponse(responseCode = "500", description = "Server error")
    @Parameter(name = "Hobbies.class",description = "Entity class for creating")
    public String createHobby();

    @Operation(summary = "Method that allows to update 1 existing hobby for given id (only for admin)")
    @ApiResponse(responseCode = "200", description = "Hobby was changed successfully")
    @ApiResponse(responseCode = "400", description = "Invalid id or dto given")
    @ApiResponse(responseCode = "403", description = "Access denied (unauthorised or not an admin)")
    @ApiResponse(responseCode = "409", description = "Database conflict")
    @ApiResponse(responseCode = "500", description = "Server error")
    @Parameter(name = "Long id",description = "Id of hobby to update")
    public String changeHobby(@PathVariable Long id, @RequestBody @Valid UpdateHobbyDTO updateHobbyDTO);

    @Operation(summary = "Method that allows to delete existing hobby for given id(only for admin)")
    @ApiResponse(responseCode = "200", description = "Hobby was deleted successfully")
    @ApiResponse(responseCode = "400", description = "Wrong id given")
    @ApiResponse(responseCode = "403", description = "Access denied (unauthorised or not an admin)")
    @ApiResponse(responseCode = "409", description = "Database conflict")
    @ApiResponse(responseCode = "500", description = "Server error")
    @Parameter(name = "Long id",description = "Id of hobby to delete")
    public String deleteHobby(@PathVariable Long id);
}
