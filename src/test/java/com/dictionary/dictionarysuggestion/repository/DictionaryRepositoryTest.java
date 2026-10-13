
package com.dictionary.dictionarysuggestion.repository;

import com.dictionary.dictionarysuggestion.model.Word;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DictionaryRepositoryTest {

    @TempDir
    Path tempDir;

    @Test
    void testSaveAndLoad() throws IOException {
        Path file = tempDir.resolve("dictionary.csv");

        DictionaryRepository repository =
                new DictionaryRepository(file);

        List<Word> words = List.of(
                new Word("apple", "Quả táo"),
                new Word("book", "Quyển sách")
        );

        repository.save(words);

        List<Word> loaded = repository.load();

        assertEquals(2, loaded.size());
        assertEquals("apple", loaded.get(0).getEnglishWord());
        assertEquals("Quả táo",
                loaded.get(0).getVietnameseMeaning());
    }

    @Test
    void testLoadNonExistingFile() throws IOException {
        DictionaryRepository repository =
                new DictionaryRepository(
                        tempDir.resolve("missing.csv")
                );

        assertTrue(repository.load().isEmpty());
    }

    @Test
    void testOverwriteFile() throws IOException {
        Path file = tempDir.resolve("dictionary.csv");

        DictionaryRepository repository =
                new DictionaryRepository(file);

        repository.save(List.of(
                new Word("apple", "Quả táo")
        ));

        repository.save(List.of(
                new Word("banana", "Quả chuối")
        ));

        List<Word> loaded = repository.load();

        assertEquals(1, loaded.size());
        assertEquals("banana",
                loaded.get(0).getEnglishWord());
    }
}
