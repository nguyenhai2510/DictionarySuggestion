
package com.dictionary.dictionarysuggestion.repository;

import com.dictionary.dictionarysuggestion.model.Word;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DictionaryRepository {

    private final Path filePath;

    public DictionaryRepository(Path filePath) {
        this.filePath = filePath;
    }

    public List<Word> load() throws IOException {
        List<Word> result = new ArrayList<>();

        if (!Files.exists(filePath)) {
            return result;
        }

        List<String> lines = Files.readAllLines(
                filePath, StandardCharsets.UTF_8
        );

        for (String line : lines) {
            if (line.isBlank()) {
                continue;
            }

            String[] parts = line.split(",", 2);

            if (parts.length != 2) {
                throw new IOException(
                        "Dữ liệu CSV không hợp lệ: " + line
                );
            }

            result.add(new Word(parts[0], parts[1]));
        }

        return result;
    }

    public void save(Collection<Word> words) throws IOException {
        Path parent = filePath.toAbsolutePath().getParent();

        if (parent != null) {
            Files.createDirectories(parent);
        }

        List<String> lines = new ArrayList<>();

        for (Word word : words) {
            lines.add(
                    word.getEnglishWord() + ","
                            + word.getVietnameseMeaning()
            );
        }

        Files.write(
                filePath,
                lines,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING
        );
    }
}
