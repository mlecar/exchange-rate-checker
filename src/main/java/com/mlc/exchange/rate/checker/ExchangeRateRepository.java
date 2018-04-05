package com.mlc.exchange.rate.checker;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ExchangeRateRepository extends CrudRepository<ExchangeRate, Long> {

    ExchangeRate findFirstByOrderByLastDateCheckDesc();

    @Query("SELECT rate FROM ExchangeRate rate WHERE rate.date >= ?1 AND rate.date <= ?2")
    List<ExchangeRate> findByDateBetween(String startDate, String finalDate);

}
