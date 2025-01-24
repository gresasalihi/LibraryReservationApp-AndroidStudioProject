package com.example.libraryreservationapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); // Vendos layout-in e aktivitetit nga një skedar XML.

        // Deklarimi dhe inicializimi i elementeve të ndërfaqes për username, password dhe butonat.
        EditText usernameEditText = findViewById(R.id.username);
        EditText passwordEditText = findViewById(R.id.password);
        Button loginButton = findViewById(R.id.login_button);
        Button signupButton = findViewById(R.id.signup_button);

        // Vendos veprimin që kryhet kur shtypet butoni i logimit.
        loginButton.setOnClickListener(v -> handleLogin(usernameEditText, passwordEditText));

        // Vendos veprimin që kryhet kur shtypet butoni i regjistrimit.
        signupButton.setOnClickListener(v -> handleSignup());
    }

    // Funksion që përpunon logimin e përdoruesit.
    private void handleLogin(EditText usernameEditText, EditText passwordEditText) {
        // Merr tekstin e futur nga përdoruesi dhe e pastron nga hapsirat boshe.
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // Kontrollon nëse fusha e username dhe password janë mbushur.
        if (username.isEmpty() || password.isEmpty()) {
            // Tregon një mesazh që kërkon mbushjen e të dy fushave.
            Toast.makeText(this, "Please enter both username and password", Toast.LENGTH_SHORT).show();
            return; // Përfundon funksionin nëse kushti është i vërtetë.
        }

        // Përdor një `try-with-resources` për të punuar me bazën e të dhënave.
        try (SQLiteOpenHelper dbHelper = new DatabaseHelper(this);
             SQLiteDatabase database = dbHelper.getWritableDatabase()) {

            // Ekzekuton një pyetje SQL për të marrë përdoruesin me username-in e futur.
            Cursor cursor = database.rawQuery("SELECT * FROM users WHERE username = ?", new String[]{username});

            // Kontrollon nëse ekziston ndonjë përdorues me këtë username.
            if (cursor.getCount() == 0) {
                // Tregon një mesazh që informon përdoruesin që llogaria nuk ekziston.
                showAlert("Account does not exist", "Please sign up first.");
                cursor.close(); // Mbyll cursor-in për të shmangur rrjedhjen e burimeve.
                return;
            }

            // Lëviz tek rreshti i parë i rezultatit të cursor-it.
            cursor.moveToFirst();

            // Merr index-in e kolonës që përmban password-in.
            int passwordColumnIndex = cursor.getColumnIndex("password");
            if (passwordColumnIndex == -1) {
                // Tregon një mesazh gabimi nëse kolona `password` nuk gjendet.
                showAlert("Database Error", "Password column not found.");
                cursor.close();
                return;
            }

            // Merr fjalëkalimin e ruajtur në bazën e të dhënave për këtë username.
            String storedPassword = cursor.getString(passwordColumnIndex);
            cursor.close(); // Mbyll cursor-in pasi nuk është më i nevojshëm.

            // Kontrollon nëse fjalëkalimi i futur përputhet me atë të ruajtur.
            if (storedPassword.equals(password)) {
                // Kalon në aktivitetin e rezervimeve nëse logimi është i suksesshëm.
                Intent intent = new Intent(LoginActivity.this, ReserveActivity.class);
                startActivity(intent);
                finish(); // Mbyll aktivitetin aktual që të mos mund të kthehet mbrapa pa loguar.
            } else {
                // Tregon një mesazh që informon përdoruesin që fjalëkalimi është i pasaktë.
                showAlert("Incorrect password", "Please try again.");
            }
        }
    }

    // Funksion që përpunon regjistrimin e përdoruesit.
    private void handleSignup() {
        // Krijon një intent për të hapur `SignupActivity`.
        Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
        startActivity(intent);
        finish(); // Mbyll aktivitetin aktual për të mos lejuar kthimin në këtë ekran.
    }

    // Funksion që shfaq një dialog për të informuar përdoruesin.
    private void showAlert(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title) // Vendos titullin e dialogut.
                .setMessage(message) // Vendos mesazhin e dialogut.
                .setPositiveButton("OK", null) // Vendos butonin "OK" që mbyll dialogun.
                .show(); // Shfaq dialogun.
    }
}
