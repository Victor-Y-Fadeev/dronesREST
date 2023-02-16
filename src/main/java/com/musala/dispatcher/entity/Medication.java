package com.musala.dispatcher.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.constraints.URL;

import java.util.Objects;
import java.util.Set;


@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "medications")
public class Medication {

    @Pattern(regexp = "[a-zA-Z_0-9\\-]+")
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

    @Fetch(FetchMode.JOIN)
    @OneToMany(mappedBy = "medication",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private Set<Load> loads;

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof Medication)) {
            return false;
        }

        return code.equals(((Medication) o).code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

    @Override
    public String toString() {
        try {
            return new ObjectMapper()
                    .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                    .writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return super.toString();
        }
    }
}
