package com.sgz.server.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "movies")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Movie {

    @Id
    @GeneratedValue(generator = "hibernate-uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @NotBlank(message = "Title must not be blank")
    @Size(min = 5, max = 255, message = "Title must be between 5-255 characters")
    @Column(nullable = false)
    private String title;

    @ManyToOne
    @JoinColumn(name = "genre_id")
    private Genre genre;

    @NotNull(message = "Quantity must not be null")
    @Digits(integer = 5, fraction = 0, message="Must be properly formatted")
    @Column(name = "quantity", nullable = false)
    private long qty;

    @NotNull(message = "Daily rate must not be null")
    @DecimalMin(value = "00.00", message="Must be in proper range")
    @DecimalMax(value = "99.99", message="Must be in proper range")
    @Digits(integer = 2, fraction = 2, message="Must be properly formatted")
    @Column(nullable = false)
    private BigDecimal dailyRate;
}
