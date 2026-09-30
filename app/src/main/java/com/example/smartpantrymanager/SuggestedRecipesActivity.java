//SuggestedRecipesActivity with strict matching logic
package com.example.smartpantrymanager;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SuggestedRecipesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecipeAdapter adapter;
    private TextView emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggested_recipes);

        recyclerView = findViewById(R.id.recyclerViewRecipes); // ✅ matches XML
        emptyView = findViewById(R.id.emptyView);              // ✅ added in XML

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Recipe> suggestedRecipes = getSuggestedRecipes();

        if (suggestedRecipes.isEmpty()) {
            emptyView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            adapter = new RecipeAdapter(suggestedRecipes);
            recyclerView.setAdapter(adapter);
            emptyView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    private List<Recipe> getSuggestedRecipes() {
        DatabaseHelper dbHelper = new DatabaseHelper(SuggestedRecipesActivity.this);

        // Read recipes from database
        List<Recipe> recipes = dbHelper.getAllRecipes();

        // Read pantry items
        List<PantryItem> pantryItems = dbHelper.getAllItems();
        if (pantryItems == null) pantryItems = new ArrayList<>();

        List<Recipe> matched = new ArrayList<>();
        for (Recipe recipe : recipes) {
            boolean allPresent = true;
            for (String ingredient : recipe.getIngredients()) {
                boolean found = false;
                for (PantryItem item : pantryItems) {
                    if (item.getName().equalsIgnoreCase(ingredient)) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    allPresent = false;
                    break;
                }
            }
            if (allPresent) {
                matched.add(recipe);
            }
        }

        return matched;
    }
}
