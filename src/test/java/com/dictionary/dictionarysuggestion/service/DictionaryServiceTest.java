
package com.dictionary.dictionarysuggestion.service;

import com.dictionary.dictionarysuggestion.model.Word;
import com.dictionary.dictionarysuggestion.repository.DictionaryRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DictionaryServiceTest {

    @TempDir
    Path tempDir;

    private DictionaryService service;

    @BeforeEach
    void setUp() {
        DictionaryRepository repository =
                new DictionaryRepository(
                        tempDir.resolve("dictionary.csv")
                );

        service = new DictionaryService(repository);
        service.addWord("apple", "Quả táo");
        service.addWord("application", "Ứng dụng");
        service.addWord("banana", "Quả chuối");
    }

    @Test
    void testAddWord() {
        service.addWord("book", "Quyển sách");

        assertEquals("Quyển sách",
                service.findWord("book").getVietnameseMeaning());
        assertEquals(4, service.getAllWords().size());
    }

    @Test
    void testDuplicateWord() {
        assertThrows(IllegalArgumentException.class,
                () -> service.addWord("apple", "Táo"));
    }

    @Test
    void testFindWord() {
        Word result = service.findWord("APPLE");

        assertNotNull(result);
        assertEquals("Quả táo", result.getVietnameseMeaning());
    }

    @Test
    void testUpdateMeaning() {
        service.updateMeaning("apple", "Táo đỏ");

        assertEquals("Táo đỏ",
                service.findWord("apple").getVietnameseMeaning());
    }

    @Test
    void testDeleteWord() {
        assertTrue(service.deleteWord("apple"));
        assertNull(service.findWord("apple"));

        assertEquals(
                List.of("application"),
                service.suggestWords("app", 10)
                        .stream()
                        .map(Word::getEnglishWord)
                        .toList()
        );
    }

    @Test
    void testSuggestions() {
        List<Word> result = service.suggestWords("app", 10);

        assertEquals(2, result.size());
        assertEquals("apple", result.get(0).getEnglishWord());
        assertEquals("application", result.get(1).getEnglishWord());
    }

    @Test
    void testSaveAndLoad() throws IOException {
        service.save();

        DictionaryRepository repository =
                new DictionaryRepository(
                        tempDir.resolve("dictionary.csv")
                );

        DictionaryService restored =
                new DictionaryService(repository);

        restored.load();

        assertEquals(3, restored.getAllWords().size());
        assertNotNull(restored.findWord("apple"));
        assertNotNull(restored.findWord("banana"));
    }
}
