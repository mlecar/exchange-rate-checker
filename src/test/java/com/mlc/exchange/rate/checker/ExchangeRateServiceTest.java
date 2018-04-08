package com.mlc.exchange.rate.checker;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;

public class ExchangeRateServiceTest {

    @InjectMocks
    private ExchangeRateService exchangeRateService;

    @Mock
    private RestTemplate restTemplate;

    private Gson gson = new Gson();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(exchangeRateService, "gson", gson);
    }

    @Test
    public void successGetFromExchangeRateServiceProvider() {
        Map<String, Object> expected = new HashMap<String, Object>();
        expected.put("date", "2018-04-03");
        Map<String, Object> rate = new HashMap<String, Object>();
        rate.put("USD", 1.3);
        expected.put("rates", rate);

        when(restTemplate.getForEntity(anyString(), eq(String.class))).thenReturn(new ResponseEntity<String>(gson.toJson(expected), HttpStatus.OK));

        Map<String, Object> rates = exchangeRateService.exchangeRate("EUR", "USD");

        assertEquals(rates.get("date"), expected.get("date"));
        assertEquals(rates.get("rate"), "1.3");
    }

}
