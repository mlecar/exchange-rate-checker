package com.mlc.exchange.rate.checker;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class MockedExchangeRateService {

    public Map<String, Object> exchangeRate(String from, String to) {

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("date", "2018-04-04");
        result.put("rate", "1.2390");

        return result;
    }

}
