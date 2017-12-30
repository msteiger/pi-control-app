package msteiger.de.picontrolapp;

import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import msteiger.de.picontrolapp.dummy.RelayInfo;

public class RestService {

    private static final int TIMEOUT_MS = 5000;

    private final RestTemplate restTemplate;
    private String baseUrl;

    private static final String RELAYS = "relays";

    public RestService(String url) {
        this.baseUrl = url;
        if (baseUrl.endsWith("/")) {
            baseUrl += "/";
        }

        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setReadTimeout(TIMEOUT_MS);
        factory.setConnectTimeout(TIMEOUT_MS);

        restTemplate = new RestTemplate(factory);
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
    }

    public List<RelayInfo> getAllRelays() {
        String url = baseUrl + RELAYS;
        RelayInfo[] relays = restTemplate.getForObject(url, RelayInfo[].class);
        List<RelayInfo> relayInfos = Arrays.asList(relays);
        return relayInfos;
    }

    public List<RelayInfo> toggleRelay(String id) {
        String url = baseUrl + RELAYS;
        RelayInfo[] relays = restTemplate.getForObject(url, RelayInfo[].class);
        List<RelayInfo> relayInfos = Arrays.asList(relays);
        return relayInfos;
    }
}
