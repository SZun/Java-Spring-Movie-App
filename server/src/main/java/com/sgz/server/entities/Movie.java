package com.sgz.server.entities;

import com.sgz.server.models.MovieVM;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
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

    @Column(nullable = false)
    private String title;

    @ManyToOne
    @JoinColumn(name = "genre_id")
    private Genre genre;

    @Column(name = "quantity", nullable = false)
    private long qty;

    @Column(nullable = false)
    private BigDecimal dailyRate;

    public Movie(MovieVM that, Genre genre) {
        this.id = that.getId();
        this.title = that.getTitle();
        this.genre = genre;
        this.qty = that.getQty();
        this.dailyRate = that.getDailyRate();
    }
}
