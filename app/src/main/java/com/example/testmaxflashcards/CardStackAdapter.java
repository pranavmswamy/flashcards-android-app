package com.example.testmaxflashcards;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackView;

import java.util.ArrayList;

public class CardStackAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final CardStackView cardStackView;
    private final Context appContext;
    private final Gameplay gameplay;
    private final CardStackLayoutManager manager;
    ArrayList<FlashcardModel> flashcards = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public CardStackAdapter(Context appContext, CardStackView cardStackView, ArrayList<FlashcardModel> flashcards, Gameplay gameplay, CardStackLayoutManager manager) {
        this.appContext = appContext;
        this.flashcards = flashcards;
        this.cardStackView = cardStackView;
        this.gameplay = gameplay;
        sharedPreferences = appContext.getSharedPreferences("scores", 0);
        editor = sharedPreferences.edit();
        this.manager = manager;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View cardView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        CardViewHolder cardViewHolder = new CardViewHolder(cardView, manager);
        return cardViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CardViewHolder cardViewHolder = (CardViewHolder) holder;
        cardViewHolder.front_text.setText(flashcards.get(position).getFront_text());
        cardViewHolder.back_text.setText(Html.fromHtml(flashcards.get(position).getBack_text(), Html.FROM_HTML_MODE_LEGACY));
    }

    @Override
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
        CardViewHolder cardViewHolder = (CardViewHolder) holder;
        // flip easyflipview to front since its being recycled
        cardViewHolder.easyFlipView.setFlipEnabled(true);
        cardViewHolder.easyFlipView.flipTheView();
    }

    @Override
    public int getItemCount() {
        return flashcards.size();
    }
}
