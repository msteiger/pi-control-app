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
import java.util.concurrent.Callable;

import android.support.v7.widget.RecyclerView;
import android.util.Log;

import msteiger.de.picontrolapp.dummy.RelayInfo;

public class HttpRequestTask extends AsyncTask<Void, Void, List<RelayInfo>> {

    private final RestTemplate restTemplate;
    private final ItemListActivity.SimpleItemRecyclerViewAdapter adapter;

    public HttpRequestTask(ItemListActivity.SimpleItemRecyclerViewAdapter adapter) {
        restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

        this.adapter = adapter;
    }

    @Override
    protected List<RelayInfo> doInBackground(Void... params) {
        try {
            String url = "http://msteiger-pc:8080/relays";

            RelayInfo[] relays = restTemplate.getForObject(url, RelayInfo[].class);
            List<RelayInfo> relayInfos = Arrays.asList(relays);
            Log.i("MainActivity", relayInfos.toString());
            return relayInfos;
        } catch (Exception e) {
            Log.e("MainActivity", e.getMessage(), e);
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<RelayInfo> greeting) {
        adapter.setData(greeting);
    }
}
