package com.example.testmaxflashcards;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

public class FinishedPlayingActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    ArrayList<String> scores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finished_playing);

        sharedPreferences = getSharedPreferences("scores", 0);
        editor = sharedPreferences.edit();

        Gameplay gameplay = Gameplay.getInstance();
        scores = gameplay.getScores(sharedPreferences);

        TextView dispScore = findViewById(R.id.dispScore);
        TextView currentScore = findViewById(R.id.currentScore);
        TextView gameSelection = findViewById(R.id.txtGameSelection);

        String[] currentGameScore = scores.get(0).split(":");
        currentScore.setText(currentGameScore[1].trim());
        gameSelection.setText(currentGameScore[0].trim());

        String scores_text = "";
        for (String s: scores.subList(1, scores.size())) {
            scores_text += s + "\n\n";
        }

        dispScore.setText(scores_text);
    }
}