//Commit: Implemented SQLite CRUD
// Commit marker for GitHub test
package com.example.smartpantrymanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils; // safe join for Android

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database name and version
    private static final String DATABASE_NAME = "SmartPantry.db";
    private static final int DATABASE_VERSION = 1;

    // Pantry table
    public static final String TABLE_ITEMS = "Pantry";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_QUANTITY = "quantity";
    public static final String COLUMN_UNIT = "unit";
    public static final String COLUMN_EXPIRY = "expiry";

    // Recipe table
    public static final String TABLE_RECIPES = "Recipes";
    public static final String COLUMN_RECIPE_ID = "recipe_id"; // ✅ clearer name
    public static final String COLUMN_RECIPE_NAME = "name";
    public static final String COLUMN_RECIPE_INGREDIENTS = "ingredients";
    public static final String COLUMN_RECIPE_STEPS = "steps";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Pantry table
        db.execSQL("CREATE TABLE " + TABLE_ITEMS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_QUANTITY + " INTEGER, " +
                COLUMN_UNIT + " TEXT, " +
                COLUMN_EXPIRY + " TEXT)");

        // Recipe table
        db.execSQL("CREATE TABLE " + TABLE_RECIPES + " (" +
                COLUMN_RECIPE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_RECIPE_NAME + " TEXT, " +
                COLUMN_RECIPE_INGREDIENTS + " TEXT, " +
                COLUMN_RECIPE_STEPS + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);
        onCreate(db);
    }

    // ------------------ PANTRY CRUD ------------------

    public void addItem(PantryItem item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, item.getName());
        values.put(COLUMN_QUANTITY, item.getQuantity());
        values.put(COLUMN_UNIT, item.getUnit());
        values.put(COLUMN_EXPIRY, item.getExpiry());
        db.insert(TABLE_ITEMS, null, values);
        db.close();
    }

    public List<PantryItem> getAllItems() {
        List<PantryItem> itemList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_ITEMS, null, null, null, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_QUANTITY));
                String unit = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_UNIT));
                String expiry = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EXPIRY));

                PantryItem item = new PantryItem(name, quantity, unit, expiry);
                itemList.add(item);
            }
            cursor.close();
        }
        db.close();
        return itemList;
    }

    public void updateItem(int id, PantryItem item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, item.getName());
        values.put(COLUMN_QUANTITY, item.getQuantity());
        values.put(COLUMN_UNIT, item.getUnit());
        values.put(COLUMN_EXPIRY, item.getExpiry());
        db.update(TABLE_ITEMS, values, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void deleteItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ITEMS, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }

    // ------------------ RECIPE CRUD ------------------

    public void addRecipe(Recipe recipe) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_RECIPE_NAME, recipe.getName());
        values.put(COLUMN_RECIPE_INGREDIENTS, TextUtils.join(",", recipe.getIngredients())); // ✅ safe join
        values.put(COLUMN_RECIPE_STEPS, recipe.getSteps());
        db.insert(TABLE_RECIPES, null, values);
        db.close();
    }

    public List<Recipe> getAllRecipes() {
        List<Recipe> recipeList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_RECIPES, null, null, null, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RECIPE_NAME));
                String ingredientsText = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RECIPE_INGREDIENTS));
                String steps = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RECIPE_STEPS));

                List<String> ingredients = new ArrayList<>();
                if (ingredientsText != null && !ingredientsText.isEmpty()) {
                    for (String ing : ingredientsText.split(",")) {
                        ingredients.add(ing.trim());
                    }
                }

                Recipe recipe = new Recipe(name, ingredients, steps);
                recipeList.add(recipe);
            }
            cursor.close();
        }
        db.close();
        return recipeList;
    }

    public void updateRecipe(int id, Recipe recipe) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_RECIPE_NAME, recipe.getName());
        values.put(COLUMN_RECIPE_INGREDIENTS, TextUtils.join(",", recipe.getIngredients())); // ✅ safe join
        values.put(COLUMN_RECIPE_STEPS, recipe.getSteps());
        db.update(TABLE_RECIPES, values, COLUMN_RECIPE_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void deleteRecipe(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_RECIPES, COLUMN_RECIPE_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }

    // ------------------ SEED RECIPES ------------------

    public void seedRecipes() {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(TABLE_RECIPES, null, null, null, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.close();
            db.close();
            return; // already seeded
        }
        cursor.close();

        // ✅ Full recipe list
        insertRecipe(db, "Tomato Rice Pasta", Arrays.asList("Tomato", "Rice"), "Boil rice, add tomato sauce, mix and serve.");
        insertRecipe(db, "Milk Bread Pudding", Arrays.asList("Milk", "Bread", "Sugar"), "Soak bread in milk, add sugar, bake until golden.");
        insertRecipe(db, "Egg Fried Rice", Arrays.asList("Rice", "Eggs"), "Fry rice with scrambled eggs and season to taste.");
        insertRecipe(db, "Vegetable Stir Fry", Arrays.asList("Carrot", "Broccoli", "Soy Sauce"), "Stir fry vegetables with soy sauce until tender.");
        insertRecipe(db, "Chicken Curry", Arrays.asList("Chicken", "Onion", "Tomato"), "Cook chicken with onion, tomato, and spices until tender.");
        insertRecipe(db, "Fruit Salad", Arrays.asList("Apple", "Banana", "Orange"), "Chop fruits and mix together for a refreshing salad.");
        insertRecipe(db, "Grilled Cheese Sandwich", Arrays.asList("Bread", "Cheese", "Butter"), "Toast bread with cheese and butter until golden.");
        insertRecipe(db, "Pancakes", Arrays.asList("Flour", "Milk", "Eggs"), "Mix flour, milk, and eggs. Fry batter until golden.");
        insertRecipe(db, "Omelette", Arrays.asList("Eggs", "Onion", "Tomato"), "Beat eggs, add onion and tomato, cook in pan.");
        insertRecipe(db, "Mashed Potatoes", Arrays.asList("Potatoes", "Butter", "Milk"), "Boil potatoes, mash with butter and milk.");
        insertRecipe(db, "Spaghetti Bolognese", Arrays.asList("Spaghetti", "Tomato", "Minced Meat"), "Cook spaghetti, add minced meat with tomato sauce.");
        insertRecipe(db, "Vegetable Soup", Arrays.asList("Carrot", "Potato", "Onion"), "Boil vegetables in broth until soft.");
        insertRecipe(db, "Rice and Beans", Arrays.asList("Rice", "Beans", "Onion"), "Cook rice and beans together with onion");
        db.close();
    }

    //
    private void insertRecipe(SQLiteDatabase db, String name, List<String> ingredients, String steps) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_RECIPE_NAME, name);
        values.put(COLUMN_RECIPE_INGREDIENTS, TextUtils.join(",", ingredients));
        values.put(COLUMN_RECIPE_STEPS, steps);
        db.insert(TABLE_RECIPES, null, values);
    }
}