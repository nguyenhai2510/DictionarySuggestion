
package com.dictionary.dictionarysuggestion.algorithm;

public class TrieDemo {

    public static void main(String[] args) {

        Trie trie = new Trie();

        trie.insert("apple");
        trie.insert("application");
        trie.insert("apply");
        trie.insert("banana");
        trie.insert("book");

        System.out.println("Search apple: "
                + trie.contains("apple"));

        System.out.println("Search app: "
                + trie.contains("app"));

        System.out.println("Suggestions for app: "
                + trie.suggest("app", 10));

        trie.remove("apple");

        System.out.println("After deleting apple: "
                + trie.suggest("app", 10));
    }
}
