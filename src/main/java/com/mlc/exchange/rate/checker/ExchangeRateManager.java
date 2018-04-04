package com.mlc.exchange.rate.checker;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ExchangeRateManager {

    @Autowired
    private MockedExchangeRateService exchangeRateService;

    @Autowired
    private ExchangeRateRepository repository;

    @Value("${exchange.rate.from}")
    private String from;

    @Value("${exchange.rate.to}")
    private String to;

    @Scheduled(cron = "${exchange.rate.interval-check}")
    public void updateExchangeRates() {
        Map<String, Object> rate = exchangeRateService.exchangeRate(from, to);

        ExchangeRate exchanteRate = new ExchangeRate();
        exchanteRate.setFrom(from);
        exchanteRate.setFrom(to);
        exchanteRate.setRate(Double.valueOf(rate.get("rate").toString()));
        exchanteRate.setQuotationDate(rate.get("date").toString());

        repository.save(exchanteRate);
    }

}
