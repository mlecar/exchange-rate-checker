package com.mlc.exchange.rate.checker;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.test.util.ReflectionTestUtils;

public class ExchangeRateManagerTest {

    @Spy
    private MockedExchangeRateService exchangeRateService;

    @Mock
    private ExchangeRateRepository repository;

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

}
