package com.mlc.exchange.rate.checker;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ExchangeRateManager {
    private static Logger logger = LoggerFactory.getLogger(ExchangeRateManager.class);

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
        logger.debug("Executing scheduler");
        Map<String, Object> rate = exchangeRateService.exchangeRate(from, to);

        ExchangeRate exchanteRate = new ExchangeRate();
        exchanteRate.setFrom(from);
        exchanteRate.setTo(to);
        exchanteRate.setRate(Double.valueOf(rate.get("rate").toString()));
        exchanteRate.setDate(rate.get("date").toString());
        exchanteRate.setLastDateCheck(new Date());

        repository.save(exchanteRate);
    }

    public Map<String, Object> getLatestExchangeRate() {
        ExchangeRate latestRate = repository.findFirstByOrderByLastDateCheckDesc();

        Map<String, Object> rates = new HashMap<String, Object>();
        rates.put("date", latestRate.getDate());
        rates.put("rate", latestRate.getRate());

        Map<String, Object> fromTo = new HashMap<String, Object>();
        fromTo.put("from", latestRate.getFrom());
        fromTo.put("to", latestRate.getTo());
        fromTo.put("rates", rates);

        return fromTo;
    }

    public Map<String, Object> getExchangeRate(String startDate, String endDate) {
        List<Map<String, Object>> resultRates = new ArrayList<Map<String, Object>>();

        List<ExchangeRate> rates = repository.findByDateBetween(startDate, endDate);

        for (ExchangeRate rate : rates) {
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("date", rate.getDate());
            result.put("rate", rate.getRate());
            resultRates.add(result);
        }

        Map<String, Object> fromTo = new HashMap<String, Object>();
        fromTo.put("from", rates.get(0).getFrom());
        fromTo.put("to", rates.get(0).getTo());
        fromTo.put("rates", resultRates);

        return fromTo;
    }

}
