package com.example.testmaxflashcards;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ChooseActivity extends AppCompatActivity {

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

        btnAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cardsIntent = new Intent(v.getContext(), CardsActivity.class);
                cardsIntent.putExtra("selection", "ALL");
                v.getContext().startActivity(cardsIntent);
            }
        });

        btnSN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cardsIntent = new Intent(v.getContext(), CardsActivity.class);
                cardsIntent.putExtra("selection", "SN");
                v.getContext().startActivity(cardsIntent);
            }
        });

        btnIdQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cardsIntent = new Intent(v.getContext(), CardsActivity.class);
                cardsIntent.putExtra("selection", "IDQ");
                v.getContext().startActivity(cardsIntent);
            }
        });
    }
}