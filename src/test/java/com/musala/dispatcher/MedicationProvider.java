package com.musala.dispatcher;

import com.musala.dispatcher.entity.Drone;
import com.musala.dispatcher.entity.Medication;
import com.musala.dispatcher.entity.Model;

import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Optional.ofNullable;

public class MedicationProvider {

    public static Stream<Medication> provideMedications() {
        return Stream.of(new Medication())
                .flatMap(medication -> Stream.concat(
                        Stream.concat(Stream.of(null, "_", "-"), characterRange('a', 'z')),
                                Stream.concat(characterRange('A', 'Z'), characterRange('0', '9')))
                        .map(name -> medication.setName(name)))
                .flatMap(medication -> Stream.of(1, 250, 500)
                        .map(weight -> medication.setWeight(weight)))
                .flatMap(medication -> Stream.of(null, "http://com.musala", "file://com.musala")
                        .map(image -> medication.setImage(image)))
                .map(medication -> medication.setCode(
                        ofNullable(medication.getName())
                                .orElse("NULL")
                                .toUpperCase()
                                .replace("-", "__")
                        + ofNullable(medication.getImage())
                                .orElse("NULL")
                                .substring(0, 4)
                                .toUpperCase()));
    }

    private static Stream<String> characterRange(char fst, char snd) {
        return IntStream.range(fst, snd)
                .mapToObj(Character::toString);
    }
}
