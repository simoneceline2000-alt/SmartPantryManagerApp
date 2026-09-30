//Implemented RecipeDetailActivity with layout binding
package com.example.smartpantrymanager;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class RecipeDetailActivity extends AppCompatActivity {

    private TextView recipeName, recipeIngredients, recipeSteps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        // Match IDs with XML
        recipeName = findViewById(R.id.detailName);
        recipeIngredients = findViewById(R.id.detailIngredients);
        recipeSteps = findViewById(R.id.detailSteps);

        // Get data from Intent
        String name = getIntent().getStringExtra("name");
        List<String> ingredients = getIntent().getStringArrayListExtra("ingredients");
        String steps = getIntent().getStringExtra("steps");

        recipeName.setText(name != null ? name : "No name provided");

        if (ingredients != null && !ingredients.isEmpty()) {
            recipeIngredients.setText("Ingredients: " + ingredients.toString());
        } else {
            recipeIngredients.setText("Ingredients: none");
        }

        recipeSteps.setText(steps != null ? "Steps: " + steps : "Steps: none");
    }
}
