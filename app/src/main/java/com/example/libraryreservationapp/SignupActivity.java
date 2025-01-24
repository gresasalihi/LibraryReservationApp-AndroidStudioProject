package com.example.libraryreservationapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Inicjalizimi i EditText dhe Button nga layout-i
        EditText usernameEditText = findViewById(R.id.username);
        EditText passwordEditText = findViewById(R.id.password);
        EditText addressEditText = findViewById(R.id.address);
        Button signupButton = findViewById(R.id.signup_button);
        ImageButton backButton = findViewById(R.id.back_button);

        // Krijo një instancë të DatabaseHelper për të menaxhuar databazën
        try (DatabaseHelper dbHelper = new DatabaseHelper(this)) {

            backButton.setOnClickListener(v -> {
                // Kthehu te LoginActivity kur shtypet butoni back
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            });

            // Vendos klikues për butonin e regjistrimit
            signupButton.setOnClickListener(v -> handleSignup(usernameEditText, passwordEditText, addressEditText, dbHelper)); // Menaxho procesin e regjistrimit
        }
    }

    // Funksion për të trajtuar regjistrimin e përdoruesit
    private void handleSignup(EditText usernameEditText, EditText passwordEditText, EditText addressEditText, DatabaseHelper dbHelper) {
        // Merrni tekstin e futur nga përdoruesi dhe hiqni hapësirat boshe
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String address = addressEditText.getText().toString().trim();

        // Kontrolloni cilat fusha janë bosh dhe ndërtoni një mesazh gabimi
        StringBuilder errorMessage = new StringBuilder();
        if (username.isEmpty()) {
            errorMessage.append("Username is required.\n");
        }
        if (password.isEmpty()) {
            errorMessage.append("Password is required.\n");
        }
        if (address.isEmpty()) {
            errorMessage.append("Email Address is required.\n");
        }

        if (errorMessage.length() > 0) {
            // Trego mesazhin e gabimit në një Toast
            Toast.makeText(this, errorMessage.toString().trim(), Toast.LENGTH_LONG).show();
            return;
        }

        // Hapni databazën për shkrim dhe kontrolloni nëse emri i përdoruesit ekziston tashmë
        try (SQLiteDatabase database = dbHelper.getWritableDatabase();
             Cursor cursor = database.rawQuery("SELECT * FROM users WHERE username = ?", new String[]{username})) {

            if (cursor.getCount() > 0) {
                showAlert(); // Trego një alert nëse emri i përdoruesit ekziston
                return;
            }

            // Futni përdoruesin e ri në databazë
            database.execSQL("INSERT INTO users (username, password, address) VALUES (?, ?, ?)",
                    new String[]{username, password, address});

            // Navigoni te ReserveActivity pas regjistrimit të suksesshëm
            Intent intent = new Intent(SignupActivity.this, ReserveActivity.class);
            startActivity(intent);
            finish();
        }
    }

    // Funksion për të treguar një dialog alert kur emri i përdoruesit ekziston tashmë
    private void showAlert() {
        new AlertDialog.Builder(this)
                .setTitle("Username already exists.")
                .setMessage("Please try a different username.")
                .setPositiveButton("OK", null)
                .show();
    }
}
