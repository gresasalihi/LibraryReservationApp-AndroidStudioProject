package com.example.libraryreservationapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class ReserveActivity extends AppCompatActivity {

    // Objekti DatabaseHelper për të menaxhuar operacionet e bazës së të dhënave SQLite
    DatabaseHelper myDb;

    // Deklarimi i butonave dhe EditText për ndërveprim me përdoruesin
    Button btnInsert, btnUpdate, btnDelete, btnViewAll;
    EditText editTextId, editTextName;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve);

        // Inicializimi i DatabaseHelper
        myDb = new DatabaseHelper(this);

        // Inicializimi i komponenteve të ndërfaqes së përdoruesit
        btnInsert = findViewById(R.id.btn_insert);
        btnUpdate = findViewById(R.id.btn_update);
        btnDelete = findViewById(R.id.btn_delete);
        btnViewAll = findViewById(R.id.btn_view_all);
        editTextId = findViewById(R.id.editText_id);
        editTextName = findViewById(R.id.editText_name);

        // Inicializimi i DrawerLayout, NavigationView dhe Toolbar
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        // Vendosja e Toolbar si ActionBar për aktivitetin
        setSupportActionBar(toolbar);

        // Konfigurimi i toggle për DrawerLayout me një ikonë me vija të zezë
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);

        // Aplikimi i ngjyrës së ikonës
        toggle.getDrawerArrowDrawable().setColor(ContextCompat.getColor(this, android.R.color.black));

        // Bashkangjitja e DrawerListener dhe sinkronizimi i gjendjes
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Sigurimi që ikona e navigimit e Toolbar-it është e zezë
        toolbar.setNavigationIcon(toggle.getDrawerArrowDrawable());

        // Menaxhimi i klikimeve në NavigationView me një lambda
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_about_us) {
                startActivity(new Intent(ReserveActivity.this, AboutActivity.class));
            } else if (id == R.id.nav_reserved) {
                startActivity(new Intent(ReserveActivity.this, ReservedBooksActivity.class));
            } else if (id == R.id.nav_delivery) {
                startActivity(new Intent(ReserveActivity.this, DeliveryActivity.class));
            } else if (id == R.id.nav_logout) {
                startActivity(new Intent(ReserveActivity.this, GetStartedActivity.class));
                finish(); // Mbyllja e aktivitetit ReserveActivity pas daljes
            }
            drawerLayout.closeDrawers(); // Mbyllja e drawer-it pas klikimit të një artikulli
            return true;
        });

        // Shtimi i të dhënave me klikim të butonit
        btnInsert.setOnClickListener(v -> {
            String name = editTextName.getText().toString().trim();
            if (TextUtils.isEmpty(name)) {
                Toast.makeText(ReserveActivity.this, "Please enter a name", Toast.LENGTH_SHORT).show();
                return;
            }
            boolean isInserted = myDb.insertData(name);
            if (isInserted)
                Toast.makeText(ReserveActivity.this, "Book Reserved", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(ReserveActivity.this, "Book not Reserved", Toast.LENGTH_SHORT).show();
        });

        // Përditësimi i të dhënave me klikim të butonit
        btnUpdate.setOnClickListener(v -> {
            String id = editTextId.getText().toString().trim();
            String name = editTextName.getText().toString().trim();
            if (TextUtils.isEmpty(id) || TextUtils.isEmpty(name)) {
                Toast.makeText(ReserveActivity.this, "Please enter ID and Name", Toast.LENGTH_SHORT).show();
                return;
            }
            boolean isUpdated = myDb.updateData(id, name);
            if (isUpdated)
                Toast.makeText(ReserveActivity.this, "Book Updated", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(ReserveActivity.this, "Book not Updated", Toast.LENGTH_SHORT).show();
        });

        // Fshirja e të dhënave me klikim të butonit
        btnDelete.setOnClickListener(v -> {
            String id = editTextId.getText().toString().trim();
            if (TextUtils.isEmpty(id)) {
                Toast.makeText(ReserveActivity.this, "Please enter an ID", Toast.LENGTH_SHORT).show();
                return;
            }
            Integer deletedRows = myDb.deleteData(id);
            if (deletedRows > 0)
                Toast.makeText(ReserveActivity.this, "Book Cancelled", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(ReserveActivity.this, "Book not Cancelled", Toast.LENGTH_SHORT).show();
        });

        // Shfaqja e të gjitha të dhënave me klikim të butonit
        btnViewAll.setOnClickListener(v -> {
            Intent intent = new Intent(ReserveActivity.this, ReservedBooksActivity.class);
            startActivity(intent);
        });
    }
}
