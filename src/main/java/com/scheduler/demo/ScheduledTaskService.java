package com.scheduler.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import java.time.LocalDateTime;

@Configuration
@EnableScheduling
@Slf4j
public class ScheduledTaskService {

    @Scheduled(fixedRate = 10000)
    public void scheduleTask() {
        Mono.fromRunnable(this::performTask)
                .subscribeOn(Schedulers.boundedElastic())
                .doOnError(this::handleError)
                .subscribe();
    }

    private void performTask() {
        try {
           log.info("Tarea programada ejecutada en: " + Thread.currentThread().getName() + " a las " + LocalDateTime.now());
            // Simulando una operación de bloqueo
            Thread.sleep(2000);
            log.info("Tarea completada en: " + Thread.currentThread().getName() + " a las " + LocalDateTime.now());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("La tarea fue interrumpida");
        }
    }

    private void handleError(Throwable throwable) {
        // Manejo del error, por ejemplo, registrar el error
       log.error("Error durante la ejecución de la tarea programada: " + throwable.getMessage());
    }
}
