package com.sgz.server.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.UUID;

@Entity
@Table(name = "genres")
public class Genre {

    @Id
    @GeneratedValue(generator = "hibernate-uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @NotBlank(message = "Name can not be blank")
    @Size(min = 5, max = 50, message = "Name must be between 5-50 characters")
    @Column(nullable = false, unique = true)
    private String name;

}
