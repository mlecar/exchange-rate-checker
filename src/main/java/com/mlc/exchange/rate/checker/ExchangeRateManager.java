package com.mlc.exchange.rate.checker;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.stream.JsonReader;

@Component
public class ExchangeRateManager {
    private static Logger logger = LoggerFactory.getLogger(ExchangeRateManager.class);

    @Autowired
    private ExchangeRateService exchangeRateService;

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

        Map<String, Object> fromTo = new HashMap<String, Object>();

        if (latestRate != null) {
            Map<String, Object> rates = new HashMap<String, Object>();
            rates.put("date", latestRate.getDate());
            rates.put("rate", latestRate.getRate());

            fromTo.put("from", latestRate.getFrom());
            fromTo.put("to", latestRate.getTo());
            fromTo.put("rates", rates);
        }

        return fromTo;
    }

    public Map<String, Object> getExchangeRate(LocalDate startDate, LocalDate endDate) {
        List<Map<String, Object>> resultRates = new ArrayList<Map<String, Object>>();

        List<ExchangeRate> rates = repository.findByDateBetween(startDate.toString(), endDate.toString());

        Map<String, Object> fromTo = new HashMap<String, Object>();
        fromTo.put("from", from);
        fromTo.put("to", to);
        fromTo.put("rates", resultRates);

        for (ExchangeRate rate : rates) {
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("date", rate.getDate());
            result.put("rate", rate.getRate());
            resultRates.add(result);
        }
        return fromTo;
    }

    public void loadHistoricalRates(Resource resource) throws FileNotFoundException, IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(resource.getInputStream()));

        JsonElement historical = new Gson().fromJson(reader, JsonElement.class);

        Set<Map.Entry<String, JsonElement>> rates = historical.getAsJsonObject().get("rates").getAsJsonObject().entrySet();

        for (Map.Entry<String, JsonElement> e : rates) {
            ExchangeRate rate = new ExchangeRate();
            rate.setDate(e.getKey());
            rate.setRate(e.getValue().getAsJsonObject().get("USD").getAsDouble());
            rate.setLastDateCheck(new Date());
            rate.setFrom("EUR");
            rate.setTo("USD");
            repository.save(rate);
        }

    }

    public Iterable<ExchangeRate> findAll() {
        return repository.findAll();
    }

}
