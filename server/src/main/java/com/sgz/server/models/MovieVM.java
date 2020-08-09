package com.sgz.server.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieVM {

    private UUID id;

    @NotBlank(message = "Title must not be blank")
    @Size(min = 5, max = 255, message = "Title must be between 5-255 characters")
    private String title;

    private UUID genreId;

    @NotNull(message = "Quantity must not be null")
    @Max(value = 99999, message="Must be in proper range")
    private long qty;

    @NotNull(message = "Daily rate must not be null")
    @DecimalMin(value = "00.00", message="Must be in proper range")
    @DecimalMax(value = "99.99", message="Must be in proper range")
    @Digits(integer = 2, fraction = 2, message="Must be properly formatted")
    private BigDecimal dailyRate;

}
