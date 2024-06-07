package com.scheduler.demo;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

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
    public Poke randomPokemon(){
        return this.poke;
    }

    @Scheduled(fixedRate = 5000) // Ejecutar cada minuto (ajusta el intervalo según sea necesario)
    public void updateDollarPrice() {
        Random random = new Random();
        int id = random.nextInt(1000) + 1;
        Mono<JsonNode> priceMono = webClient.get()
                .uri("/pokemon/{id}", String.valueOf(id))
                .retrieve()
                .bodyToMono(JsonNode.class)
                .doOnError(err -> log.error(err.getLocalizedMessage()))
                ;
        log.info(" new poke ::: "+id);

        priceMono.subscribeOn(Schedulers.boundedElastic())
                .subscribe(price -> poke.setValue(price));
    }

    @Bean
    public JsonNode randomPokesito(Poke randomPokemon){
        log.info(randomPokemon.toString());
        return randomPokemon.getValue();
    }
}
