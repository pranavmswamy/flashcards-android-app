package com.example.testmaxflashcards;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wajahatkarim3.easyflipview.EasyFlipView;

public class CardViewHolder extends RecyclerView.ViewHolder {
    /**
     *
     * View Holder for CardStackView.
     *
     * Contains elements present in the card view.
     * easyFlipView: EasyFlipView - Flippable Card View from https://github.com/wajahatkarim3/EasyFlipView
     * btnYes: Button - Button to click if the student knows the answer
     * btnNo: Button - Button to click of the student does not know the answer.
     *
     */
    private EasyFlipView easyFlipView;
    private TextView front_text, back_text;
    private Button btnYes, btnNo;

    public EasyFlipView getEasyFlipView() {
        return easyFlipView;
    }

    public TextView getFront_text() {
        return front_text;
    }

    public TextView getBack_text() {
        return back_text;
    }

    public Button getBtnYes() {
        return btnYes;
    }

    public Button getBtnNo() {
        return btnNo;
    }

    public CardViewHolder(@NonNull View itemView) {
        super(itemView);
        easyFlipView = itemView.findViewById(R.id.easyFlipView);
        front_text = easyFlipView.findViewById(R.id.frontText);
        back_text = easyFlipView.findViewById(R.id.backText);
        btnNo = easyFlipView.findViewById(R.id.btnNo);
        btnYes = easyFlipView.findViewById(R.id.btnYes);

        easyFlipView.setOnClickListener(v -> {
            easyFlipView.flipTheView();
            //Log.e("Flipping the view", "flipping the view");
            easyFlipView.setFlipEnabled(false);
        });
    }

}
