package com.example.testmaxflashcards;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yuyakaido.android.cardstackview.CardStackView;

import java.util.ArrayList;

public class CardStackAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final CardStackView cardStackView;
    ArrayList<FlashcardModel> flashcards = new ArrayList<>();

    public CardStackAdapter(CardStackView cardStackView, ArrayList<FlashcardModel> flashcards) {
        this.flashcards = flashcards;
        this.cardStackView = cardStackView;
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
                if(position == flashcards.size()-1) {
                    Intent finishedPlaying = new Intent(v.getContext(), FinishedPlayingActivity.class);
                    // put extras to intent?
                    v.getContext().startActivity(finishedPlaying);
                }
                else {
                    cardStackView.swipe();
                }
            }
        });

        cardViewHolder.btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // dp something with position when its last card!
                if(position == flashcards.size()-1) {
                    Intent finishedPlaying = new Intent(v.getContext(), FinishedPlayingActivity.class);
                    // put extras to intent?
                    v.getContext().startActivity(finishedPlaying);
                }
                else {
                    cardStackView.swipe();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return flashcards.size();
    }
}
