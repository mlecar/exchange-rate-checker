package com.mlc.exchange.rate.checker;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("mock")
public class MockedExchangeRateService implements ExchangeRateService {

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public Map<String, Object> exchangeRate(String from, String to) {

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("date", sdf.format(new Date()));
        result.put("rate", new BigDecimal(ThreadLocalRandom.current().nextDouble(1, 2)).setScale(3, BigDecimal.ROUND_UP));

        return result;
    }

}
