
package com.dictionary.dictionarysuggestion.model;

public class Word {

    private final String englishWord;
    private String vietnameseMeaning;

    public Word(String englishWord, String vietnameseMeaning) {
        this.englishWord = englishWord;
        this.vietnameseMeaning = vietnameseMeaning;
    }

    public String getEnglishWord() {
        return englishWord;
    }

    public String getVietnameseMeaning() {
        return vietnameseMeaning;
    }

    public void setVietnameseMeaning(String vietnameseMeaning) {
        this.vietnameseMeaning = vietnameseMeaning;
    }

    @Override
    public String toString() {
        return englishWord + " - " + vietnameseMeaning;
    }
}
