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

    private final CardStackView cardStackView;
    private final Context appContext;
    private final Gameplay gameplay;
    private final CardStackLayoutManager manager;
    ArrayList<FlashcardModel> flashcards = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    SwipeAnimationSetting swipeRightAnimationSetting, swipeLeftAnimationSetting;

    public CardStackAdapter(Context appContext, CardStackView cardStackView, ArrayList<FlashcardModel> flashcards, Gameplay gameplay, CardStackLayoutManager manager) {
        this.appContext = appContext;
        this.flashcards = flashcards;
        this.cardStackView = cardStackView;
        this.gameplay = gameplay;
        sharedPreferences = appContext.getSharedPreferences("scores", 0);
        editor = sharedPreferences.edit();
        this.manager = manager;

        swipeRightAnimationSetting = new SwipeAnimationSetting.Builder()
                .setDirection(Direction.Right)
                .setDuration(Duration.Normal.duration)
                .setInterpolator(new AccelerateInterpolator())
                .build();

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
        CardViewHolder cardViewHolder = new CardViewHolder(cardView, manager, gameplay);
        return cardViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CardViewHolder cardViewHolder = (CardViewHolder) holder;
        cardViewHolder.front_text.setText(flashcards.get(position).getFront_text());
        cardViewHolder.back_text.setText(Html.fromHtml(flashcards.get(position).getBack_text(), Html.FROM_HTML_MODE_LEGACY));
//        TextView txtKnow = ((View) cardViewHolder.itemView.getParent().getParent()).findViewById(R.id.txtCorrect);
//        TextView txtDontKnow = ((View)cardViewHolder.itemView.getParent().getParent()).findViewById(R.id.txtWrong);
//
        cardViewHolder.btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameplay.incrementKnow();
                //cardViewHolder.txtKnow.setText("" + gameplay.getKnow());

                CardsActivity cardsActivity = (CardsActivity) appContext;
                cardsActivity.setTxtKnow("" + gameplay.getKnow());

                if(position == flashcards.size()-1) {
                    gameplay.recordScore(sharedPreferences, editor);
                    gameplay.endGame();

                    Intent finishedPlaying = new Intent(cardViewHolder.itemView.getContext(), FinishedPlayingActivity.class);
                    // put extras to intent?
                    cardViewHolder.itemView.getContext().startActivity(finishedPlaying);
                    Activity currentActivity = (Activity) cardViewHolder.itemView.getContext();
                    currentActivity.finish();
                }
                else {
                    manager.setSwipeAnimationSetting(swipeRightAnimationSetting);
                    cardStackView.swipe();
                }

            }
        });

        cardViewHolder.btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameplay.incrementDontKnow();
                //cardViewHolder.txtDontKnow.setText("" + gameplay.getDontKnow());

                CardsActivity cardsActivity = (CardsActivity) appContext;
                cardsActivity.setTxtDontKnow("" + gameplay.getDontKnow());

                if(position == flashcards.size()-1) {
                    gameplay.recordScore(sharedPreferences, editor);
                    gameplay.endGame();

                    Intent finishedPlaying = new Intent(cardViewHolder.itemView.getContext(), FinishedPlayingActivity.class);
                    // put extras to intent?
                    cardViewHolder.itemView.getContext().startActivity(finishedPlaying);
                    Activity currentActivity = (Activity) cardViewHolder.itemView.getContext();
                    currentActivity.finish();
                }
                else {
                    manager.setSwipeAnimationSetting(swipeLeftAnimationSetting);
                    cardStackView.swipe();
                }
            }
        });
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
