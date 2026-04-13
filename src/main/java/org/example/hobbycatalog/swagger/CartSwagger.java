package org.example.hobbycatalog.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;


@Tag(name="Cart controller",description = "Controller that holds data about id user and items that he has")
public interface CartSwagger {

    @Operation(summary = "Method gets all items that current user has")
    @ApiResponse(responseCode = "200", description = "All hobbies in cart for current user was displayed successfully")
    @ApiResponse(responseCode = "403", description = "Unauthorised user")
    @ApiResponse(responseCode = "409", description = "Database conflict")
    @ApiResponse(responseCode = "500", description = "Server error")
    public String getCart();

    @Operation(summary = "Method adds new item for current user")
    @ApiResponse(responseCode = "200", description = "Hobby was successfully added to cart for current user")
    @ApiResponse(responseCode = "400", description = "Wrong id given")
    @ApiResponse(responseCode = "403", description = "Unauthorised user")
    @ApiResponse(responseCode = "409", description = "Database conflict")
    @ApiResponse(responseCode = "500", description = "Server error")
    @Parameter(name = "Long id",description = "Id of hobby to add to cart")
    public String addItemToCart();

    @Operation(summary = "Method decreases balance and delete item for given id from table if purchase succed")
    @ApiResponse(responseCode = "200", description = "Hobby was buyed successfully")
    @ApiResponse(responseCode = "400", description = "No hobby with given id found in cart")
    @ApiResponse(responseCode = "403", description = "Unauthorised user")
    @ApiResponse(responseCode = "409", description = "Database conflict")
    @ApiResponse(responseCode = "500", description = "Server error")
    @Parameter(name = "Long id",description = "id to buy(delete) hobby from cart")
    public String buyItemFromCart();

    @Operation(summary = "Method deletes item from table for given id")
    @ApiResponse(responseCode = "200", description = "Hobby was successfully deleted from cart")
    @ApiResponse(responseCode = "400", description = "No hobby with given id found in cart")
    @ApiResponse(responseCode = "403", description = "Unauthorised user")
    @ApiResponse(responseCode = "409", description = "Database conflict")
    @ApiResponse(responseCode = "500", description = "Server error")
    @Parameter(name = "Long id",description = "id to delete hobby from cart")
    public String deleteItemFromCart();
}
