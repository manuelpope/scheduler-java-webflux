package com.scheduler.demo;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@CrossOrigin
public class PokeApiController {

    private final PokeApiService pokeApiService;

    public PokeApiController(PokeApiService pokeApiService) {
        this.pokeApiService = pokeApiService;
    }

    @GetMapping("/pokemon/{id}")
    public Mono<String> getPokemonById(@PathVariable int id) {
        return pokeApiService.getPokemonById(id);
    }

    @GetMapping("/pokemons")
    public Flux<JsonNode> getAllPokemons(@RequestParam(defaultValue = "1000") int limit) {
        return pokeApiService.getAllPokemons(limit).log();
    }
}
