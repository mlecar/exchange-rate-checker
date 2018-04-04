package com.mlc.exchange.rate.checker;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface ExchangeRateRepository extends CrudRepository<ExchangeRate, Long> {

    ExchangeRate findFirstByOrderByQuotationDateDesc();

    List<ExchangeRate> findByQuotationDateBetween(String startDate, String finalDate);

}
