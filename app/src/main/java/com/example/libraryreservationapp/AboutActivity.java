package com.example.libraryreservationapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        // Inicializimi i butonit të rikthimit si variabël lokale
        ImageButton backButton = findViewById(R.id.back_button);

        // Vendosja e një listener për butonin e rikthimit
        backButton.setOnClickListener(v -> {
            // Krijimi i një intent-i për të naviguar nga AboutActivity në ReserveActivity
            Intent intent = new Intent(AboutActivity.this, ReserveActivity.class);
            startActivity(intent);

            // Mbyllja e aktivitetit aktual për të hequr atë nga back stack (opsionale)
            finish();
        });
    }
}
