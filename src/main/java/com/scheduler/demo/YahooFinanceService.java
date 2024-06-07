package com.scheduler.demo;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

@Service
public class YahooFinanceService {

    private final WebClient webClient;

    public YahooFinanceService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://query1.finance.yahoo.com").build();
    }

    public Mono<String> getSp500Data() {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusYears(3);

        long period1 = startDate.atStartOfDay().toEpochSecond(ZoneOffset.UTC);
        long period2 = endDate.atStartOfDay().toEpochSecond(ZoneOffset.UTC);

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v8/finance/chart/^GSPC")
                        .queryParam("period1", period1)
                        .queryParam("period2", period2)
                        .queryParam("interval", "1d")
                        .build())
                .retrieve()
                .bodyToMono(String.class);
    }
}
