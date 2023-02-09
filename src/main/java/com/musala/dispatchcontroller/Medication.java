package com.musala.dispatchcontroller;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.URL;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "medications")
public class Medication {

    @Pattern(regexp = "[a-zA-Z_\\-]*")
    @Column(name = "name")
    private String name;

    @Min(1)
    @Column(name = "weight", nullable = false)
    private Integer weight;

    @Id
    @Pattern(regexp = "[A-Z_0-9]+")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String code;

    @URL
    @Column(name = "image")
    private String image;
}
