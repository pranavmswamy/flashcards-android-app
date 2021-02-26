package com.example.testmaxflashcards;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

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
    /**
     * CardsActivity - called from ChooseActivity
     * This activity is used to display flashcards.
     *
     * Member Variables:
     *
     * cardStackView: CardStackView - Stack of Cards View for flashcards
     * gamePlay: Gameplay - singleton object to keep track of the score of the game.
     * txtKnow: TextView - TextView to display number of correctly guessed flashcards on screen.
     * txtDontKnow: TextView - TextView to display number of incorrectly guessed flashcards on screen.*
     * cardsLayout: ConstraintLayout - Layout holding flashcards.
     * loadingLayout: ConstraintLayout - Layout holding progress bar.
     * progressBar: ProgressBar - Progress bar to display on screen when data is loading.
     *
     */

    private CardStackView cardStackView;
    private Gameplay gameplay;
    private TextView txtKnow, txtDontKnow;
    private ConstraintLayout cardsLayout, loadingLayout;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards);
        cardsLayout = findViewById(R.id.cardsConstraintLayout);
        loadingLayout = findViewById(R.id.loadingConstraintLayout);
        progressBar = findViewById(R.id.progressBar);
        txtKnow = findViewById(R.id.txtCorrect);
        txtDontKnow = findViewById(R.id.txtWrong);
        txtKnow.setText("0");
        txtDontKnow.setText("0");
        cardStackView = findViewById(R.id.cardStackView);

        // hide cards layout in the beginning and show loading layout.
        cardsLayout.setVisibility(View.GONE);
        loadingLayout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        // get selection from Intent.
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String selection = (String) bundle.get("selection");
            loadQuestions(selection);
        }

        // get Gameplay singleton instance.
        gameplay = Gameplay.getInstance();


    }

    void setTxtKnow(String text) {
        txtKnow.setText(text);
    }

    void setTxtDontKnow(String text) {
        txtDontKnow.setText(text);
    }

    /**
     * Method to populate dummy flashcards in case of an error in loading flashcards.
     */
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

    /**
     * Method to send GET request to server,
     * load data from server,
     * display flashcards,
     * start learning.
     *
     * @param selection specifies one of the three selections (ALL, IDQ, or SN)
     */
    private void loadQuestions(String selection) {
        ArrayList<FlashcardModel> allFlashcards = new ArrayList<>();

        // Create GET request.
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

                // Populate Layout on screen.
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

                // Filter cards according to selection
                CardStackAdapter flashcardAdapter = null;
                if(selection.equals("ALL")) {
                    flashcardAdapter = new CardStackAdapter(this, cardStackView, allFlashcards, gameplay, manager);
                }
                else {
                    ArrayList<FlashcardModel> filtered = getFilteredFlashcards(allFlashcards, selection);
                    flashcardAdapter = new CardStackAdapter(this, cardStackView, filtered, gameplay, manager);
                }

                // Populate cards on screen, and display.
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

        // Add GET request to Volley request queue.
        queue.add(stringRequest);
    }

    /**
     * Helper method to filter flashcards based on selection.
     * @param allFlashcards Arraylist<FlashcardModel> of all flashcards.
     * @param selection String parameter- one of ALL, SN or IDQ.
     * @return Filtered list of ArrayList<FlashcardModel> flashcards.
     */
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

        if(filtered.size() > 0) {
            return filtered;
        }
        else {
            // return empty flashcards
            return populateDummyFlashcards();
        }
    }

}