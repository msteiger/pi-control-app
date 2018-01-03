package msteiger.de.picontrolapp;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import msteiger.de.picontrolapp.dummy.DayOfWeek;
import msteiger.de.picontrolapp.dummy.LocalTime;
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

        String time = holder.textView.getResources().getString(R.string.time);
        holder.timeClock.setText(info.getTime().toString());
        holder.trigger = info;
        for (int i = 0; i < 7; i++) {
            DayOfWeek weekDay = DayOfWeek.of(i + 1);
            boolean onOff = info.getWeekDays().contains(weekDay);
            holder.dayButtons.get(i).setChecked(onOff);
            holder.dayButtons.get(i).setTag(weekDay);
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView textView;
        final List<ToggleButton> dayButtons = new ArrayList<>(7);
        final EditText timeClock;

        TriggerTime trigger;

        ViewHolder(View view) {
            super(view);
            textView = (TextView) view.findViewById(R.id.time_label);
            timeClock = (EditText) view.findViewById(R.id.time_text);
            String packageName = view.getContext().getPackageName();
            timeClock.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        timeClock.callOnClick();
                    }
                }
            });
            timeClock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TimePickerDialog mTimePicker;
                    mTimePicker = new TimePickerDialog(itemDetailFragment.getContext(),
                            new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                            LocalTime time = new LocalTime(selectedHour, selectedMinute);
                            if (!Objects.equals(trigger.getTime(), time)){
                                trigger.setTime(time);
                                timeClock.setText(time.toString());
                                itemDetailFragment.saveData();
                            }
                        }
                    }, trigger.getTime().getHours(), trigger.getTime().getMins(), true);
                    mTimePicker.setTitle("Select Time");
                    mTimePicker.show();
                }
            });

            String[] weekdays = new DateFormatSymbols(Locale.getDefault()).getShortWeekdays();

            for (int i = 1; i <= 7; i++) {
                String name = "toggleButton" + i;
                int id = view.getResources().getIdentifier(name, "id", packageName);
                ToggleButton button = (ToggleButton)view.findViewById(id);
                final String weekday = weekdays[i].substring(0, 2); // remove trailing dot
                button.setText(weekday);
                button.setTextOn(weekday);
                button.setTextOff(weekday);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToggleButton button = (ToggleButton) v;
                        DayOfWeek weekDay = (DayOfWeek) button.getTag();
                        if (button.isChecked()) {
                            Log.d("TriggerViewAdapter", "Added " + weekday);
                            trigger.getWeekDays().add(weekDay);
                        } else {
                            Log.d("TriggerViewAdapter", "Removed " + weekday);
                            trigger.getWeekDays().remove(weekDay);
                        }
                        itemDetailFragment.saveData();
                    }
                });
                dayButtons.add(button);
            }

        }
    }

}
