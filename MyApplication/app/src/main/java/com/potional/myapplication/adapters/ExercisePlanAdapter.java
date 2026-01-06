package com.potional.myapplication.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.potional.myapplication.R;
import com.potional.myapplication.entities.ExercisePlan;

import java.util.List;
import java.util.Locale;

public class ExercisePlanAdapter extends RecyclerView.Adapter<ExercisePlanAdapter.ExercisePlanViewHolder> {
    private List<ExercisePlan> exercisePlans;
    private final OnExercisePlanClickListener listener;

    public interface OnExercisePlanClickListener {
        void onExercisePlanClick(ExercisePlan plan);
    }

    public ExercisePlanAdapter(List<ExercisePlan> exercisePlans, OnExercisePlanClickListener listener) {
        this.exercisePlans = exercisePlans;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ExercisePlanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_exercise_plan, parent, false);
        return new ExercisePlanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExercisePlanViewHolder holder, int position) {
        ExercisePlan plan = exercisePlans.get(position);
        holder.bind(plan);
        holder.itemView.setOnClickListener(v -> listener.onExercisePlanClick(plan));
    }

    @Override
    public int getItemCount() {
        return exercisePlans != null ? exercisePlans.size() : 0;
    }

    public static class ExercisePlanViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameTextView;
        private final TextView timeTextView;
        private final TextView roundsTextView;

        public ExercisePlanViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.exercise_plan_name_text_view);
            timeTextView = itemView.findViewById(R.id.exercise_plan_time_text_view);
            roundsTextView = itemView.findViewById(R.id.exercise_plan_rounds_text_view);
        }

        public void bind(ExercisePlan plan) {
            nameTextView.setText(plan.getName());
            timeTextView.setText(String.format(Locale.getDefault(), "%d minutes", plan.getEstimatedTimeMinutes()));
            roundsTextView.setText(String.format(Locale.getDefault(), "Rounds: %d", plan.getRounds()));
        }
    }

    public void updateExercisePlans(List<ExercisePlan> newPlans) {
        this.exercisePlans = newPlans;
        notifyDataSetChanged();
    }
}
