package msteiger.de.picontrolapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import msteiger.de.picontrolapp.dummy.RelayInfo;

import java.util.ArrayList;
import java.util.Collection;
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
    private final RestService restService = new RestService("http://msteiger-pc:8080/");

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

        final SimpleItemRecyclerViewAdapter adapter = new SimpleItemRecyclerViewAdapter(this, mTwoPane);
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(ItemListActivity.this);
                    builder.setMessage(getString(R.string.connection_failed_msg) + ": " + String.valueOf(exception));
                    builder.setTitle(R.string.error_dialog_title);
                    builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ItemListActivity.this.finish();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.setCancelable(false);
                    dialog.show();
                }
            }
        };

        recyclerView.setAdapter(adapter);
        snackbar.show();
        task.execute();
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final ItemListActivity mParentActivity;
        private final List<RelayInfo> mValues;
        private final boolean mTwoPane;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RelayInfo item = (RelayInfo) view.getTag();
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putString(ItemDetailFragment.ARG_ITEM_ID, item.getId());
                    ItemDetailFragment fragment = new ItemDetailFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.item_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, ItemDetailActivity.class);
                    intent.putExtra(ItemDetailFragment.ARG_ITEM_ID, item.getId());

                    context.startActivity(intent);
                }
            }
        };

        private final View.OnClickListener toggleButtonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = (String) view.getTag();
            }
        };

        SimpleItemRecyclerViewAdapter(ItemListActivity parent, boolean twoPane) {
            mValues = new ArrayList<>();
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        public void setData(Collection<RelayInfo> infos) {
            mValues.clear();
            notifyItemRangeRemoved(0, mValues.size());
            mValues.addAll(infos);
            notifyItemRangeInserted(0,mValues.size());
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.item_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            RelayInfo info = mValues.get(position);

            holder.textView.setText(info.getName());

            holder.toggleButton.setTag(info.getId());
            holder.toggleButton.setOnClickListener(toggleButtonClickListener);

            holder.itemView.setTag(info);
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            final TextView textView;
            final ImageButton toggleButton;

            ViewHolder(View view) {
                super(view);
                textView = (TextView) view.findViewById(R.id.id_text);
                toggleButton = (ImageButton) view.findViewById(R.id.id_toggle);
            }
        }
    }
}
