package com.example.testmaxflashcards;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ChooseActivity extends AppCompatActivity {
    /**
     * Choose Activity - called from MainActivity.
     *
     * Member Variables:
     * btnAll - Button for choosing ALL flashcards
     * btnSN - Button for choosing SUFFICIENT & NECESSARY CONDITIONS flashcards
     * btnIdQ - Button for choosing IDENTIFY THE QUESTION TYPE flashcards
     *
     */
    private Button btnAll;
    private Button btnSN;
    private Button btnIdQ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);

        btnAll = findViewById(R.id.btnAll);
        btnSN = findViewById(R.id.btnSN);
        btnIdQ = findViewById(R.id.btnIdQ);

        /**
         * Code for Categories -
         * ALL - 'ALL'
         * SUFFICIENT & NECESSARY CONDITIONS - 'SN'
         * IDENTIFY THE QUESTION TYPE - 'IDQ'
         */


        // Attach onclick listener to ALL button
        btnAll.setOnClickListener(v -> {
            Intent cardsIntent = new Intent(v.getContext(), CardsActivity.class);
            cardsIntent.putExtra("selection", "ALL");
            v.getContext().startActivity(cardsIntent);
        });

        // Attach onclick listener to SUFFICIENT & NECESSARY CONDITIONS button
        btnSN.setOnClickListener(v -> {
            Intent cardsIntent = new Intent(v.getContext(), CardsActivity.class);
            cardsIntent.putExtra("selection", "SN");
            v.getContext().startActivity(cardsIntent);
        });

        // Attach onclick listener to IDENTIFY THE QUESTION TYPE button
        btnIdQ.setOnClickListener(v -> {
            Intent cardsIntent = new Intent(v.getContext(), CardsActivity.class);
            cardsIntent.putExtra("selection", "IDQ");
            v.getContext().startActivity(cardsIntent);
        });
    }
}