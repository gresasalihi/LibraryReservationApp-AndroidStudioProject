package com.example.libraryreservationapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class GetStartedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started); // Vendos layout-in e aktivitetit të caktuar

        Button getStartedButton = findViewById(R.id.get_started_button); // Gjen butonin "get_started_button" nga layout-i

        // Zëvendësimi i klasës anonime të brendshme me një shprehje lambda
        getStartedButton.setOnClickListener(v -> {
            // Intent për të kaluar në aktivitetin Login (Identifikimi)
            Intent intent = new Intent(GetStartedActivity.this, LoginActivity.class);
            startActivity(intent); // Fillon aktivitetin e ri
        });
    }
}
