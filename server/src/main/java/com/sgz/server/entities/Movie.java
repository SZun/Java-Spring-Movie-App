package com.sgz.server.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "movies")
public class Movie {

    @Id
    @GeneratedValue(generator = "hibernate-uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    private Genre genre;

    @Column(name = "quantity", nullable = false)
    private long qty;

    @DecimalMin(value = "00.00", message="Must be in proper range")
    @DecimalMax(value = "99.99", message="Must be in proper range")
    @Digits(integer = 2, fraction = 2, message="Must be properly formatted")
    @Column(nullable = false)
    private BigDecimal dailyRate;
}
