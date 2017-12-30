package msteiger.de.picontrolapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import msteiger.de.picontrolapp.dummy.RelayInfo;

class ItemListViewAdapter extends RecyclerView.Adapter<ItemListViewAdapter.ViewHolder> {

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
            mParentActivity.toggleRelay(id);
        }
    };

    ItemListViewAdapter(ItemListActivity parent, boolean twoPane) {
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

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView textView;
        final ImageButton toggleButton;

        ViewHolder(View view) {
            super(view);
            textView = (TextView) view.findViewById(R.id.id_text);
            toggleButton = (ImageButton) view.findViewById(R.id.id_toggle);
        }
    }
}
