package com.mlc.exchange.rate.checker;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping("/exchange/rate")
public class ExchangeRateController {

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private ExchangeRateManager exchangeRateManager;

    @GetMapping(produces = "application/json")
    public Map<String, Object> getLatest() {
        return exchangeRateManager.getLatestExchangeRate();
    }

    @GetMapping(path = { "/all", "/all/" }, produces = "application/json")
    public Iterable<ExchangeRate> getAll() {
        return exchangeRateManager.findAll();
    }

    @GetMapping(path = { "/dateseries/{startDate}/{endDate}", "/dateseries/{startDate}/{endDate}/" }, produces = "application/json")
    public Map<String, Object> getByPeriod(@DateTimeFormat(pattern = "yyyy-MM-dd") @PathVariable("startDate") LocalDate startDate, @DateTimeFormat(pattern = "yyyy-MM-dd") @PathVariable("endDate") LocalDate endDate) {

        return exchangeRateManager.getExchangeRate(startDate, endDate);

    }

    /**
     * Built for testing purpose. Loading some historical exchange rates EUR to
     * USD from a json file in the classpath
     * 
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    @ResponseBody
    @PutMapping(value = { "/historical/load", "/historical/load/" })
    public ResponseEntity<String> loadFile() throws FileNotFoundException, IOException {

        Resource resource = resourceLoader.getResource("classpath:historical.exchange.rates.EUR.USD.json");

        exchangeRateManager.loadHistoricalRates(resource);

        return new ResponseEntity<String>(HttpStatus.CREATED);
    }

}
