package com.scheduler.demo;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@CrossOrigin
public class YahooFinanceController {

    private final YahooFinanceService yahooFinanceService;

    public YahooFinanceController(YahooFinanceService yahooFinanceService) {
        this.yahooFinanceService = yahooFinanceService;
    }

    @GetMapping("/sp500")
    public Mono<String> getSp500Data() {
        return yahooFinanceService.getSp500Data().log();
    }
}
