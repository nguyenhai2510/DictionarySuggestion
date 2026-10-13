
package com.dictionary.dictionarysuggestion.service;

import com.dictionary.dictionarysuggestion.model.Word;
import com.dictionary.dictionarysuggestion.repository.DictionaryRepository;

import java.nio.file.Path;

public class DictionaryDemo {

    public static void main(String[] args) throws Exception {

        DictionaryRepository repository =
                new DictionaryRepository(
                        Path.of("data", "dictionary.csv")
                );

        DictionaryService service =
                new DictionaryService(repository);

        // 1. Tải dữ liệu
        service.load();

        System.out.println("Total words: "
                + service.getAllWords().size());

        // 2. Tra cứu chính xác
        Word apple = service.findWord("apple");

        System.out.println("Search apple: " + apple);

        // 3. Gợi ý từ
        System.out.println("Suggestions for app:");

        for (Word word : service.suggestWords("app", 10)) {
            System.out.println("  " + word);
        }

        // 4. Thêm từ
        service.addWord("algorithm", "Thuật toán");

        System.out.println("Search algorithm: "
                + service.findWord("algorithm"));

        // 5. Sửa nghĩa
        service.updateMeaning(
                "algorithm",
                "Thuật toán trong khoa học máy tính"
        );

        System.out.println("After update: "
                + service.findWord("algorithm"));

        // 6. Xóa từ
        service.deleteWord("algorithm");

        System.out.println("After delete: "
                + service.findWord("algorithm"));

        // 7. Lưu dữ liệu
        service.save();

        System.out.println("Saved successfully!");
    }
}
