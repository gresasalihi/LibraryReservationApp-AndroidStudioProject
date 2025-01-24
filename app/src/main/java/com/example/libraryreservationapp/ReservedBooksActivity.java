package com.example.libraryreservationapp;

import android.content.Intent; // Importimi i klasës Intent për të filluar aktivitete të reja.
import android.database.Cursor; // Importimi i klasës Cursor për të lëvizur nëpër të dhënat e bazës së të dhënave.
import android.os.Bundle; // Importimi i klasës Bundle për menaxhimin e gjendjes së aktivitetit.
import android.widget.ArrayAdapter; // Importimi i klasës ArrayAdapter për lidhjen e të dhënave me ListView.
import android.widget.ImageButton; // Importimi i klasës ImageButton për butonat me imazhe.
import android.widget.ListView; // Importimi i klasës ListView për shfaqjen e një liste me artikuj.
import android.widget.Toast; // Importimi i klasës Toast për mesazhet e shkurtra të ekranit.

import androidx.appcompat.app.AppCompatActivity; // Importimi i klasës AppCompatActivity për të krijuar aktivitete që janë në përputhje me versionet e vjetra të Android-it.

import java.util.ArrayList; // Importimi i klasës ArrayList për krijimin e listave dinamike.

public class ReservedBooksActivity extends AppCompatActivity {

    // Deklarimi i objekteve të përdorura në këtë aktivitet.
    DatabaseHelper myDb; // Objekt për menaxhimin e bazës së të dhënave.
    ListView listViewData; // Objekt për shfaqjen e të dhënave në formë liste.
    ImageButton backButton; // Buton për kthim mbrapa në aktivitetin e mëparshëm.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // Thirrja e metodës super për të krijuar aktivitetin.
        setContentView(R.layout.activity_reserved_books); // Vendosja e layout-it për këtë aktivitet.

        myDb = new DatabaseHelper(this); // Inicializimi i objektit të bazës së të dhënave.
        listViewData = findViewById(R.id.listView_data); // Gjetja e ListView në layout.
        backButton = findViewById(R.id.back_button); // Gjetja e ImageButton për kthim mbrapa.

        loadData(); // Thirrja e metodës për të ngarkuar të dhënat nga baza e të dhënave.

        backButton.setOnClickListener(v -> {
            // Definimi i veprimit kur shtypet butoni për kthim mbrapa.
            Intent intent = new Intent(ReservedBooksActivity.this, ReserveActivity.class);
            startActivity(intent); // Nisja e aktivitetit të ri.
        });
    }

    private void loadData() {
        // Metoda për të ngarkuar të dhënat nga baza e të dhënave.
        Cursor cursor = myDb.getAllData(); // Kërkimi i të gjitha të dhënave nga baza e të dhënave.
        if (cursor.getCount() == 0) {
            // Nëse nuk gjenden të dhëna, tregohet një mesazh.
            Toast.makeText(ReservedBooksActivity.this, "No Data Found", Toast.LENGTH_SHORT).show();
            return; // Kthimi nga metoda nëse nuk ka të dhëna.
        }

        ArrayList<String> listData = new ArrayList<>(); // Krijimi i një liste për të ruajtur të dhënat.
        while (cursor.moveToNext()) {
            // Lëvizja nëpër të gjitha rreshtat e të dhënave dhe shtimi i tyre në listë.
            listData.add("ID: " + cursor.getString(0) + "\nBook name: " + cursor.getString(1));
        }

        // Krijimi i një adapteri për të lidhur listën e të dhënave me ListView.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listData);
        listViewData.setAdapter(adapter); // Vendosja e adapterit në ListView.
    }
}
