
package com.dictionary.dictionarysuggestion.algorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Trie {

    private final TrieNode root = new TrieNode();

    // Thêm từ vào Trie
    public void insert(String word) {
        TrieNode current = root;

        for (char c : word.toCharArray()) {
            current = current.getChildren()
                    .computeIfAbsent(c, k -> new TrieNode());
        }

        current.setEndOfWord(true);
    }

    // Kiểm tra từ có tồn tại không
    public boolean contains(String word) {
        if (word == null || word.isEmpty()) {
            return false;
        }

        TrieNode current = root;

        for (char c : word.toCharArray()) {
            current = current.getChildren().get(c);

            if (current == null) {
                return false;
            }
        }

        return current.isEndOfWord();
    }

    // Gợi ý các từ theo tiền tố
    public List<String> suggest(String prefix, int limit) {
        List<String> results = new ArrayList<>();

        if (prefix == null || prefix.isEmpty() || limit <= 0) {
            return results;
        }

        TrieNode current = root;

        for (char c : prefix.toCharArray()) {
            current = current.getChildren().get(c);

            if (current == null) {
                return results;
            }
        }

        collectWords(
                current,
                new StringBuilder(prefix),
                results,
                limit
        );

        return results;
    }

    // Duyệt DFS để lấy danh sách gợi ý
    private void collectWords(
            TrieNode node,
            StringBuilder prefix,
            List<String> results,
            int limit
    ) {
        if (results.size() >= limit) {
            return;
        }

        if (node.isEndOfWord()) {
            results.add(prefix.toString());
        }

        for (Map.Entry<Character, TrieNode> entry
                : node.getChildren().entrySet()) {

            if (results.size() >= limit) {
                break;
            }

            prefix.append(entry.getKey());

            collectWords(
                    entry.getValue(),
                    prefix,
                    results,
                    limit
            );

            prefix.deleteCharAt(prefix.length() - 1);
        }
    }

    // Xóa một từ khỏi Trie
    public boolean remove(String word) {
        if (!contains(word)) {
            return false;
        }

        removeRecursive(root, word, 0);
        return true;
    }

    private boolean removeRecursive(
            TrieNode node,
            String word,
            int index
    ) {
        if (index == word.length()) {
            node.setEndOfWord(false);
        } else {
            char c = word.charAt(index);

            TrieNode child = node.getChildren().get(c);

            if (removeRecursive(child, word, index + 1)) {
                node.getChildren().remove(c);
            }
        }

        return !node.isEndOfWord()
                && node.getChildren().isEmpty();
    }
}
