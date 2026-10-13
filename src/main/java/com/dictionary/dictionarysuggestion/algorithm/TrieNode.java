
package com.dictionary.dictionarysuggestion.algorithm;

import java.util.Map;
import java.util.TreeMap;

public class TrieNode {

    private final Map<Character, TrieNode> children;

    private boolean endOfWord;

    public TrieNode() {
        children = new TreeMap<>();
        endOfWord = false;
    }

    public Map<Character, TrieNode> getChildren() {
        return children;
    }

    public boolean isEndOfWord() {
        return endOfWord;
    }

    public void setEndOfWord(boolean endOfWord) {
        this.endOfWord = endOfWord;
    }
}
