package com.mlc.exchange.rate.checker;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.FileNotFoundException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ExchangeRateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExchangeRateManager manager;

    private String baseContext = "/exchange/rate";

    @Test
    public void invalidStartDate() throws Exception {
        this.mockMvc.perform(get(baseContext + "/20180306/2018-03-06")).andExpect(status().isBadRequest());
    }

    @Test
    public void invalidEndDate() throws Exception {
        this.mockMvc.perform(get(baseContext + "/2018-03-06/20180306")).andExpect(status().isBadRequest());
    }

    @Test
    public void validStartDateAndEndDate() throws Exception {
        this.mockMvc.perform(get(baseContext + "/2018-03-06/2018-03-06")).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
    }

    @Test
    public void missingDate() throws Exception {
        this.mockMvc.perform(get(baseContext + "/2018-03-06/")).andExpect(status().isNotFound());
    }

    @Test
    public void successLatestRate() throws Exception {
        this.mockMvc.perform(get(baseContext)).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
    }

    @Test
    public void successHistoricalLoad() throws Exception {
        this.mockMvc.perform(put(baseContext + "/historical/load")).andExpect(status().isCreated());
    }

    @Test
    public void methodNotSupported() throws Exception {
        this.mockMvc.perform(post(baseContext)).andExpect(status().isBadRequest());
    }

    @Test
    public void internalServerErrorLoadingHistoricExchangeRates() throws Exception {
        doThrow(new FileNotFoundException("any error")).when(manager).loadHistoricalRates(anyObject());
        this.mockMvc.perform(put(baseContext + "/historical/load")).andExpect(status().isInternalServerError());
    }

}
