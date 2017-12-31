package msteiger.de.picontrolapp;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import msteiger.de.picontrolapp.dummy.RelayInfo;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    private final RestService restService = new RestService(PiConfig.instance.getTargetUrl());

    /**
     * The content this fragment is presenting.
     */
    private RelayInfo relayInfo;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String id = getArguments().getString(ARG_ITEM_ID);
        if (id != null) {
            loadRelay(id);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_detail, container, false);
        return rootView;
    }

    private void loadRelay(String id) {
        final AsyncTask<String, Void, RelayInfo> task = new AsyncTask<String, Void, RelayInfo>() {

            private volatile Exception exception;

            @Override
            protected RelayInfo doInBackground(String... params) {
                try {
                    String id = params[0];
                    return restService.getRelay(id);
                } catch (Exception e) {
                    this.exception = e;
                }
                return null;
            }

            @Override
            protected void onPostExecute(RelayInfo relay) {
                if (exception != null) {
                    String msg = getString(R.string.connection_failed_msg);
                    Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
                } else {
                    showRelay(relay);
                }

            }
        };
        task.execute(id);
    }

    private void showRelay(RelayInfo relayInfo) {
        this.relayInfo = relayInfo;
        Activity activity = this.getActivity();

        View view = this.getView();
        CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
        if (appBarLayout != null) {
            appBarLayout.setTitle(relayInfo.getName());
        }
        ((TextView) view.findViewById(R.id.item_detail)).setText(relayInfo.getTriggers().toString());

    }
}
