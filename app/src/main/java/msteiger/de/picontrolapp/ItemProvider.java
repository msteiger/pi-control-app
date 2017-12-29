package msteiger.de.picontrolapp;

import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.util.Log;

import msteiger.de.picontrolapp.dummy.RelayInfo;

public class ItemProvider {

    private Map<String, RelayInfo> relays = new LinkedHashMap<>();

    public ItemProvider() {
        HttpRequestTask httpRequestTask = new HttpRequestTask();
        httpRequestTask.execute();
    }

    public List<RelayInfo> retrieveAll() {
        return new ArrayList<>(relays.values());
    }

    public RelayInfo retrieve(String id) {
        return relays.get(id);
    }

    private class HttpRequestTask extends AsyncTask<Void, Void, List<RelayInfo>> {
        @Override
        protected List<RelayInfo> doInBackground(Void... params) {
            try {
                String url = "http://msteiger-pc:8080/relays";

                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
                RelayInfo[] relays = restTemplate.getForObject(url, RelayInfo[].class);
                Log.i("MainActivity", relays.toString());
                return Arrays.asList(relays);
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<RelayInfo> greeting) {
            for (RelayInfo info : greeting) {
                relays.put(info.getId(), info);
            }
        }

    }
}
