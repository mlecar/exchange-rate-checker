package com.mlc.exchange.rate.checker;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

@Service
public class ExchangeRateService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private Gson gson;

    @Value("${exchange.rate.provider.url}")
    private String exchangeServiceProviderUrl;

    public Map<String, Object> exchangeRate(String from, String to) {

        ResponseEntity<String> response = restTemplate.getForEntity(exchangeServiceProviderUrl + from, String.class);
        JsonElement rate = gson.fromJson(response.getBody(), JsonElement.class);

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("date", rate.getAsJsonObject().get("date").getAsString());
        result.put("rate", rate.getAsJsonObject().get("rates").getAsJsonObject().get(to).getAsString());

        return result;
    }

}
