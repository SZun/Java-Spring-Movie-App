package com.sgz.server.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Entity
@Table(name = "movies")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Rental {

    @Id
    @GeneratedValue(generator = "hibernate-uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    private LocalDate rentalDate;

    private LocalDate returnDate;

    @Column(nullable = false)
    private BigDecimal fee;

    public BigDecimal getFee(){
        long daysRented = ChronoUnit.DAYS.between(rentalDate, returnDate);

        BigDecimal movieRate = movie.getDailyRate();

        return movieRate.multiply(new BigDecimal(String.valueOf(daysRented)));
    }

}
