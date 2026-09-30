package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddEditItemActivity extends AppCompatActivity {

    private EditText editName, editQuantity, editUnit, editExpiry;
    private Button btnSave, btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_item);

        editName = findViewById(R.id.editName);
        editQuantity = findViewById(R.id.editQuantity);
        editUnit = findViewById(R.id.editUnit);
        editExpiry = findViewById(R.id.editExpiry);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        // If extras exist, pre-fill fields (editing mode)
        if (getIntent().hasExtra("name")) {
            editName.setText(getIntent().getStringExtra("name"));
            editQuantity.setText(String.valueOf(getIntent().getIntExtra("quantity", 0)));
            editUnit.setText(getIntent().getStringExtra("unit"));
            editExpiry.setText(getIntent().getStringExtra("expiry"));

            btnSave.setText("Update"); // change button text when editing
        }

        btnSave.setOnClickListener(v -> saveItem());
        btnCancel.setOnClickListener(v -> finish());
    }

    private void saveItem() {
        String name = editName.getText().toString().trim();
        String qtyText = editQuantity.getText().toString().trim();
        String unit = editUnit.getText().toString().trim();
        String expiry = editExpiry.getText().toString().trim();

        // Basic validation
        if (name.isEmpty() || qtyText.isEmpty() || unit.isEmpty() || expiry.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int quantity;
        try {
            quantity = Integer.parseInt(qtyText);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Quantity must be a number", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseHelper dbHelper = new DatabaseHelper(AddEditItemActivity.this);

        if (getIntent().hasExtra("id")) {
            // ✅ Update existing item (make sure you pass "id" in intent when editing)
            int itemId = getIntent().getIntExtra("id", -1);
            dbHelper.updateItem(itemId, new PantryItem(name, quantity, unit, expiry));
            Toast.makeText(this, "Item updated successfully", Toast.LENGTH_SHORT).show();
        } else {
            //  Insert new item
            dbHelper.addItem(new PantryItem(name, quantity, unit, expiry));
            Toast.makeText(this, "Item saved successfully", Toast.LENGTH_SHORT).show();
        }

        finish(); // close activity and return to list
    }
}

