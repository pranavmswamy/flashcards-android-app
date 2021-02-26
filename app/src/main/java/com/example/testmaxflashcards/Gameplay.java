package com.example.testmaxflashcards;

import android.content.SharedPreferences;

import java.util.ArrayList;

public class Gameplay {
    /**
     * Singleton Class to keep track of each session of training with flashcards.
     *
     * Member Variables:
     * singleton: Gameplay - singleton object
     * know: int - count of cards gotten right
     * dontKnow: int - count of cards gotten wrong
     * selection: String - current category selection being played (ALL, IDQ or SN)
     */

    private static Gameplay singleton;
    private int know = 0;
    private int dontKnow = 0;
    private String selection;

    public static Gameplay getInstance() {
        if(singleton == null) {
            singleton = new Gameplay();
        }
        return singleton;
    }

    public void startGame(String selection) {
        this.selection = selection;
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

    /**
     * Writes current score at the end of a session to disk.
     * @param sharedPreferences object of Key-Value Read/Write library for Android.
     * @param editor Editor object to write key-value pair to permanent storage.
     */
    public void recordScore(SharedPreferences sharedPreferences, SharedPreferences.Editor editor) {
        int i = 0;
        while(sharedPreferences.contains("score" + i)) {
            i++;
        }

        String gameSelection = "-";
        if (selection.equals("IDQ")) {
            gameSelection = "IDENTIFY THE QUESTION TYPE";
        }
        else if (selection.equals("SN")) {
            gameSelection = "SUFFICIENT & NECESSARY CONDITIONS";
        }
        else {
            gameSelection = "ALL";
        }

        // record score as key-value pair
        editor.putString("score" + i, gameSelection + ": " + know + "/" + (know + dontKnow));
        editor.putInt("last_score_idx", i);
        editor.commit();
    }

    /**
     *
     * @param sharedPreferences object of Key-Value Read/Write library for Android.
     * @return Scores of last 5 games (or less).
     */
    public ArrayList<String> getScores(SharedPreferences sharedPreferences) {
        ArrayList<String> scores = new ArrayList<>();

        int last_score_idx = sharedPreferences.getInt("last_score_idx", -1);
        if(last_score_idx < 0) {
            return scores;
        }

        int limit = 6;
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
