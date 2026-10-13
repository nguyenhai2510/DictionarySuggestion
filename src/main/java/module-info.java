
module com.dictionary.dictionarysuggestion {

    requires javafx.controls;
    requires javafx.fxml;

    exports com.dictionary.dictionarysuggestion;
    exports com.dictionary.dictionarysuggestion.model;
    exports com.dictionary.dictionarysuggestion.algorithm;
    exports com.dictionary.dictionarysuggestion.repository;
    exports com.dictionary.dictionarysuggestion.service;
    exports com.dictionary.dictionarysuggestion.controller;

    opens com.dictionary.dictionarysuggestion to javafx.fxml;
}
