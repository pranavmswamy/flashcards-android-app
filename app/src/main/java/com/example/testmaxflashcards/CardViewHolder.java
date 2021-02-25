package com.example.testmaxflashcards;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wajahatkarim3.easyflipview.EasyFlipView;

public class CardViewHolder extends RecyclerView.ViewHolder {

    EasyFlipView easyFlipView;
    TextView front_text, back_text;
    Button btnYes, btnNo, btnFlip;

    public CardViewHolder(@NonNull View itemView) {
        super(itemView);
        easyFlipView = itemView.findViewById(R.id.easyFlipView);
        front_text = easyFlipView.findViewById(R.id.frontText);
        back_text = easyFlipView.findViewById(R.id.backText);
        btnNo = easyFlipView.findViewById(R.id.btnNo);
        btnYes = easyFlipView.findViewById(R.id.btnYes);
        btnFlip = easyFlipView.findViewById(R.id.btnFlip);

        easyFlipView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                easyFlipView.flipTheView();
                Log.e("Flipping the view", "flipping the view");
            }
        });

//        btnFlip.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                easyFlipView.flipTheView();
//                Log.e("Flipping the view", "flipping the view");
//            }
//        });

    }

}
