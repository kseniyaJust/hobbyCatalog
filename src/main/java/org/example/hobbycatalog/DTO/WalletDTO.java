package org.example.hobbycatalog.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.sql.Date;

@Data
public class WalletDTO {

        @NotBlank(message = "Owner name is required")
        @Size(min = 2, max = 100, message = "Owner name must be between 2 and 100 characters")
        private String owner_name;

        @NotNull(message = "Card number is required")
        @Digits(integer = 16, fraction = 0, message = "Card number must be 16 digits")
        private Long cart_number;

        @NotNull(message = "Expiration date is required")
        @JsonFormat(pattern = "MM/yyyy")
        private Date date_expire;

        @NotNull(message = "CVC is required")
        @Digits(integer = 3, fraction = 0, message = "CVC must be 3 digits")
        private Long CVC;

}
