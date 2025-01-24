package com.example.libraryreservationapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class DeliveryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);

        // Inicializimi i DatabaseHelper për të punuar me bazën e të dhënave
        DatabaseHelper myDb = new DatabaseHelper(this);

        // Inicializimi i komponentëve UI (User Interface)
        ImageButton backButton = findViewById(R.id.back_button);
        EditText nameEditText = findViewById(R.id.name);
        EditText cityEditText = findViewById(R.id.city);
        EditText addressEditText = findViewById(R.id.address);
        EditText mobileNumberEditText = findViewById(R.id.mobile_number);
        Button doneButton = findViewById(R.id.done_button);
        ListView reservedBooksListView = findViewById(R.id.reserved_books_listview);

        // Caktimi i tipit të inputit për numrin e telefonit që të pranojë vetëm shifra
        mobileNumberEditText.setInputType(InputType.TYPE_CLASS_PHONE);

        // Ngarkimi i librave të rezervuar në ListView duke përdorur një adapter të personalizuar
        loadReservedBooks(myDb, reservedBooksListView);

        // Menaxhimi i navigimit prapa
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Trajtimi i ngjarjes së shtypjes së butonit "back"
                finish(); // Mbyll aktivitetin aktual
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);

        // Menaxhimi i klikimit të butonit "back"
        backButton.setOnClickListener(v -> callback.handleOnBackPressed());

        // Menaxhimi i klikimit të butonit "done"
        doneButton.setOnClickListener(v -> {
            // Kontrollo nëse të gjitha fushat e kërkuara janë plotësuar
            if (validateInputs(nameEditText, cityEditText, addressEditText, mobileNumberEditText)) {
                // Shfaq dialogun e konfirmimit
                showConfirmationDialog();
            }
        });
    }

    // Metodë për të kontrolluar nëse fushat e inputit janë të plotësuara
    private boolean validateInputs(EditText nameEditText, EditText cityEditText, EditText addressEditText, EditText mobileNumberEditText) {
        // Kontrollo nëse ndonjë nga fushat e kërkuara është e zbrazët
        if (nameEditText.getText().toString().trim().isEmpty() ||
                cityEditText.getText().toString().trim().isEmpty() ||
                addressEditText.getText().toString().trim().isEmpty() ||
                mobileNumberEditText.getText().toString().trim().isEmpty()) {

            // Shfaq mesazhin që të gjitha fushat duhet të plotësohen
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    // Metodë për të ngarkuar librat e rezervuar në ListView
    private void loadReservedBooks(DatabaseHelper myDb, ListView reservedBooksListView) {
        List<String> reservedBooks = myDb.getReservedBooks(); // Merr librat e rezervuar nga baza e të dhënave
        ReservedBooksAdapter adapter = new ReservedBooksAdapter(this, reservedBooks); // Krijo adapterin për librat
        reservedBooksListView.setAdapter(adapter); // Vendos adapterin në ListView
    }

    // Metodë për të shfaqur një dialog konfirmimi për dorëzimin e librave
    private void showConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Confirm Delivery") // Cakto titullin e dialogut
                .setMessage("Are you sure you want your books to be delivered?") // Cakto mesazhin e dialogut
                .setPositiveButton("Yes", (dialog, which) -> {
                    // Nëse përdoruesi zgjedh "Po", kalon në aktivitetin tjetër
                    Intent intent = new Intent(DeliveryActivity.this, DeliveryDoneActivity.class);
                    startActivity(intent);
                    finish(); // Mbyll aktivitetin aktual
                })
                .setNegativeButton("No", null) // Nëse përdoruesi zgjedh "Jo", nuk bën asgjë
                .show(); // Shfaq dialogun
    }
}
