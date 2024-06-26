package com.scheduler.demo;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

@Configuration
@EnableScheduling
@Slf4j
public class ScheduledTask {


    private WebClient webClient;

    @Autowired
    private Poke poke;

    public ScheduledTask(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://pokeapi.co/api/v2").build();
    }

    @Bean
    public Poke randomPokemon() {
        return this.poke;
    }

    @Bean
    @DependsOn("firstValue")
    public String valueName(Poke randomPokemon) {
        if (randomPokemon.getValue() == null) {

            throw new RuntimeException("not valid poke");
        }
        return this.poke.getValue().toString();
    }

    @Scheduled(fixedRate = 5000 ,initialDelay = 2000) // Ejecutar cada minuto (ajusta el intervalo según sea necesario)
    @DependsOn("firstValue")
    public void updateDollarPrice() {
        Random random = new Random();
        int id = random.nextInt(1000) + 1;

        log.info(" new poke ::: " + id);

        getPokemon(id).subscribeOn(Schedulers.boundedElastic())
                .subscribe(price -> poke.setValue(price));
    }

    @Bean
    public String firstValue() throws InterruptedException {
        int flg = 0;

        Random random = new Random();
        int id = random.nextInt(1000) + 1;

        log.info(" new poke ::: " + id);

        getPokemon(id)
                .doOnNext(price -> poke.setValue(price))
                .subscribe();


        while (flg == 0) {


            log.info("not ready");
            Thread.sleep(3000);

            if (poke.getValue() != null) {

                flg += 1;
            }



        }

        return "aloha";
    }

    @Bean
    public JsonNode randomPokesito(Poke randomPokemon) {
        log.info(randomPokemon.toString());
        return randomPokemon.getValue();
    }

    private  Mono<JsonNode> getPokemon(int id){

        return webClient.get()
                .uri("/pokemon/{id}", String.valueOf(id))
                .retrieve()
                .bodyToMono(JsonNode.class)
                .doOnError(err -> log.error(err.getLocalizedMessage()))
                .subscribeOn(Schedulers.single())
                .publishOn(Schedulers.single());
    }
}
