package com.musala.dispatcher;

import com.musala.dispatcher.entity.Drone;
import com.musala.dispatcher.entity.Model;
import com.musala.dispatcher.entity.State;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DroneProvider {

    public static Stream<Drone> provideDefaultDrones() {
        return Stream.of(new Drone())
                .flatMap(drone -> Arrays.stream(Model.values())
                        .map(model -> drone.setModel(model)))
                .flatMap(drone -> Stream.of(0, 500)
                        .map(weightLimit -> drone.setWeightLimit(weightLimit)))
                .map(drone -> drone.setSerialNumber(
                        drone.getWeightLimit() + drone.getModel().getCode()));
    }

    public static Stream<Drone> provideDrones() {
        return provideDefaultDrones()
                .flatMap(drone -> Stream.of(0, 25, 100)
                        .map(batteryCapacity -> drone.setBatteryCapacity(batteryCapacity)))
                .flatMap(drone -> Arrays.stream(State.values())
                        .map(state -> drone.setState(state)))
                .map(drone -> drone.setSerialNumber(
                        drone.getWeightLimit()
                                + drone.getModel().getCode()
                                + drone.getState().ordinal()
                                + drone.getBatteryCapacity()));
    }

    public static Stream<List<Drone>> provideFilteringDrones() {
        return provideDrones()
                .collect(Collectors.groupingBy(Drone::getModel,
                        Collectors.groupingBy(Drone::getWeightLimit)))
                .values()
                .stream()
                .map(Map::values)
                .flatMap(Collection::stream);
    }
}
