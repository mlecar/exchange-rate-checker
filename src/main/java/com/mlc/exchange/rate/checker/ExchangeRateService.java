package com.mlc.exchange.rate.checker;

import java.util.Map;

public interface ExchangeRateService {

    Map<String, Object> exchangeRate(String from, String to);

}
