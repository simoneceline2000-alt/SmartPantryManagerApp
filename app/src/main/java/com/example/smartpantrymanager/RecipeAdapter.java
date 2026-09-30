// RecipeAdapter layout
package com.example.smartpantrymanager;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private List<Recipe> recipeList;

    public RecipeAdapter(List<Recipe> recipeList) {
        this.recipeList = (recipeList != null) ? recipeList : new ArrayList<>();
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
        Recipe recipe = recipeList.get(position);

        holder.recipeName.setText(recipe.getName() != null ? recipe.getName() : "Unnamed Recipe");
        holder.recipeIngredients.setText(recipe.getIngredients() != null ? recipe.getIngredients().toString() : "[]");
        holder.recipeSteps.setText(recipe.getSteps() != null ? recipe.getSteps() : "No steps provided");

        //Safe click listener
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), RecipeDetailActivity.class);
            intent.putExtra("name", recipe.getName() != null ? recipe.getName() : "Unnamed Recipe");

            // Defensive: avoid null crash
            ArrayList<String> safeIngredients = (recipe.getIngredients() != null)
                    ? new ArrayList<>(recipe.getIngredients())
                    : new ArrayList<>();
            intent.putStringArrayListExtra("ingredients", safeIngredients);

            intent.putExtra("steps", recipe.getSteps() != null ? recipe.getSteps() : "No steps provided");

            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public static class RecipeViewHolder extends RecyclerView.ViewHolder {
        TextView recipeName, recipeIngredients, recipeSteps;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeName = itemView.findViewById(R.id.recipeName);
            recipeIngredients = itemView.findViewById(R.id.recipeIngredients);
            recipeSteps = itemView.findViewById(R.id.recipeSteps);
        }
    }
}

