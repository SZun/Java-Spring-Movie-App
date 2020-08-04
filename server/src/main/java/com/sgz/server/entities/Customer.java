package com.sgz.server.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.UUID;

@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(generator = "hibernate-uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @NotBlank(message = "Name can not be blank")
    @Size(min = 5, max = 50, message = "Name must be between 5-50 characters")
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private boolean Gold;

    @NotBlank(message = "Phone number can not be blank")
    @Pattern(regexp = "\"\\\\d{10}\"", message = "Phone number must be 10 digits")
    @Column(nullable = false)
    private String phone;

}
