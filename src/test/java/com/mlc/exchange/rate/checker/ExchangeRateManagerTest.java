package com.mlc.exchange.rate.checker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

public class ExchangeRateManagerTest {

    @Mock
    private ExchangeRateService exchangeRateService;

    @Mock
    private ExchangeRateRepository repository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ExchangeRateManager exchangeManager;

    @Captor
    private ArgumentCaptor<ExchangeRate> captor;

    private String from = "EUR";

    private String to = "USD";

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(exchangeManager, "from", from);
        ReflectionTestUtils.setField(exchangeManager, "to", to);
    }

    @Test
    public void successUpdateExchangeRates() {
        Map<String, Object> expected = new HashMap<>();
        expected.put("rate", 1.234);
        expected.put("date", "2018-04-06");

        when(exchangeRateService.exchangeRate(from, to)).thenReturn(expected);

        exchangeManager.updateExchangeRates();

        verify(repository).save(captor.capture());

        assertEquals(from, captor.getValue().getFrom());
        assertEquals(to, captor.getValue().getTo());
        assertEquals(expected.get("date"), captor.getValue().getDate());
        assertEquals(expected.get("rate"), captor.getValue().getRate());
    }

    @Test
    public void successLatestExchangeRate() {
        ExchangeRate rate = new ExchangeRate();
        rate.setDate("2018-08-04");
        rate.setFrom("EUR");
        rate.setTo("USD");
        rate.setLastDateCheck(new Date());
        rate.setRate(1.3);

        when(repository.findFirstByOrderByLastDateCheckDesc()).thenReturn(rate);

        Map<String, Object> result = exchangeManager.getLatestExchangeRate();

        JsonElement e = new Gson().toJsonTree(result);

        assertEquals(e.getAsJsonObject().get("from").getAsString(), rate.getFrom());
        assertEquals(e.getAsJsonObject().get("to").getAsString(), rate.getTo());
        assertTrue(e.getAsJsonObject().get("rates").getAsJsonObject().get("rate").getAsDouble() == rate.getRate());
        assertEquals(e.getAsJsonObject().get("rates").getAsJsonObject().get("date").getAsString(), rate.getDate());
    }

    @Test
    public void successHistoricalExchangeRatesLoad() throws FileNotFoundException, IOException {
        Resource resource = new ClassPathResource("historical-exchange-rates.from.euro.to.usdollar.json");

        exchangeManager.loadHistoricalRates(resource);
    }

    @Test
    public void successExchangeRateByPeriod() {

        LocalDate startDate = LocalDate.parse("2018-08-07");
        LocalDate endDate = LocalDate.parse("2018-08-10");

        List<ExchangeRate> rates = new ArrayList<>();

        Date lastTimeChecked = new Date();

        ExchangeRate er1 = new ExchangeRate();
        er1.setDate("2018-08-07");
        er1.setFrom("EUR");
        er1.setTo("USD");
        er1.setLastDateCheck(lastTimeChecked);
        er1.setRate(1.2345);

        ExchangeRate er2 = new ExchangeRate();
        er2.setDate("2018-08-08");
        er2.setFrom("EUR");
        er2.setTo("USD");
        er2.setLastDateCheck(lastTimeChecked);
        er2.setRate(1.9999);

        ExchangeRate er3 = new ExchangeRate();
        er3.setDate("2018-08-09");
        er3.setFrom("EUR");
        er3.setTo("USD");
        er3.setLastDateCheck(lastTimeChecked);
        er3.setRate(1.1111);

        rates.add(er1);
        rates.add(er2);
        rates.add(er3);

        when(repository.findByDateBetween(startDate.toString(), endDate.toString())).thenReturn(rates);

        Map<String, Object> result = exchangeManager.getExchangeRate(startDate, endDate);

        JsonElement e = new Gson().toJsonTree(result);

        JsonElement jsonRates = e.getAsJsonObject().get("rates");

        assertEquals(e.getAsJsonObject().get("from").getAsString(), er1.getFrom());
        assertEquals(e.getAsJsonObject().get("to").getAsString(), er1.getTo());
        assertTrue(jsonRates.getAsJsonArray().get(0).getAsJsonObject().get("rate").getAsDouble() == er1.getRate());
        assertEquals(jsonRates.getAsJsonArray().get(0).getAsJsonObject().get("date").getAsString(), er1.getDate());

        assertEquals(e.getAsJsonObject().get("from").getAsString(), er2.getFrom());
        assertEquals(e.getAsJsonObject().get("to").getAsString(), er2.getTo());
        assertTrue(jsonRates.getAsJsonArray().get(1).getAsJsonObject().get("rate").getAsDouble() == er2.getRate());
        assertEquals(jsonRates.getAsJsonArray().get(1).getAsJsonObject().get("date").getAsString(), er2.getDate());

        assertEquals(e.getAsJsonObject().get("from").getAsString(), er3.getFrom());
        assertEquals(e.getAsJsonObject().get("to").getAsString(), er3.getTo());
        assertTrue(jsonRates.getAsJsonArray().get(2).getAsJsonObject().get("rate").getAsDouble() == er3.getRate());
        assertEquals(jsonRates.getAsJsonArray().get(2).getAsJsonObject().get("date").getAsString(), er3.getDate());

    }

}
