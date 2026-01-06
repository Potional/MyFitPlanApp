package com.potional.myapplication.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;
import com.potional.myapplication.R;
import com.potional.myapplication.adapters.ExerciseAdapter;
import com.potional.myapplication.daos.AppDatabaseDao;
import com.potional.myapplication.entities.Exercise;
import com.potional.myapplication.entities.ExercisePlan;
import com.potional.myapplication.entities.ExercisePlanExerciseCrossRef;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddExercisePlanActivity extends AppCompatActivity {
    private EditText nameEditText;
    private RecyclerView exercisesRecyclerView;
    private ExerciseAdapter exerciseAdapter;
    private AppDatabaseDao database;
    private ExecutorService executor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercise_plan);

        database = AppDatabaseDao.getDatabase(this);
        executor = Executors.newSingleThreadExecutor();

        initViews();
        setupRecyclerView();
        loadExercises();
        setupSaveButton();
        setupAddExerciseButton();
    }

    private void initViews() {
        nameEditText = findViewById(R.id.plan_name_edit_text);
        exercisesRecyclerView = findViewById(R.id.exercises_recycler_view);
    }

    private void loadExercises() {
        executor.execute(() -> {
            List<Exercise> allExercises = database.exerciseDao().getAllExercises();
            runOnUiThread(() -> exerciseAdapter.updateExercises(allExercises));
        });
    }

    private void setupRecyclerView() {
        exerciseAdapter = new ExerciseAdapter(new ArrayList<>());
        exercisesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        exercisesRecyclerView.setAdapter(exerciseAdapter);
    }

    private void setupSaveButton() {
        Button saveButton = findViewById(R.id.save_plan_button);
        saveButton.setOnClickListener(v -> saveExercisePlan());
    }

    private void setupAddExerciseButton() {
        Button addExerciseButton = findViewById(R.id.add_exercise_button);
        addExerciseButton.setOnClickListener(v -> showAddExerciseDialog());
    }

    private void showAddExerciseDialog() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setTitle("Add New Exercise");

        View view = getLayoutInflater().inflate(R.layout.dialog_add_exercise, null);
        builder.setView(view);

        TextInputLayout nameLayout = view.findViewById(R.id.exercise_name_text_input_layout);
        TextInputLayout descriptionLayout = view.findViewById(R.id.exercise_description_text_input_layout);
        TextInputLayout setsLayout = view.findViewById(R.id.exercise_sets_text_input_layout);
        TextInputLayout repsLayout = view.findViewById(R.id.exercise_reps_text_input_layout);

        builder.setPositiveButton("Add", (dialog, which) -> {
            String name = Objects.requireNonNull(nameLayout.getEditText()).getText().toString().trim();
            String description = Objects.requireNonNull(descriptionLayout.getEditText()).getText().toString().trim();
            String setsStr = Objects.requireNonNull(setsLayout.getEditText()).getText().toString().trim();
            String repsStr = Objects.requireNonNull(repsLayout.getEditText()).getText().toString().trim();

            boolean hasError = false;
            if (TextUtils.isEmpty(name)) {
                nameLayout.setError("Name is required");
                hasError = true;
            }
            if (TextUtils.isEmpty(description)) {
                descriptionLayout.setError("Description is required");
                hasError = true;
            }
            if (TextUtils.isEmpty(setsStr)) {
                setsLayout.setError("Sets are required");
                hasError = true;
            }
            if (TextUtils.isEmpty(repsStr)) {
                repsLayout.setError("Reps are required");
                hasError = true;
            }

            if (hasError) {
                return;
            }

            int sets = Integer.parseInt(setsStr);
            int reps = Integer.parseInt(repsStr);

            Exercise newExercise = new Exercise();
            newExercise.setName(name);
            newExercise.setDescription(description);
            newExercise.setSets(sets);
            newExercise.setReps(reps);

            executor.execute(() -> {
                database.exerciseDao().insertAll(newExercise);
                loadExercises();
            });
        });

        builder.setNegativeButton("Cancel", null);
        builder.create().show();
    }

    private void saveExercisePlan() {
        String name = nameEditText.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            nameEditText.setError("Please enter plan name");
            return;
        }

        List<Exercise> selectedExercises = exerciseAdapter.getSelectedExercises();
        if (selectedExercises.isEmpty()) {
            Toast.makeText(this, "Please select at least one exercise", Toast.LENGTH_SHORT).show();
            return;
        }

        ExercisePlan exercisePlan = new ExercisePlan();
        exercisePlan.setName(name);

        List<ExercisePlanExerciseCrossRef> exercisePlanExercises = new ArrayList<>();
        for (Exercise exercise : selectedExercises) {
            ExercisePlanExerciseCrossRef crossRef = new ExercisePlanExerciseCrossRef();
            crossRef.exerciseId = exercise.getId();
            exercisePlanExercises.add(crossRef);
        }

        executor.execute(() -> {
            database.exercisePlanDao().insertPlanWithExercises(exercisePlan, exercisePlanExercises, database.exercisePlanDao());

            runOnUiThread(() -> {
                Toast.makeText(this, "Exercise plan saved successfully", Toast.LENGTH_SHORT).show();
                finish();
            });
        });
    }
}
