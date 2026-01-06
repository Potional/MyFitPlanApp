package com.potional.myapplication.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.potional.myapplication.R;
import com.potional.myapplication.entities.Exercise;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder> {
    private List<Exercise> exercises;
    private final Set<Exercise> selectedExercises = new HashSet<>();

    public ExerciseAdapter(List<Exercise> exercises) {
        this.exercises = exercises;
    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_exercise, parent, false);
        return new ExerciseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
        Exercise exercise = exercises.get(position);
        holder.bind(exercise);
    }

    @Override
    public int getItemCount() {
        return exercises != null ? exercises.size() : 0;
    }

    public class ExerciseViewHolder extends RecyclerView.ViewHolder {
        private final CheckBox checkBox;
        private final TextView nameTextView;

        public ExerciseViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.exercise_checkbox);
            nameTextView = itemView.findViewById(R.id.exercise_name_text_view);
        }

        public void bind(Exercise exercise) {
            nameTextView.setText(exercise.getName());

            checkBox.setOnCheckedChangeListener(null);
            checkBox.setChecked(selectedExercises.contains(exercise));

            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    selectedExercises.add(exercise);
                } else {
                    selectedExercises.remove(exercise);
                }
            });
        }
    }

    public List<Exercise> getSelectedExercises() {
        return new ArrayList<>(selectedExercises);
    }

    public void updateExercises(List<Exercise> newExercises) {
        this.exercises = newExercises;
        notifyDataSetChanged();
    }
}
