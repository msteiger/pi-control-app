package msteiger.de.picontrolapp;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;

import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import msteiger.de.picontrolapp.dummy.RelayInfo;

public class RestService {

    private static final int TIMEOUT_MS = 5000;
    public static final String PREF_WEBSERVER_URL = "webserver_url";

    private final RestTemplate restTemplate;
    private String baseUrl;

    private static final String RELAYS = "relays";

    public RestService(Context context) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        this.baseUrl = sharedPref.getString(PREF_WEBSERVER_URL, "");

        sharedPref.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {

            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                if (key.equals(PREF_WEBSERVER_URL)) {
                    baseUrl = sharedPreferences.getString(key, "");
                }
            }
        });

        if (!baseUrl.endsWith("/")) {
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

    public RelayInfo getRelay(String id) {
        String url = baseUrl + RELAYS + "/" + id;
        RelayInfo relayInfo = restTemplate.getForObject(url, RelayInfo.class);
        return relayInfo;
    }

    public void toggleRelay(String id) {
        String url = baseUrl + RELAYS + "/" + id + "/toggle";
        restTemplate.postForObject(url, null, void.class);
    }

    public void setRelay(RelayInfo relay) {
        String url = baseUrl + RELAYS;
        restTemplate.postForObject(url, relay, Void.class);
    }
}
