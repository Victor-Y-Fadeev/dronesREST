package com.musala.dispatcher.controller;

import com.musala.dispatcher.entity.Drone;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.GreaterThanOrEqual;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.jpa.domain.Specification;

@And({
        @Spec(path = "state", spec = Equal.class),
        @Spec(
                path = "batteryCapacity",
                params = "battery",
                spec = GreaterThanOrEqual.class
        )
})
public interface DroneSpec extends Specification<Drone> {
}
