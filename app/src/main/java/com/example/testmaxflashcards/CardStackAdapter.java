package com.example.testmaxflashcards;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yuyakaido.android.cardstackview.CardStackView;

import java.util.ArrayList;

public class CardStackAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final CardStackView cardStackView;
    private final Context appContext;
    private final Gameplay gameplay;
    ArrayList<FlashcardModel> flashcards = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public CardStackAdapter(Context appContext, CardStackView cardStackView, ArrayList<FlashcardModel> flashcards, Gameplay gameplay) {
        this.appContext = appContext;
        this.flashcards = flashcards;
        this.cardStackView = cardStackView;
        this.gameplay = gameplay;
        sharedPreferences = appContext.getSharedPreferences("scores", 0);
        editor = sharedPreferences.edit();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View cardView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        CardViewHolder cardViewHolder = new CardViewHolder(cardView);
        return cardViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CardViewHolder cardViewHolder = (CardViewHolder) holder;
        cardViewHolder.front_text.setText(flashcards.get(position).getFront_text());
        cardViewHolder.back_text.setText(flashcards.get(position).getBack_text());


        cardViewHolder.btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                gameplay.incrementKnow();

                if(position == flashcards.size()-1) {

                    gameplay.recordScore(sharedPreferences, editor);
                    gameplay.endGame();

                    Intent finishedPlaying = new Intent(v.getContext(), FinishedPlayingActivity.class);
                    // put extras to intent?
                    v.getContext().startActivity(finishedPlaying);
                    Activity currentActivity = (Activity) v.getContext();
                    currentActivity.finish();

                }
                else {
                    cardStackView.swipe();

                    // flip easyflipview to front since its being recycled
                    cardViewHolder.easyFlipView.flipTheView();
                }
            }
        });

        cardViewHolder.btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                gameplay.incrementDontKnow();

                // dp something with position when its last card!
                if(position == flashcards.size()-1) {

                    gameplay.recordScore(sharedPreferences, editor);
                    gameplay.endGame();

                    Intent finishedPlaying = new Intent(v.getContext(), FinishedPlayingActivity.class);
                    // put extras to intent?
                    v.getContext().startActivity(finishedPlaying);
                    Activity currentActivity = (Activity) v.getContext();
                    currentActivity.finish();
                }
                else {
                    cardStackView.swipe();

                    // flip easyflipview to front
                    cardViewHolder.easyFlipView.flipTheView();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return flashcards.size();
    }
}
