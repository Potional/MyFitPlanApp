package com.potional.myapplication.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.potional.myapplication.R;
import com.potional.myapplication.entities.Ingredient;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {
    private List<Ingredient> ingredients;
    private final Set<Ingredient> selectedIngredients = new HashSet<>();

    public IngredientAdapter(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ingredient, parent, false);
        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        Ingredient ingredient = ingredients.get(position);
        holder.bind(ingredient);
    }

    @Override
    public int getItemCount() {
        return ingredients != null ? ingredients.size() : 0;
    }

    public class IngredientViewHolder extends RecyclerView.ViewHolder {
        private final CheckBox checkBox;
        private final TextView nameTextView;
        private final TextView caloriesTextView;

        public IngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.ingredient_checkbox);
            nameTextView = itemView.findViewById(R.id.ingredient_name_text_view);
            caloriesTextView = itemView.findViewById(R.id.ingredient_calories_text_view);
        }

        public void bind(Ingredient ingredient) {
            nameTextView.setText(ingredient.getName());
            caloriesTextView.setText(String.format(Locale.getDefault(), "%.2f cal/100g", ingredient.getCaloriesPer100g()));

            checkBox.setOnCheckedChangeListener(null);
            checkBox.setChecked(selectedIngredients.contains(ingredient));

            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    selectedIngredients.add(ingredient);
                } else {
                    selectedIngredients.remove(ingredient);
                }
            });
        }
    }

    public List<Ingredient> getSelectedIngredients() {
        return new ArrayList<>(selectedIngredients);
    }

    public void updateIngredients(List<Ingredient> newIngredients) {
        this.ingredients = newIngredients;
        notifyDataSetChanged();
    }
}