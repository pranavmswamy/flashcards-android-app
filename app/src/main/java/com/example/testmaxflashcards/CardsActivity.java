package com.example.testmaxflashcards;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
    Gameplay gameplay = Gameplay.getInstance();
    TextView txtKnow, txtDontKnow;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // setContentView loading image

        // collect from intent,
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String selection = (String) bundle.get("selection");
            loadQuestions(selection);
        }

        sharedPreferences = getSharedPreferences("scores", 0);
        editor = sharedPreferences.edit();

        setContentView(R.layout.activity_cards);

        txtKnow = findViewById(R.id.txtCorrect);
        txtDontKnow = findViewById(R.id.txtWrong);

        txtKnow.setText("0");
        txtDontKnow.setText("0");

        cardStackView = findViewById(R.id.cardStackView);
        CardStackLayoutManager manager = new CardStackLayoutManager(getApplicationContext(), new CardStackListener() {
            @Override
            public void onCardDragging(Direction direction, float ratio) {

            }

            @Override
            public void onCardSwiped(Direction direction) {
                if(direction == Direction.Left) {
                    gameplay.incrementDontKnow();
                    txtDontKnow.setText("" + gameplay.getDontKnow());

                }
                else if(direction == Direction.Right) {
                    gameplay.incrementKnow();
                    txtKnow.setText("" + gameplay.getKnow());
                }

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
                Log.e("adfa", "" + position);
            }
        });

        manager.setStackFrom(StackFrom.Top);
        manager.setSwipeThreshold(0.5f);
        manager.setSwipeableMethod(SwipeableMethod.Manual);
        manager.setDirections(Direction.HORIZONTAL);
        manager.setCanScrollHorizontal(false);
        manager.setCanScrollVertical(false);
        cardStackView.setLayoutManager(manager);
        CardStackAdapter cardStackAdapter = new CardStackAdapter(getApplicationContext(), cardStackView, populateFlashcards(), gameplay, manager);
        cardStackView.setAdapter(cardStackAdapter);


    }


    private ArrayList<FlashcardModel> populateFlashcards() {
        ArrayList<FlashcardModel> flashCards = new ArrayList<FlashcardModel>();
        flashCards.add(new FlashcardModel("10", "A", "", ""));
        flashCards.add(new FlashcardModel("11", "B", "", ""));
        flashCards.add(new FlashcardModel("12", "A", "", ""));
        return flashCards;
    }

    private void loadQuestions(String selection) {
        ArrayList<FlashcardModel> allFlashcards = new ArrayList<>();

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://lsatmaxadmin.us/interview/loadDataFC.php";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
            try {
                JSONArray flashcards = new JSONArray(response);
                Log.e("sdzsbdskh", "response received");
                for(int i=0; i<flashcards.length(); i++) {

                    JSONObject flashcard = flashcards.getJSONObject(i);
                    String id = flashcard.getString("id");
                    String front_text = flashcard.getString("front_text");
                    String back_text = flashcard.getString("back_text");
                    String category = flashcard.getString("category");

                    //Log.e("ssfsf", id + " " + front_text);

                    FlashcardModel flashcardModel = new FlashcardModel(id, category, front_text, back_text);
                    allFlashcards.add(flashcardModel);
                }

                CardStackLayoutManager manager = new CardStackLayoutManager(getApplicationContext(), new CardStackListener() {
                    @Override
                    public void onCardDragging(Direction direction, float ratio) {

                    }

                    @Override
                    public void onCardSwiped(Direction direction) {
                        if(direction == Direction.Left) {
                            gameplay.incrementDontKnow();
                            txtDontKnow.setText("" + gameplay.getDontKnow());

                        }
                        else if(direction == Direction.Right) {
                            gameplay.incrementKnow();
                            txtKnow.setText("" + gameplay.getKnow());
                        }

                    }

                    @Override
                    public void onCardRewound() {

                    }

                    @Override
                    public void onCardCanceled() {

                    }

                    @Override
                    public void onCardAppeared(View view, int position) {
                        ConstraintLayout constraintLayout = (ConstraintLayout) view;
                        EasyFlipView easyFlipView = (EasyFlipView) ((ConstraintLayout) view).getChildAt(0);
                        if(easyFlipView.isFrontSide()) {
                            Log.e("front view", "aefadf");
                        }
                        else if(easyFlipView.isBackSide()) {
                            Log.e("back view", "aefadf");
                        }
                    }

                    @Override
                    public void onCardDisappeared(View view, int position) {
                        Log.e("adfa", "" + position);
                        if(position == allFlashcards.size()-1) {
                            gameplay.recordScore(sharedPreferences, editor);
                            gameplay.endGame();

                            Intent finishedPlaying = new Intent(view.getContext(), FinishedPlayingActivity.class);
                            // put extras to intent?
                            view.getContext().startActivity(finishedPlaying);
                            Activity currentActivity = (Activity) view.getContext();
                            currentActivity.finish();
                        }
                    }
                });

                manager.setStackFrom(StackFrom.Top);
                manager.setSwipeThreshold(0.5f);
                manager.setSwipeableMethod(SwipeableMethod.Manual);
                manager.setDirections(Direction.HORIZONTAL);
//                manager.setCanScrollHorizontal(false);
//                manager.setCanScrollVertical(false);
                cardStackView.setLayoutManager(manager);

                CardStackAdapter cardStackAdapter2 = new CardStackAdapter(this, cardStackView, allFlashcards, gameplay, manager);
                cardStackView.setAdapter(cardStackAdapter2);
                cardStackAdapter2.notifyDataSetChanged();

                gameplay.startGame();

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }, error -> {
            Log.e("sfsgsgs", "Error");
            Log.e("Sdffss", error.getMessage());
        });
        queue.add(stringRequest);

        Log.e("Ssfs", "request added to queue");
    }
}