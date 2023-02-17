package com.musala.dispatcher;

import com.musala.dispatcher.entity.Medication;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Optional.ofNullable;

public class MedicationProvider {

    public static Stream<Medication> provideMedications() {
        return Stream.of(new Medication())
                .flatMap(medication -> Stream.concat(
                        Stream.concat(Stream.of(null, "_", "-"), characterRange('a', 'z')),
                                Stream.concat(characterRange('A', 'Z'), characterRange('0', '9')))
                        .map(medication::setName))
                .flatMap(medication -> Stream.of(1, 250, 1000)
                        .map(medication::setWeight))
                .flatMap(medication -> Stream.of(null, "http://musala.com", "file://musala.com")
                        .map(medication::setImage))
                .peek(medication -> medication.setCode(
                        ofNullable(medication.getName())
                                .orElse("NULL")
                                .toUpperCase()
                                .replace("-", "__")
                        + ofNullable(medication.getImage())
                                .orElse("NULL")
                                .substring(0, 4)
                                .toUpperCase()));
    }

    public static Stream<Medication> provideWrongNameMedications() {
        return Stream.of(provideMedications().findFirst().get())
                .flatMap(medication -> Stream.of("", " ", "\t", "\\")
                        .map(medication::setName));
    }

    public static Stream<Medication> provideWrongWeightMedications() {
        return Stream.of(provideMedications().findFirst().get())
                .flatMap(medication -> Stream.of(null, 0, -1, Integer.MIN_VALUE)
                        .map(medication::setWeight));
    }

    public static Stream<Medication> provideWrongCodeMedications() {
        return Stream.of(provideMedications().findFirst().get())
                .flatMap(medication -> Stream.concat(
                        Stream.of(null, "", " ", "\t", "\\", "-"),
                                characterRange('a', 'z'))
                        .map(medication::setCode));
    }

    private static Stream<String> characterRange(char fst, char snd) {
        return IntStream.range(fst, snd)
                .mapToObj(Character::toString);
    }
}
