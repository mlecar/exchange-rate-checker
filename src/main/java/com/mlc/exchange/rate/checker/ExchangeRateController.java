package com.mlc.exchange.rate.checker;

import java.util.List;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/exchange/rate")
public class ExchangeRateController {

    @Autowired
    private ExchangeRateRepository repository;

    @GetMapping(path = { "/exchange/rate", "/exchange/rate/" }, produces = "application/json")
    public ExchangeRate getLatest() {
        return repository.findAll().iterator().next();

    }

    @GetMapping(path = { "/{startDate}/{endDate}", "/{startDate}/{endDate}/" }, produces = "application/json")
    public List<ExchangeRate> getByPeriod(@PathParam("startDate") String startDate, @PathParam("endDate") String endDate) {

        return repository.findByQuotationDateBetween(startDate, endDate);

    }

}
