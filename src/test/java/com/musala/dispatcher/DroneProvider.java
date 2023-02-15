package com.musala.dispatcher;

import com.musala.dispatcher.entity.Drone;
import com.musala.dispatcher.entity.Model;
import com.musala.dispatcher.entity.State;
import org.junit.jupiter.params.provider.Arguments;

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

    public static Stream<Drone> provideWrongSerialNumberDrones() {
        return Stream.of(provideDefaultDrones().findFirst().get())
                .flatMap(drone -> Stream.of(null, "", " ", "\t", "0".repeat(101))
                        .map(serialNumber -> drone.setSerialNumber(serialNumber)));
    }

    public static Stream<Drone> provideWrongWeightLimitDrones() {
        return Stream.of(provideDefaultDrones().findFirst().get())
                .flatMap(drone -> Stream.of(null, -1, 501, Integer.MIN_VALUE, Integer.MAX_VALUE)
                        .map(weightLimit -> drone.setWeightLimit(weightLimit)));
    }

    public static Stream<Drone> provideWrongBatteryCapacityDrones() {
        return Stream.of(provideDefaultDrones().findFirst().get())
                .flatMap(drone -> Stream.of(-1, 101, Integer.MIN_VALUE, Integer.MAX_VALUE)
                        .map(batteryCapacity -> drone.setBatteryCapacity(batteryCapacity)));
    }

    public static Stream<Arguments> provideFilteringDronesByState() {
        return provideFilteringDrones()
                .flatMap(list -> Arrays.stream(State.values())
                        .map(state -> Arguments.of(list, state)));
    }

    public static Stream<Arguments> provideFilteringDronesByBatteryCapacity() {
        return provideFilteringDrones()
                .flatMap(list -> Stream.of(0, 25, 100)
                        .map(batteryCapacity -> Arguments.of(list, batteryCapacity)));
    }

    public static Stream<Arguments> provideFilteringDronesByStateAndBatteryCapacity() {
       return provideFilteringDrones()
               .flatMap(list -> Arrays.stream(State.values())
                       .flatMap(state -> Stream.of(0, 25, 100)
                               .map(batteryCapacity -> Arguments.of(list, state, batteryCapacity))));
    }

    private static Stream<List<Drone>> provideFilteringDrones() {
        return copyDrones(provideDrones())
                .collect(Collectors.groupingBy(Drone::getModel,
                        Collectors.groupingBy(Drone::getWeightLimit)))
                .values()
                .stream()
                .map(Map::values)
                .flatMap(Collection::stream);
    }

    private static Stream<Drone> copyDrones(Stream<Drone> stream) {
        return stream.map(drone -> new Drone()
                .setSerialNumber(drone.getSerialNumber())
                .setModel(drone.getModel())
                .setWeightLimit(drone.getWeightLimit())
                .setBatteryCapacity(drone.getBatteryCapacity())
                .setState(drone.getState()));
    }
}
