
package com.dictionary.dictionarysuggestion.algorithm;

import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TrieTest {

    @Test
    void testInsertAndSearch() {
        Trie trie = new Trie();

        trie.insert("apple");

        assertTrue(trie.contains("apple"));
        assertFalse(trie.contains("app"));
        assertFalse(trie.contains("banana"));
    }

    @Test
    void testPrefixSuggestion() {
        Trie trie = new Trie();

        trie.insert("apple");
        trie.insert("application");
        trie.insert("apply");
        trie.insert("banana");

        List<String> result = trie.suggest("app", 10);

        assertEquals(
                List.of("apple", "application", "apply"),
                result
        );
    }

    @Test
    void testSuggestionLimit() {
        Trie trie = new Trie();

        trie.insert("apple");
        trie.insert("application");
        trie.insert("apply");

        assertEquals(2, trie.suggest("app", 2).size());
    }

    @Test
    void testDeleteWord() {
        Trie trie = new Trie();

        trie.insert("app");
        trie.insert("apple");

        assertTrue(trie.remove("app"));
        assertFalse(trie.contains("app"));
        assertTrue(trie.contains("apple"));
    }

    @Test
    void testDeleteNonExistingWord() {
        Trie trie = new Trie();

        trie.insert("apple");

        assertFalse(trie.remove("banana"));
        assertTrue(trie.contains("apple"));
    }

    @Test
    void testPrefixNotFound() {
        Trie trie = new Trie();

        trie.insert("apple");

        assertTrue(trie.suggest("xyz", 10).isEmpty());
    }
}
