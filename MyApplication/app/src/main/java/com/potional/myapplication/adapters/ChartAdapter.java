package com.potional.myapplication.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.potional.myapplication.R;

import java.util.List;

public class ChartAdapter extends RecyclerView.Adapter<ChartAdapter.ChartViewHolder> {
    private final List<String> chartList;
    private final OnChartClickListener listener;

    public interface OnChartClickListener {
        void onChartClick(String chartName);
    }

    public ChartAdapter(List<String> chartList, OnChartClickListener listener) {
        this.chartList = chartList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ChartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ChartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChartViewHolder holder, int position) {
        String chartName = chartList.get(position);
        holder.bind(chartName);
    }

    @Override
    public int getItemCount() {
        return chartList.size();
    }

    public class ChartViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;

        public ChartViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(android.R.id.text1);
        }

        public void bind(String chartName) {
            textView.setText(chartName);
            itemView.setOnClickListener(v -> listener.onChartClick(chartName));
        }
    }
}
