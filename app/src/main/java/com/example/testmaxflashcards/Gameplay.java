package com.example.testmaxflashcards;

import android.content.SharedPreferences;

import java.util.ArrayList;

public class Gameplay {
    private static Gameplay singleton;
    private int know = 0;
    private int dontKnow = 0;

    public static Gameplay getInstance() {
        if(singleton == null) {
            singleton = new Gameplay();
        }
        return singleton;
    }

    public void startGame() {
        know = 0;
        dontKnow = 0;
    }

    public void endGame() {
        know = 0;
        dontKnow = 0;
    }

    public void incrementKnow() {
        know++;
    }

    public void incrementDontKnow() {
        dontKnow++;
    }

    public int getKnow() {
        return know;
    }

    public int getDontKnow() {
        return dontKnow;
    }

    public void recordScore(SharedPreferences sharedPreferences, SharedPreferences.Editor editor) {
        int i = 0;
        while(sharedPreferences.contains("score" + i)) {
            i++;
        }

        editor.putString("score" + i, know + "/" + (know + dontKnow));
        editor.putInt("last_score_idx", i);
        editor.commit();
    }

    public ArrayList<String> getScores(SharedPreferences sharedPreferences) {
        ArrayList<String> scores = new ArrayList<>();

        int last_score_idx = sharedPreferences.getInt("last_score_idx", -1);
        if(last_score_idx < 0) {
            return scores;
        }

        int limit = 5;
        for(int i = last_score_idx; i>= 0; i--) {
            String score = sharedPreferences.getString("score" + i, "N/A");
            if (score != "N/A") {
                scores.add(score);
            }
            limit--;
            if(limit == 0) {
                break;
            }
        }

        return  scores;
    }

}
