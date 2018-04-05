package com.mlc.exchange.rate.checker;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/exchange/rate")
public class ExchangeRateController {

    @Autowired
    private ExchangeRateRepository repository;

    @Autowired
    private ExchangeRateManager exchangeRateManager;

    @GetMapping(produces = "application/json")
    public Map<String, Object> getLatest() {
        return exchangeRateManager.getLatestExchangeRate();
    }

    @GetMapping(path = { "/all", "/all/" }, produces = "application/json")
    public Iterable<ExchangeRate> getAll() {
        return repository.findAll();
    }

    @GetMapping(path = { "/{startDate}/{endDate}", "/{startDate}/{endDate}/" }, produces = "application/json")
    public Map<String, Object> getByPeriod(@PathVariable("startDate") String startDate, @PathVariable("endDate") String endDate) {

        return exchangeRateManager.getExchangeRate(startDate, endDate);

    }

}
