package com.example.testmaxflashcards;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Disable Night Mode for Development
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Attach click listener to LEARN NOW button.
        Button btnLearn = findViewById(R.id.btnLearn);
        btnLearn.setOnClickListener(v -> {
            Intent chooseIntent = new Intent(v.getContext(), ChooseActivity.class);
            v.getContext().startActivity(chooseIntent);
            finish();
        });

    }
}