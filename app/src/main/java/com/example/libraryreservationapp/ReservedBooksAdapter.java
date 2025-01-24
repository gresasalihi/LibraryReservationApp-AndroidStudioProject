package com.example.libraryreservationapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.List;

// Krijo një adapter të personalizuar për të shfaqur librat e rezervuar në një ListView
public class ReservedBooksAdapter extends ArrayAdapter<String> {
    private final Context context; // Konteksti i aktivitetit ose fragmentit
    private final List<String> reservedBooks; // Lista e librave të rezervuar

    // Konstruktor për adapterin, inicializon kontekstin dhe listën e librave të rezervuar
    public ReservedBooksAdapter(@NonNull Context context, @NonNull List<String> reservedBooks) {
        super(context, R.layout.list_item_reserved_book, reservedBooks);
        this.context = context;
        this.reservedBooks = reservedBooks;
    }

    @Override
    @NonNull
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Merr pamjen ekzistuese, ose krijon një të re nëse është e nevojshme
        View view = convertView;
        if (view == null) {
            // Inflates pamjen e re duke përdorur LayoutInflater
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item_reserved_book, parent, false);
        }

        // Merr TextView për emrin e librit dhe vendos tekstin nga lista e librave të rezervuar
        TextView bookNameTextView = view.findViewById(R.id.book_name_textview);
        bookNameTextView.setText(reservedBooks.get(position));

        // Kthen pamjen e krijuar ose të ripërdorur
        return view;
    }
}
