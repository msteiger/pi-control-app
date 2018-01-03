package msteiger.de.picontrolapp;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import msteiger.de.picontrolapp.dummy.DialogHelper;
import msteiger.de.picontrolapp.dummy.RelayInfo;

import java.util.List;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ItemDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class ItemListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private final RestService restService = new RestService(PiConfig.instance.getTargetUrl());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        View recyclerView = findViewById(R.id.item_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Snackbar.make(null, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return true;
        }
        return false;
    }

    private void setupRecyclerView(@NonNull final RecyclerView recyclerView) {

        View parentLayout = findViewById(android.R.id.content);
        final Snackbar snackbar = Snackbar.make(parentLayout, "Loading relay data", Snackbar.LENGTH_INDEFINITE);

        final ItemListViewAdapter adapter = new ItemListViewAdapter(this, mTwoPane);
        final AsyncTask<Void, Void, List<RelayInfo>> task = new AsyncTask<Void, Void, List<RelayInfo>>() {

            private volatile Exception exception;

            @Override
            protected List<RelayInfo> doInBackground(Void... params) {
                try {
                    List<RelayInfo> relays = restService.getAllRelays();
                    return relays;
                } catch (Exception e) {
                    this.exception = e;
                    return null;
                }
            }

            @Override
            protected void onPostExecute(List<RelayInfo> relays) {
                if (relays != null) {
                    adapter.setData(relays);
                    snackbar.dismiss();
                } else {
                    String msg = getString(R.string.connection_failed_msg) + ": " + String.valueOf(exception);
                    DialogHelper.createErrorDialog(ItemListActivity.this, msg, true).show();
                }
            }
        };

        recyclerView.setAdapter(adapter);
        snackbar.show();
        task.execute();
    }

    void toggleRelay(String id) {
        AsyncTask<String, Void, Void> task = new ToggleRelayTask(this, restService);
        task.execute(id);
    }

}
