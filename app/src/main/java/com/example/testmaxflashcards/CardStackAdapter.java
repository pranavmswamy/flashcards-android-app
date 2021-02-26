package com.example.testmaxflashcards;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.Duration;
import com.yuyakaido.android.cardstackview.SwipeAnimationSetting;

import java.util.ArrayList;

public class CardStackAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /**
     * Adapter for the flashcards CardStackView
     *
     * Member Variables:
     * cardStackView: CardStackView - Card Stack View from https://github.com/yuyakaido/CardStackView
     * appContext: Context - Context of application, used to access CardsActivity
     * gameplay: Gameplay - singleton object to keep track of learning scores.
     * manager: CardStackLayoutManager - Layout manager for card stack
     * flashcards: ArrayList<FlashcardModel> - Adapter Arraylist for flashcards.
     * sharedPreferences: SharedPreferences - data storage library to access and store scores.
     * editor: Editor - data editor for Shared Preferences
     * swipeRightAnimationSetting, swipeLeftAnimationSetting - Animation for card swipe automatically to left and right.
     *
     */

    private final CardStackView cardStackView;
    private final Context appContext;
    private final Gameplay gameplay;
    private final CardStackLayoutManager manager;
    private ArrayList<FlashcardModel> flashcards;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private SwipeAnimationSetting swipeRightAnimationSetting, swipeLeftAnimationSetting;

    public CardStackAdapter(Context appContext, CardStackView cardStackView, ArrayList<FlashcardModel> flashcards, Gameplay gameplay, CardStackLayoutManager manager) {
        this.appContext = appContext;
        this.flashcards = flashcards;
        this.cardStackView = cardStackView;
        this.gameplay = gameplay;
        sharedPreferences = appContext.getSharedPreferences("scores", 0);
        editor = sharedPreferences.edit();
        this.manager = manager;

        // create swipe right animation
        swipeRightAnimationSetting = new SwipeAnimationSetting.Builder()
                .setDirection(Direction.Right)
                .setDuration(Duration.Normal.duration)
                .setInterpolator(new AccelerateInterpolator())
                .build();

        // create swipe left animation
        swipeLeftAnimationSetting = new SwipeAnimationSetting.Builder()
                .setDirection(Direction.Left)
                .setDuration(Duration.Normal.duration)
                .setInterpolator(new AccelerateInterpolator())
                .build();

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
        cardViewHolder.getFront_text().setText(flashcards.get(position).getFront_text());
        cardViewHolder.getBack_text().setText(Html.fromHtml(flashcards.get(position).getBack_text(), Html.FROM_HTML_MODE_LEGACY));

        cardViewHolder.getBtnYes().setOnClickListener(v -> {
            // increment KNOW score
            gameplay.incrementKnow();
            CardsActivity cardsActivity = (CardsActivity) appContext;
            cardsActivity.setTxtKnow("" + gameplay.getKnow());

            // if last card, finish game.
            if(position == flashcards.size()-1) {
                // record score
                gameplay.recordScore(sharedPreferences, editor);
                gameplay.endGame();

                Intent finishedPlaying = new Intent(cardViewHolder.itemView.getContext(), FinishedPlayingActivity.class);
                cardViewHolder.itemView.getContext().startActivity(finishedPlaying);
                Activity currentActivity = (Activity) cardViewHolder.itemView.getContext();
                currentActivity.finish();
            }
            else {
                // if not last card, continue to swipe and show next card.
                // NOTE - manual swiping is disabled. Swipes are done through button click.
                manager.setSwipeAnimationSetting(swipeRightAnimationSetting);
                cardStackView.swipe();
            }

        });

        cardViewHolder.getBtnNo().setOnClickListener(v -> {
            // increment DONT KNOW score
            gameplay.incrementDontKnow();
            CardsActivity cardsActivity = (CardsActivity) appContext;
            cardsActivity.setTxtDontKnow("" + gameplay.getDontKnow());

            // if last card, finish game.
            if(position == flashcards.size()-1) {
                gameplay.recordScore(sharedPreferences, editor);
                gameplay.endGame();

                Intent finishedPlaying = new Intent(cardViewHolder.itemView.getContext(), FinishedPlayingActivity.class);
                cardViewHolder.itemView.getContext().startActivity(finishedPlaying);
                Activity currentActivity = (Activity) cardViewHolder.itemView.getContext();
                currentActivity.finish();
            }
            else {
                // if not last card, continue to swipe and show next card.
                // NOTE - manual swiping is disabled. Swipes are done through button click.
                manager.setSwipeAnimationSetting(swipeLeftAnimationSetting);
                cardStackView.swipe();
            }
        });
    }

    @Override
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
        CardViewHolder cardViewHolder = (CardViewHolder) holder;

        // flip easyflipview to front since its being recycled
        cardViewHolder.getEasyFlipView().setFlipEnabled(true);
        cardViewHolder.getEasyFlipView().flipTheView();
    }

    @Override
    public int getItemCount() {
        return flashcards.size();
    }
}
