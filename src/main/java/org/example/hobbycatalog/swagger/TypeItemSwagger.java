package org.example.hobbycatalog.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Type of hobbies controller",description = "Controller that allow to make CRUD operations with types of hobbies. " +
"Functions below available only for admin")
public interface TypeItemSwagger {

    @Operation(summary = "Method gives a list of all existing types of hobbies")
    @ApiResponse(responseCode = "200", description = "All items are displayed")
    @ApiResponse(responseCode = "403", description = "Unauthorised user or not an admin")
    @ApiResponse(responseCode = "409", description = "Database conflict")
    @ApiResponse(responseCode = "500", description = "Server error")
    public String getAllTypes();

    @Operation(summary = "Method that allows to create new type of hobby")
    @ApiResponse(responseCode = "200", description = "New type of hobby was created")
    @ApiResponse(responseCode = "400", description = "Invalid dto was given")
    @ApiResponse(responseCode = "403", description = "Unauthorised user or not an admin")
    @ApiResponse(responseCode = "409", description = "Database conflict")
    @ApiResponse(responseCode = "500", description = "Server error")
    @Parameter(name = "TypeHobbies.class",description = "Class for type creating")
    public String createNewType();

    @Operation(summary = "Method that allow to update 1 existing type of hobby for given id")
    @ApiResponse(responseCode = "200", description = "Type of hobby was updated")
    @ApiResponse(responseCode = "400", description = "Wrong data for type was given")
    @ApiResponse(responseCode = "403", description = "Unauthorised user or not an admin")
    @ApiResponse(responseCode = "409", description = "Database conflict")
    @ApiResponse(responseCode = "500", description = "Server error")
    @Parameter(name = "TypeHobbiesUpdateDTO",description = "DTO for updating type")
    public String updateType();

    @Operation(summary = "Method that allows to delete 1 existing type of hobby for given id")
    @ApiResponse(responseCode = "200", description = "Type of hobby was deleted successfully")
    @ApiResponse(responseCode = "400", description = "Invalid id given")
    @ApiResponse(responseCode = "403", description = "Unauthorised user or not an admin")
    @ApiResponse(responseCode = "409", description = "Database conflict")
    @ApiResponse(responseCode = "500", description = "Server error")
    @Parameter(name = "Long id",description = "Id of type to delete")
    public String deleteType();
}
