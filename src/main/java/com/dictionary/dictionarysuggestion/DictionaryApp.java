
package com.dictionary.dictionarysuggestion;

import com.dictionary.dictionarysuggestion.controller.DictionaryController;
import com.dictionary.dictionarysuggestion.repository.DictionaryRepository;
import com.dictionary.dictionarysuggestion.service.DictionaryService;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.nio.file.Path;

public class DictionaryApp extends Application {

    @Override
    public void start(Stage stage) {

        DictionaryRepository repository =
                new DictionaryRepository(
                        Path.of("data", "dictionary.csv")
                );

        DictionaryService service =
                new DictionaryService(repository);

        try {
            service.load();
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Data loading error");
            alert.setHeaderText("Cannot load dictionary");
            alert.setContentText(ex.getMessage());
            alert.showAndWait();
            return;
        }

        DictionaryController controller =
                new DictionaryController(service);

        Scene scene = new Scene(
                controller.createView(),
                850,
                780
        );

        stage.setTitle(
                "English-Vietnamese Dictionary | DSA Project"
        );

        stage.setScene(scene);
        stage.setMinWidth(750);
        stage.setMinHeight(650);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
