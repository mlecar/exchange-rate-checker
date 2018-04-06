package com.mlc.exchange.rate.checker;

import java.time.LocalDate;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
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
    public Map<String, Object> getByPeriod(@DateTimeFormat(pattern = "yyyyMMdd") @PathVariable("startDate") LocalDate startDate, @DateTimeFormat(pattern = "yyyyMMdd") @PathVariable("endDate") LocalDate endDate) {

        return exchangeRateManager.getExchangeRate(startDate, endDate);

    }

}
