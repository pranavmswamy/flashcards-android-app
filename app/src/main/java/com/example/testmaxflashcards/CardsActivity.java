package com.example.testmaxflashcards;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.wajahatkarim3.easyflipview.EasyFlipView;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.StackFrom;
import com.yuyakaido.android.cardstackview.SwipeableMethod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CardsActivity extends AppCompatActivity {

    CardStackView cardStackView;
    Gameplay gameplay;
    private TextView txtKnow, txtDontKnow;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ConstraintLayout cardsLayout, loadingLayout;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cards);
        cardsLayout = findViewById(R.id.cardsConstraintLayout);
        loadingLayout = findViewById(R.id.loadingConstraintLayout);
        progressBar = findViewById(R.id.progressBar);

        cardsLayout.setVisibility(View.GONE);
        loadingLayout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String selection = (String) bundle.get("selection");
            loadQuestions(selection);
        }

        sharedPreferences = getSharedPreferences("scores", 0);
        editor = sharedPreferences.edit();


        txtKnow = findViewById(R.id.txtCorrect);
        txtDontKnow = findViewById(R.id.txtWrong);
        txtKnow.setText("0");
        txtDontKnow.setText("0");

        gameplay = Gameplay.getInstance();

        cardStackView = findViewById(R.id.cardStackView);

        CardStackLayoutManager manager = new CardStackLayoutManager(getApplicationContext(), new CardStackListener() {
            @Override
            public void onCardDragging(Direction direction, float ratio) {

            }

            @Override
            public void onCardSwiped(Direction direction) {

            }

            @Override
            public void onCardRewound() {

            }

            @Override
            public void onCardCanceled() {

            }

            @Override
            public void onCardAppeared(View view, int position) {

            }

            @Override
            public void onCardDisappeared(View view, int position) {

            }
        });

        manager.setStackFrom(StackFrom.Top);
        manager.setSwipeableMethod(SwipeableMethod.Automatic);
        manager.setDirections(Direction.HORIZONTAL);
        cardStackView.setLayoutManager(manager);
        CardStackAdapter cardStackAdapter = new CardStackAdapter(this, cardStackView, populateDummyFlashcards(), gameplay, manager);
        cardStackView.setAdapter(cardStackAdapter);


    }

    void setTxtKnow(String text) {
        txtKnow.setText(text);
    }

    void setTxtDontKnow(String text) {
        txtDontKnow.setText(text);
    }


    private ArrayList<FlashcardModel> populateDummyFlashcards() {
        ArrayList<FlashcardModel> flashCards = new ArrayList<FlashcardModel>();
        FlashcardModel dummyFlashcard = new FlashcardModel("0", "Dummy", "", "");
        flashCards.add(dummyFlashcard);
        flashCards.add(dummyFlashcard);
        flashCards.add(dummyFlashcard);
        flashCards.add(dummyFlashcard);
        flashCards.add(dummyFlashcard);
        return flashCards;
    }

    private void loadQuestions(String selection) {
        ArrayList<FlashcardModel> allFlashcards = new ArrayList<>();

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://lsatmaxadmin.us/interview/loadDataFC.php";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
            try {
                JSONArray flashcards = new JSONArray(response);
                Log.i("loadQuestions()", "Response Received");
                for(int i=0; i<flashcards.length(); i++) {

                    JSONObject flashcard = flashcards.getJSONObject(i);
                    String id = flashcard.getString("id");
                    String front_text = flashcard.getString("front_text");
                    String back_text = flashcard.getString("back_text");
                    String category = flashcard.getString("category");

                    FlashcardModel flashcardModel = new FlashcardModel(id, category, front_text, back_text);
                    allFlashcards.add(flashcardModel);
                }

                CardStackLayoutManager manager = new CardStackLayoutManager(this, new CardStackListener() {
                    @Override
                    public void onCardDragging(Direction direction, float ratio) {

                    }

                    @Override
                    public void onCardSwiped(Direction direction) {
                    }

                    @Override
                    public void onCardRewound() {

                    }

                    @Override
                    public void onCardCanceled() {

                    }

                    @Override
                    public void onCardAppeared(View view, int position) {
                    }

                    @Override
                    public void onCardDisappeared(View view, int position) {
                    }
                });

                manager.setStackFrom(StackFrom.Top);
                manager.setSwipeableMethod(SwipeableMethod.Automatic);
                manager.setVisibleCount(5);
                manager.setDirections(Direction.HORIZONTAL);
                cardStackView.setLayoutManager(manager);

                Log.d("allF", "" + allFlashcards);
                Log.d("selection", selection);
                CardStackAdapter flashcardAdapter = null;
                if(selection.equals("ALL")) {
                    flashcardAdapter = new CardStackAdapter(this, cardStackView, allFlashcards, gameplay, manager);
                }
                else {
                    ArrayList<FlashcardModel> filtered = getFilteredFlashcards(allFlashcards, selection);
                    flashcardAdapter = new CardStackAdapter(this, cardStackView, filtered, gameplay, manager);
                }

                cardStackView.setAdapter(flashcardAdapter);
                flashcardAdapter.notifyDataSetChanged();

                gameplay.startGame(selection);

                progressBar.setVisibility(View.GONE);
                loadingLayout.setVisibility(View.GONE);
                cardsLayout.setVisibility(View.VISIBLE);


            } catch (JSONException e) {
                Log.e("loadQuestions()", "JSON Parse Error");
                e.printStackTrace();
            }


        }, error -> {
            Log.e("loadQuestions()", "Error Receiving data");
            Log.e("loadQuestions()", error.getMessage());
        });

        queue.add(stringRequest);
    }


    public ArrayList<FlashcardModel> getFilteredFlashcards(ArrayList<FlashcardModel> allFlashcards, String selection) {

        ArrayList<FlashcardModel> filtered = new ArrayList<>();

        if(selection.equals("SN")) {
            // filter for SN
            for(int i=0; i<allFlashcards.size(); i++) {
                if(allFlashcards.get(i).getCategory().equals("Sufficient & Necessary Conditions")) {
                    filtered.add(allFlashcards.get(i));
                }
            }
        }
        else {
            // filter for IDQ
            for(int i=0; i<allFlashcards.size(); i++) {
                if(allFlashcards.get(i).getCategory().equals("Identify the Question Type")) {
                    filtered.add(allFlashcards.get(i));
                }
            }
        }

        Log.d("filtered", ""  + filtered);

        if(filtered.size() > 0) {

            return filtered;
        }
        else {
            // return empty flashcards
            return populateDummyFlashcards();
        }

    }



}