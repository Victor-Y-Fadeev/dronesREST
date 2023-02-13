package com.musala.dispatcher.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.URL;

import java.util.Set;


@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "medications")
public class Medication {

    @Pattern(regexp = "[a-zA-Z_0-9\\-]*")
    private String name;

    @Min(1)
    @Column(updatable = false, nullable = false)
    private Integer weight;

    @Id
    @Pattern(regexp = "[A-Z_0-9]+")
    @Column(updatable = false, nullable = false, unique = true)
    private String code;

    @URL
    private String image;

    @OneToMany(mappedBy = "medication",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private Set<Load> loads;
}
