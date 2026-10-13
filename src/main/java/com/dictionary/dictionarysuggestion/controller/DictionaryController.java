
package com.dictionary.dictionarysuggestion.controller;

import com.dictionary.dictionarysuggestion.model.Word;
import com.dictionary.dictionarysuggestion.service.DictionaryService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.io.IOException;
import java.util.List;

public class DictionaryController {

    private final DictionaryService service;

    private final TableView<Word> table = new TableView<>();
    private final ListView<Word> suggestions = new ListView<>();

    private final TextField searchField = new TextField();
    private final TextField englishField = new TextField();
    private final TextField meaningField = new TextField();

    private final Label statusLabel = new Label();

    public DictionaryController(DictionaryService service) {
        this.service = service;
    }

    public Pane createView() {

        // Tiêu đề
        Label title = new Label("English - Vietnamese Dictionary");
        title.setStyle(
                "-fx-font-size: 24px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #1e293b;"
        );

        Label subtitle = new Label(
                "Dictionary Management & Word Suggestion"
        );

        subtitle.setStyle("-fx-text-fill: #64748b;");

        VBox header = new VBox(5, title, subtitle);

        // Tìm kiếm
        searchField.setPromptText(
                "Search English words... (e.g. app)"
        );

        searchField.setPrefHeight(40);

        suggestions.setPrefHeight(145);

        suggestions.setPlaceholder(
                new Label("Enter a prefix to see suggestions")
        );

        searchField.textProperty().addListener(
                (obs, oldValue, newValue) -> updateSuggestions(newValue)
        );

        suggestions.setOnMouseClicked(event -> {
            Word selected = suggestions.getSelectionModel()
                    .getSelectedItem();

            if (selected != null) {
                table.getSelectionModel().select(selected);
                table.scrollTo(selected);
                englishField.setText(selected.getEnglishWord());
                meaningField.setText(selected.getVietnameseMeaning());
            }
        });

        // Bảng từ vựng
        TableColumn<Word, String> englishColumn =
                new TableColumn<>("English");

        englishColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getEnglishWord()
                )
        );

        TableColumn<Word, String> meaningColumn =
                new TableColumn<>("Vietnamese Meaning");

        meaningColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getVietnameseMeaning()
                )
        );

        englishColumn.setPrefWidth(230);
        meaningColumn.setPrefWidth(420);

        table.getColumns().addAll(englishColumn, meaningColumn);
        table.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY
        );

        table.setPrefHeight(260);

        table.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldWord, selected) -> {
                    if (selected != null) {
                        englishField.setText(
                                selected.getEnglishWord()
                        );

                        meaningField.setText(
                                selected.getVietnameseMeaning()
                        );
                    }
                });

        // Form quản lý
        englishField.setPromptText("English word");
        meaningField.setPromptText("Vietnamese meaning");

        englishField.setPrefWidth(220);
        meaningField.setPrefWidth(380);

        HBox inputRow = new HBox(
                10, englishField, meaningField
        );

        Button addButton = new Button("Add Word");
        Button editButton = new Button("Update Meaning");
        Button deleteButton = new Button("Delete Word");
        Button clearButton = new Button("Clear");

        addButton.setOnAction(e -> addWord());
        editButton.setOnAction(e -> updateWord());
        deleteButton.setOnAction(e -> deleteWord());

        clearButton.setOnAction(e -> {
            englishField.clear();
            meaningField.clear();
            table.getSelectionModel().clearSelection();
        });

        HBox buttonRow = new HBox(
                10,
                addButton,
                editButton,
                deleteButton,
                clearButton
        );

        addButton.setStyle(
                "-fx-background-color: #2563eb;" +
                        "-fx-text-fill: white;"
        );

        editButton.setStyle(
                "-fx-background-color: #059669;" +
                        "-fx-text-fill: white;"
        );

        deleteButton.setStyle(
                "-fx-background-color: #dc2626;" +
                        "-fx-text-fill: white;"
        );

        statusLabel.setStyle("-fx-text-fill: #475569;");

        VBox root = new VBox(
                14,
                header,
                new Label("Search & Suggestions"),
                searchField,
                suggestions,
                new Label("Dictionary Entries"),
                table,
                new Label("Manage Vocabulary"),
                inputRow,
                buttonRow,
                statusLabel
        );

        root.setPadding(new Insets(22));
        root.setStyle("-fx-background-color: #f8fafc;");

        refreshTable();

        return root;
    }

    private void refreshTable() {
        table.setItems(FXCollections.observableArrayList(
                service.getAllWords()
        ));

        statusLabel.setText(
                "Total words: " + service.getAllWords().size()
        );
    }

    private void updateSuggestions(String prefix) {
        if (prefix == null || prefix.isBlank()) {
            suggestions.getItems().clear();
            return;
        }

        try {
            List<Word> result =
                    service.suggestWords(prefix, 10);

            suggestions.setItems(
                    FXCollections.observableArrayList(result)
            );
        } catch (IllegalArgumentException ex) {
            suggestions.getItems().clear();
        }
    }

    private void addWord() {
        try {
            service.addWord(
                    englishField.getText(),
                    meaningField.getText()
            );

            service.save();
            refreshTable();
            updateSuggestions(searchField.getText());

            showInfo("Word added successfully!");

        } catch (IllegalArgumentException | IOException ex) {
            showError(ex.getMessage());
        }
    }


    private void updateWord() {
        try {
            String english = englishField.getText().trim();
            String meaning = meaningField.getText().trim();

            // 1. Cập nhật dữ liệu trong HashMap
            service.updateMeaning(english, meaning);

            // 2. Lưu dữ liệu xuống CSV
            service.save();

            // 3. Làm mới bảng
            refreshTable();
            table.refresh();

            // 4. Làm mới danh sách gợi ý
            updateSuggestions(searchField.getText());

            // 5. Chọn lại từ vừa cập nhật
            Word updatedWord = service.findWord(english);

            table.getSelectionModel().select(updatedWord);
            table.scrollTo(updatedWord);

            // 6. Thông báo
            showInfo("Updated successfully!");

        } catch (IllegalArgumentException | IOException ex) {
            showError(ex.getMessage());
        }
    }


    private void deleteWord() {
        try {
            boolean deleted =
                    service.deleteWord(englishField.getText());

            if (!deleted) {
                showError("Word not found!");
                return;
            }

            service.save();
            refreshTable();
            updateSuggestions(searchField.getText());

            englishField.clear();
            meaningField.clear();

            showInfo("Word deleted successfully!");

        } catch (IllegalArgumentException | IOException ex) {
            showError(ex.getMessage());
        }
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
