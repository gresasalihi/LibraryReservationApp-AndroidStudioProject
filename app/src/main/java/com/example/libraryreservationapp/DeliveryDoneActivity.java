package com.example.libraryreservationapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class DeliveryDoneActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_done);

        // Gjej butonin me ID-në go_home_button nga layout-i
        Button goHomeButton = findViewById(R.id.go_home_button);

        // Vendos një listener për klikime në buton
        goHomeButton.setOnClickListener(v -> {
            // Krijo një Intent për të hapur ReserveActivity
            Intent intent = new Intent(DeliveryDoneActivity.this, ReserveActivity.class);
            startActivity(intent);

            // Opsionalisht, përfundo aktivitetin aktual
            finish();
        });
    }
}
