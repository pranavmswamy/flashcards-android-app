package com.example.testmaxflashcards;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wajahatkarim3.easyflipview.EasyFlipView;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;

public class CardViewHolder extends RecyclerView.ViewHolder {

    EasyFlipView easyFlipView;
    Gameplay gameplay;
    TextView front_text, back_text, txtKnow, txtDontKnow;
    Button btnYes, btnNo;

    public CardViewHolder(@NonNull View itemView, CardStackLayoutManager manager, Gameplay gameplay) {
        super(itemView);
        easyFlipView = itemView.findViewById(R.id.easyFlipView);
        this.gameplay = gameplay;
        front_text = easyFlipView.findViewById(R.id.frontText);
        back_text = easyFlipView.findViewById(R.id.backText);
        btnNo = easyFlipView.findViewById(R.id.btnNo);
        btnYes = easyFlipView.findViewById(R.id.btnYes);
//
//        txtKnow = easyFlipView.findViewById(R.id.txtCorrect);
//        txtDontKnow = easyFlipView.findViewById(R.id.txtWrong);


        easyFlipView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                easyFlipView.flipTheView();

                Log.e("Flipping the view", "flipping the view");
                easyFlipView.setFlipEnabled(false);
            }
        });




    }

}
