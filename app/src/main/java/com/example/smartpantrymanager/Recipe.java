package com.example.smartpantrymanager;

import java.util.ArrayList;
import java.util.List;

public class Recipe {
    private String name;
    private List<String> ingredients;
    private String steps;

    public Recipe(String name, List<String> ingredients, String steps) {
        this.name = name;

        this.ingredients = (ingredients != null) ? ingredients : new ArrayList<>();
        this.steps = (steps != null) ? steps : "";
    }

    public String getName() {
        return name;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public String getSteps() {
        return steps;
    }
}

