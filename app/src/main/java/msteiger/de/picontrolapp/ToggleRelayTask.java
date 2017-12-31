package msteiger.de.picontrolapp;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Toast;

import msteiger.de.picontrolapp.ItemListActivity;
import msteiger.de.picontrolapp.R;
import msteiger.de.picontrolapp.RestService;

class ToggleRelayTask extends AsyncTask<String, Void, Void> {

    private final Activity parent;

    private final RestService restService;

    private volatile Exception exception;

    ToggleRelayTask(Activity parent, RestService restService) {
        this.restService = restService;
        this.parent = parent;
    }

    @Override
    protected Void doInBackground(String... params) {
        try {
            String id = params[0];
            restService.toggleRelay(id);
        } catch (Exception e) {
            this.exception = e;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void relays) {
        if (exception != null) {
            String msg = parent.getString(R.string.connection_failed_msg);
            Toast.makeText(parent, msg, Toast.LENGTH_LONG).show();
        }
    }
}