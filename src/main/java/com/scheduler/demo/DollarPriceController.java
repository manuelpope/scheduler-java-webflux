package com.scheduler.demo;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
@CrossOrigin
public class DollarPriceController {

    @Autowired
    private Poke randomPokemon;

    @GetMapping("/poke")
    public Mono<JsonNode> getDollarPrice() {
        return Mono.fromCallable(()->randomPokemon.getValue()).log();
    }
}
