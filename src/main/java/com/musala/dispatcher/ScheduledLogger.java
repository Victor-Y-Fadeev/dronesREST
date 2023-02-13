package com.musala.dispatcher;

import com.musala.dispatcher.repository.DroneRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ScheduledLogger {

    private static final Logger log = LoggerFactory.getLogger(ScheduledLogger.class);

    private DroneRepository droneRepository;

    @Scheduled(fixedRateString = "${fixedRate.in.milliseconds}")
    public void reportBatteryCapacity() {
        droneRepository.findAll().forEach(drone -> {
            log.info("Drone {} battery capacity is {}%", drone.getSerialNumber(), drone.getBatteryCapacity());
        });
    }
}
