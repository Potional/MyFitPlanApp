package com.potional.myapplication.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.potional.myapplication.R;
import com.potional.myapplication.daos.AppDatabaseDao;
import com.potional.myapplication.entities.Recipe;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {
    private List<Recipe> recipes;
    private final OnRecipeClickListener listener;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public interface OnRecipeClickListener {
        void onRecipeClick(Recipe recipe);
    }

    public RecipeAdapter(List<Recipe> recipes, OnRecipeClickListener listener) {
        this.recipes = recipes;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recipe, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe recipe = recipes.get(position);
        holder.bind(recipe);
    }

    @Override
    public int getItemCount() {
        if (Objects.isNull(recipes)){
            return 0;
        }
        return recipes.size();
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameTextView;
        private final TextView caloriesTextView;
        private final CheckBox favoriteCheckBox;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.recipe_name_text_view);
            caloriesTextView = itemView.findViewById(R.id.recipe_calories_text_view);
            favoriteCheckBox = itemView.findViewById(R.id.favorite_checkbox);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onRecipeClick(recipes.get(position));
                }
            });
        }

        public void bind(Recipe recipe) {
            nameTextView.setText(recipe.getName());
            caloriesTextView.setText(String.format(Locale.getDefault(), "%.2f cal", recipe.getTotalCalories()));

            // Set checkbox state without triggering listener
            favoriteCheckBox.setOnCheckedChangeListener(null);
            favoriteCheckBox.setChecked(recipe.isFavorite());

            // Handle favorite checkbox click
            favoriteCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                recipe.setFavorite(isChecked);
                // Update in database on a background thread
                AppDatabaseDao database = AppDatabaseDao.getDatabase(itemView.getContext());
                executor.execute(() -> database.recipeDao().update(recipe));
            });
        }
    }

    public void updateRecipes(List<Recipe> newRecipes) {
        this.recipes = newRecipes;
        notifyDataSetChanged();
    }
}