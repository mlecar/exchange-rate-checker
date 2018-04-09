package com.mlc.exchange.rate.checker;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

@Service
@Profile("!mock")
public class ExchangeRateServiceImpl implements ExchangeRateService {

    private static Logger logger = LoggerFactory.getLogger(ExchangeRateServiceImpl.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private Gson gson;

    @Value("${exchange.rate.provider.url}")
    private String exchangeServiceProviderUrl;

    public Map<String, Object> exchangeRate(String from, String to) {
        Map<String, Object> result = new HashMap<String, Object>();

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(exchangeServiceProviderUrl + from, String.class);
            JsonElement rate = gson.fromJson(response.getBody(), JsonElement.class);

            result.put("date", rate.getAsJsonObject().get("date").getAsString());
            result.put("rate", rate.getAsJsonObject().get("rates").getAsJsonObject().get(to).getAsString());

            return result;
        } catch (RestClientResponseException e) {
            logger.error("Failed to get latest exchange rate from service provider - {} - {}", e.getRawStatusCode() + " - " + e.getStatusText());
        } catch (ResourceAccessException e) {
            logger.error("Failed to get latest exchange rate from service provider - {} - {}", e.getMessage());
        }
        return result;

    }

}
