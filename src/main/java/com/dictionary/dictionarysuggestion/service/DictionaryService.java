
package com.dictionary.dictionarysuggestion.service;

import com.dictionary.dictionarysuggestion.algorithm.Trie;
import com.dictionary.dictionarysuggestion.model.Word;
import com.dictionary.dictionarysuggestion.repository.DictionaryRepository;

import java.io.IOException;
import java.util.*;

public class DictionaryService {

    private final Map<String, Word> words = new HashMap<>();
    private Trie trie = new Trie();
    private final DictionaryRepository repository;

    public DictionaryService(DictionaryRepository repository) {
        this.repository = repository;
    }

    private String normalize(String english) {
        if (english == null) {
            throw new IllegalArgumentException(
                    "Từ tiếng Anh không được null"
            );
        }

        String result = english.trim().toLowerCase(Locale.ROOT);

        if (!result.matches("[a-z]+")) {
            throw new IllegalArgumentException(
                    "Từ tiếng Anh chỉ được chứa a-z"
            );
        }

        return result;
    }

    private void validateMeaning(String meaning) {
        if (meaning == null || meaning.isBlank()) {
            throw new IllegalArgumentException(
                    "Nghĩa tiếng Việt không được để trống"
            );
        }

        if (meaning.contains("\n") || meaning.contains("\r")) {
            throw new IllegalArgumentException(
                    "Nghĩa không được chứa ký tự xuống dòng"
            );
        }
    }

    public void addWord(String english, String meaning) {
        String key = normalize(english);
        validateMeaning(meaning);

        if (words.containsKey(key)) {
            throw new IllegalArgumentException(
                    "Từ đã tồn tại: " + key
            );
        }

        words.put(key, new Word(key, meaning.trim()));
        trie.insert(key);
    }

    public void updateMeaning(String english, String meaning) {
        String key = normalize(english);
        validateMeaning(meaning);

        Word word = words.get(key);

        if (word == null) {
            throw new IllegalArgumentException(
                    "Không tìm thấy từ: " + key
            );
        }

        word.setVietnameseMeaning(meaning.trim());
    }

    public boolean deleteWord(String english) {
        String key = normalize(english);

        if (!words.containsKey(key)) {
            return false;
        }

        words.remove(key);
        trie.remove(key);

        return true;
    }

    public Word findWord(String english) {
        return words.get(normalize(english));
    }

    public List<Word> suggestWords(String prefix, int limit) {
        if (prefix == null || prefix.isBlank() || limit <= 0) {
            return List.of();
        }

        String key = normalize(prefix);
        List<String> suggestions = trie.suggest(key, limit);

        List<Word> result = new ArrayList<>();

        for (String suggestion : suggestions) {
            Word word = words.get(suggestion);

            if (word != null) {
                result.add(word);
            }
        }

        return result;
    }

    public List<Word> getAllWords() {
        List<Word> result = new ArrayList<>(words.values());

        result.sort(
                Comparator.comparing(Word::getEnglishWord)
        );

        return result;
    }

    public void load() throws IOException {
        List<Word> loadedWords = repository.load();

        Map<String, Word> newWords = new HashMap<>();
        Trie newTrie = new Trie();

        for (Word word : loadedWords) {
            String key = normalize(word.getEnglishWord());
            validateMeaning(word.getVietnameseMeaning());

            if (newWords.containsKey(key)) {
                throw new IOException(
                        "Từ bị trùng trong CSV: " + key
                );
            }

            newWords.put(
                    key,
                    new Word(key, word.getVietnameseMeaning().trim())
            );

            newTrie.insert(key);
        }

        words.clear();
        words.putAll(newWords);
        trie = newTrie;
    }

    public void save() throws IOException {
        repository.save(getAllWords());
    }
}
