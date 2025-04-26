package lead.cheese;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * Main application class for the Image Processor application.
 * Uses JavaFX to create a GUI for image processing tasks.
 */
public class ImageProcessorApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(ImageProcessorApp.class.getResource("main-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 900, 700);
            
            // Apply CSS styling
            String css = ImageProcessorApp.class.getResource("styles.css").toExternalForm();
            scene.getStylesheets().add(css);
            
            stage.setTitle("Image Processor");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error starting application: " + e.getMessage());
        }
    }

    /**
     * Main method to launch the application.
     * @param args command line arguments
     */
    public static void main(String[] args) {
        launch();
    }
} 