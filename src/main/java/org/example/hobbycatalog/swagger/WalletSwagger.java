package org.example.hobbycatalog.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.hobbycatalog.DTO.WalletDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name="Wallet Controller",description = "Controller that responsible for CRUD operations with wallet and money in it"+
"Only owner of wallet can make any operations below")
public interface WalletSwagger {

    @Operation(summary = "Method that gives amount of current balance of current user's wallet")
    @ApiResponse(responseCode = "200", description = "Balance was displayed successfully")
    @ApiResponse(responseCode = "403", description = "Unauthorised user")
    @ApiResponse(responseCode = "409", description = "Database conflict")
    @ApiResponse(responseCode = "500", description = "Server error")
    String getBalance();

    @Operation(summary = "Method that put some given number to wallet of current user's wallet")
    @ApiResponse(responseCode = "200", description = "Money was successfully added to wallet for current user")
    @ApiResponse(responseCode = "400", description = "Wrong amount of money was given")
    @ApiResponse(responseCode = "403", description = "Unauthorised user")
    @ApiResponse(responseCode = "409", description = "Database conflict")
    @ApiResponse(responseCode = "500", description = "Server error")
    @Parameter(name = "Long money",description = "Amount of money to put on balance")
    String putMoney();

    @Operation(summary = "Method that allows to add new wallet and it's info of current user's wallet")
    @ApiResponse(responseCode = "200", description = "Wallet was successfully added")
    @ApiResponse(responseCode = "400", description = "Wrong data for walllet was given")
    @ApiResponse(responseCode = "403", description = "Unauthorised user")
    @ApiResponse(responseCode = "409", description = "Database conflict")
    @ApiResponse(responseCode = "500", description = "Server error")
    @Parameter(name = "Wallet.class",description = "Entity of wallet for creating")
    String addWallet();

    @Operation(summary = "Method that allows to update info in wallet of current user's wallet")
    @ApiResponse(responseCode = "200", description = "Wallet data was updated successfully")
    @ApiResponse(responseCode = "400", description = "Wrong dto of updating wallet was given")
    @ApiResponse(responseCode = "403", description = "Unauthorised user")
    @ApiResponse(responseCode = "409", description = "Database conflict")
    @ApiResponse(responseCode = "500", description = "Server error")
    @Parameter(name = "WalletDTO",description = "DTO for updating wallet")
    String updateWallet(@RequestBody WalletDTO wallet);

    @Operation(summary = "Method that allow to delete wallet for given id of current user's wallet")
    @ApiResponse(responseCode = "200", description = "Wallet was deleted successfully")
    @ApiResponse(responseCode = "400", description = "No wallet with given id was found for current user")
    @ApiResponse(responseCode = "403", description = "Unauthorised user")
    @ApiResponse(responseCode = "409", description = "Database conflict")
    @ApiResponse(responseCode = "500", description = "Server error")
    @Parameter(name = "Long id",description = "Id of wallet to delete")
    String deleteWallet(@RequestParam Long id_wallet);
}
