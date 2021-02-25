package com.example.testmaxflashcards;

public class FlashcardModel {

    private String id;
    private String category;
    private String front_text;
    private String back_text;

    public FlashcardModel(String id, String category, String front_text, String back_text) {
        this.id = id;
        this.category = category;
        this.front_text = front_text;
        this.back_text = back_text;
    }

    public String getCategory() {
        return category;
    }

    public String getFront_text() {
        return front_text;
    }

    public String getBack_text() {
        return back_text;
    }
}
