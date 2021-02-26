package com.example.testmaxflashcards;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

public class FinishedPlayingActivity extends AppCompatActivity {
    /**
     * Activity Called after CardsActivity
     *
     * Displays scores on the screen
     *
     * Member Variables:
     * sharedPreferences: SharedPreferences - data storage library in Android to store and access key-value pairs.
     * score: ArrayList<String> - List of Scores of Previous Games.
     *
     */
    private SharedPreferences sharedPreferences;
    private ArrayList<String> scores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finished_playing);

        // bind views to objects
        TextView dispScore = findViewById(R.id.dispScore);
        TextView currentScore = findViewById(R.id.currentScore);
        TextView gameSelection = findViewById(R.id.txtGameSelection);

        // get scores to display
        sharedPreferences = getSharedPreferences("scores", 0);
        Gameplay gameplay = Gameplay.getInstance();
        scores = gameplay.getScores(sharedPreferences);


        // display current game score
        String[] currentGameScore = scores.get(0).split(":");
        currentScore.setText(currentGameScore[1].trim());
        gameSelection.setText(currentGameScore[0].trim());

        // display previous game scores
        String scores_text = "";
        for (String s: scores.subList(1, scores.size())) {
            scores_text += s + "\n\n";
        }

        if (scores_text.length() > 0) {
            dispScore.setText(scores_text);
        }
        else {
            dispScore.setText("NONE");
        }


    }
}