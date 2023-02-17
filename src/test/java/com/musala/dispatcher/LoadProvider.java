package com.musala.dispatcher;

import com.musala.dispatcher.entity.Drone;
import com.musala.dispatcher.entity.Medication;
import com.musala.dispatcher.entity.Model;
import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

public class LoadProvider {

    public static Stream<Arguments> provideLoads() {
        return provideReducedDrones()
                .peek(drone -> drone.setWeightLimit(250))
                .flatMap(drone -> provideReducedMedications()
                        .filter(medication -> medication.getWeight() <= drone.getWeightLimit())
                        .map(medication -> Arguments.of(drone, medication)));
    }

    public static Stream<Arguments> provideMultipleLoads() {
        return provideReducedDrones()
                .flatMap(drone -> provideReducedMedications()
                        .filter(medication -> 2 * medication.getWeight() <= drone.getWeightLimit())
                        .map(medication -> Arguments.of(drone, medication,
                                drone.getWeightLimit() / medication.getWeight())));
    }

    public static Stream<Arguments> provideWrongLoads() {
        return DroneProvider.provideDrones()
                .filter(drone -> drone.getModel().equals(Model.Lightweight))
                .flatMap(drone -> provideReducedMedications()
                        .filter(medication -> drone.getWeightLimit() < medication.getWeight())
                        .map(medication -> Arguments.of(drone, medication)));
    }

    private static Stream<Drone> provideReducedDrones() {
        return DroneProvider.provideDrones()
                .filter(drone -> drone.getWeightLimit() != 0)
                .filter(drone -> drone.getModel().equals(Model.Heavyweight));
    }

    private static Stream<Medication> provideReducedMedications() {
        return MedicationProvider.provideMedications()
                .filter(medication -> medication.getName() == null)
                .filter(medication -> medication.getImage() == null);
    }
}
