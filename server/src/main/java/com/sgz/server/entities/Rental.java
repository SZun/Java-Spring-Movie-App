package com.sgz.server.entities;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "movies")
public class Rental {

    @Id
    @GeneratedValue(generator = "hibernate-uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    private Movie movie;

    private Customer customer;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    private LocalDate rentalDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    private LocalDate returnDate;

    @DecimalMin(value = "00.00", message="Must be in proper range")
    @DecimalMax(value = "999.99", message="Must be in proper range")
    @Digits(integer = 3, fraction = 2, message="Must be properly formatted")
    @Column(nullable = false)
    private BigDecimal fee;

}
