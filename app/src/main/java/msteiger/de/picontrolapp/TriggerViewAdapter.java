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
import msteiger.de.picontrolapp.dummy.TriggerTime;

class TriggerViewAdapter extends RecyclerView.Adapter<TriggerViewAdapter.ViewHolder> {

    private final ItemDetailFragment itemDetailFragment;

    private final List<TriggerTime> mValues = new ArrayList<>();

    TriggerViewAdapter(ItemDetailFragment itemDetailFragment) {
        this.itemDetailFragment = itemDetailFragment;
    }

    public void setData(Collection<TriggerTime> infos) {
        mValues.clear();
        notifyItemRangeRemoved(0, mValues.size());
        mValues.addAll(infos);
        notifyItemRangeInserted(0,mValues.size());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_detail_trigger, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        TriggerTime info = mValues.get(position);

        holder.textView.setText(info.getTime());

        holder.itemView.setTag(info);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView textView;

        ViewHolder(View view) {
            super(view);
            textView = (TextView) view.findViewById(R.id.time_text);
        }
    }
}
