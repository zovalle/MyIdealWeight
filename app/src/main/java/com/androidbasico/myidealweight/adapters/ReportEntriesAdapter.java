package com.androidbasico.myidealweight.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidbasico.myidealweight.R;
import com.androidbasico.myidealweight.models.RecordEntry;

import java.util.List;

public class ReportEntriesAdapter extends RecyclerView.Adapter<ReportEntriesAdapter.EntryViewHolder> {
    private List<RecordEntry> entries;

    public ReportEntriesAdapter(List<RecordEntry> entries) {
        this.entries = entries;
    }

    public static class EntryViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView text;
        private ImageView image;

        public EntryViewHolder(View entryView) {
            super(entryView);
            title = (TextView) entryView.findViewById(R.id.result_title);
            text = (TextView) entryView.findViewById(R.id.result_detail);
            image = (ImageView) entryView.findViewById(R.id.imc_result_icon);
        }
    }

    @Override
    public EntryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_row, parent, false);
        EntryViewHolder viewHolder = new EntryViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(EntryViewHolder holder, int position) {
        holder.title.setText(entries.get(position).getDate());
        holder.text.setText(entries.get(position).getIMCDescriptionResult());
        holder.image.setImageResource(entries.get(position).getIMCImageIdResult());
    }

    @Override
    public int getItemCount() {
        return entries.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
