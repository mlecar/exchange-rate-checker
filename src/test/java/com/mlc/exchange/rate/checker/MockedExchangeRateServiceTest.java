package com.mlc.exchange.rate.checker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

public class MockedExchangeRateServiceTest {

    @InjectMocks
    private MockedExchangeRateService exchangeRateService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void successMock() {
        Map<String, Object> expected = new HashMap<String, Object>();
        expected.put("date", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        Map<String, Object> rate = new HashMap<String, Object>();
        rate.put("USD", 1.3);
        expected.put("rates", rate);

        Map<String, Object> rates = exchangeRateService.exchangeRate("EUR", "USD");

        assertEquals(rates.get("date"), expected.get("date"));
        assertTrue(Double.valueOf(rates.get("rate").toString()) > 0);
    }

}
