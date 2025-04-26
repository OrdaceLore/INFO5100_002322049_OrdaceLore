package lead.cheese.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import lead.cheese.model.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller for the main view of the application.
 * Handles user interactions and orchestrates the image processing operations.
 */
public class MainController {
    
    @FXML private VBox mainContainer;
    @FXML private Button uploadButton;
    @FXML private ComboBox<String> formatComboBox;
    @FXML private ComboBox<String> filterComboBox;
    @FXML private Button convertButton;
    @FXML private Button applyFilterButton;
    @FXML private Button downloadButton;
    @FXML private ListView<ProcessedImage> imageListView;
    @FXML private ImageView thumbnailView;
    @FXML private Slider zoomSlider;
    @FXML private Button zoomInButton;
    @FXML private Button zoomOutButton;
    @FXML private Button resetZoomButton;
    @FXML private Button viewOriginalButton;
    @FXML private StackPane imagePreviewContainer;
    @FXML private TableView<Map.Entry<String, String>> propertiesTableView;
    @FXML private TableColumn<Map.Entry<String, String>, String> propertyNameColumn;
    @FXML private TableColumn<Map.Entry<String, String>, String> propertyValueColumn;
    
    private final ObservableList<ProcessedImage> processedImages = FXCollections.observableArrayList();
    private final ObservableList<Map.Entry<String, String>> imageProperties = FXCollections.observableArrayList();
    private ImageProcessor imageProcessor;
    
    /**
     * Initializes the controller.
     * Sets up UI components and event handlers.
     */
    @FXML
    private void initialize() {
        try {
            // Initialize image processor using factory pattern
            imageProcessor = ImageProcessorFactory.createDefaultProcessor();
            
            // Configure image formats dropdown
            formatComboBox.getItems().addAll("jpg", "png", "bmp", "gif", "tiff");
            formatComboBox.getSelectionModel().selectFirst();
            
            // Configure filters dropdown
            filterComboBox.getItems().addAll("grayscale", "blur", "sharpen");
            filterComboBox.getSelectionModel().selectFirst();
            
            // Configure image list view
            imageListView.setItems(processedImages);
            imageListView.setCellFactory(param -> new ListCell<>() {
                @Override
                protected void updateItem(ProcessedImage item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.getFileName());
                    }
                }
            });
            
            // Add selection listener to image list
            imageListView.getSelectionModel().selectedItemProperty().addListener(
                    (observable, oldValue, newValue) -> {
                if (newValue != null) {
                    displayImageDetails(newValue);
                }
            });
            
            // Configure properties table view
            propertiesTableView.setItems(imageProperties);
            propertyNameColumn.setCellValueFactory(cellData -> 
                    new SimpleStringProperty(cellData.getValue().getKey()));
            propertyValueColumn.setCellValueFactory(cellData -> 
                    new SimpleStringProperty(cellData.getValue().getValue()));
            
            // Initialize buttons to disabled state
            convertButton.setDisable(true);
            applyFilterButton.setDisable(true);
            downloadButton.setDisable(true);
            viewOriginalButton.setDisable(true);
            
            // Initialize zoom controls
            configureZoomControls();
            
        } catch (IOException e) {
            showError("Initialization Error", 
                    "Failed to initialize the application: " + e.getMessage());
        }
    }
    
    /**
     * Configures the zoom controls for the image preview.
     */
    private void configureZoomControls() {
        // Bind zoom slider to thumbnail size
        zoomSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (thumbnailView.getImage() != null) {
                double scale = newVal.doubleValue();
                thumbnailView.setFitWidth(300 * scale);
                thumbnailView.setFitHeight(300 * scale);
            }
        });
        
        // Initial state
        zoomSlider.setValue(1.0);
        
        // Disable zoom controls until an image is loaded
        setZoomControlsDisabled(true);
    }
    
    /**
     * Enables or disables zoom controls
     * 
     * @param disabled Whether controls should be disabled
     */
    private void setZoomControlsDisabled(boolean disabled) {
        zoomSlider.setDisable(disabled);
        zoomInButton.setDisable(disabled);
        zoomOutButton.setDisable(disabled);
        resetZoomButton.setDisable(disabled);
        viewOriginalButton.setDisable(disabled);
    }
    
    /**
     * Handles the zoom in button click.
     */
    @FXML
    private void handleZoomIn() {
        double currentZoom = zoomSlider.getValue();
        double newZoom = Math.min(currentZoom + 0.25, zoomSlider.getMax());
        zoomSlider.setValue(newZoom);
    }
    
    /**
     * Handles the zoom out button click.
     */
    @FXML
    private void handleZoomOut() {
        double currentZoom = zoomSlider.getValue();
        double newZoom = Math.max(currentZoom - 0.25, zoomSlider.getMin());
        zoomSlider.setValue(newZoom);
    }
    
    /**
     * Handles the reset zoom button click.
     */
    @FXML
    private void handleResetZoom() {
        zoomSlider.setValue(1.0);
    }
    
    /**
     * Handles the view original button click.
     * Opens a new window with the full-size original image.
     */
    @FXML
    private void handleViewOriginal() {
        ProcessedImage selectedImage = imageListView.getSelectionModel().getSelectedItem();
        if (selectedImage == null) {
            return;
        }
        
        try {
            // Create a new window with the original image
            Stage originalImageStage = new Stage();
            originalImageStage.initModality(Modality.WINDOW_MODAL);
            originalImageStage.initOwner(mainContainer.getScene().getWindow());
            originalImageStage.setTitle("Original Image: " + selectedImage.getFileName());
            
            // Load the original image at full size
            Image originalImage = new Image(selectedImage.getOriginalFile().toURI().toString());
            
            // Create a scrollable image view
            ScrollPane scrollPane = new ScrollPane();
            ImageView originalImageView = new ImageView(originalImage);
            originalImageView.setPreserveRatio(true);
            
            // Set the image view in a stack pane for centering
            StackPane stackPane = new StackPane(originalImageView);
            scrollPane.setContent(stackPane);
            scrollPane.setPannable(true);
            scrollPane.setFitToWidth(true);
            scrollPane.setFitToHeight(true);
            
            // Create and set the scene
            Scene scene = new Scene(scrollPane, 800, 600);
            originalImageStage.setScene(scene);
            
            // Show the window
            originalImageStage.show();
            
        } catch (Exception e) {
            showError("Image View Error", 
                    "Failed to display original image: " + e.getMessage());
        }
    }
    
    /**
     * Handles the upload button click.
     * Opens a file chooser and processes selected images.
     */
    @FXML
    private void handleUpload() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image Files");
        fileChooser.getExtensionFilters().addAll(
                new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp", "*.tiff")
        );
        
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(mainContainer.getScene().getWindow());
        if (selectedFiles != null && !selectedFiles.isEmpty()) {
            processImages(selectedFiles);
        }
    }
    
    /**
     * Processes the selected image files.
     * Creates thumbnails and extracts metadata.
     * 
     * @param files The list of image files to process
     */
    private void processImages(List<File> files) {
        try {
            for (File file : files) {
                ProcessedImage processedImage = new ProcessedImage(file);
                
                // Create and set thumbnail
                File thumbnailFile = imageProcessor.createThumbnail(file, 300, 300);
                processedImage.setThumbnailFile(thumbnailFile);
                processedImage.setThumbnailImage(new Image(thumbnailFile.toURI().toString()));
                
                // Extract and store metadata (for internal use)
                Map<String, String> metadata = imageProcessor.extractMetadata(file);
                processedImage.setMetadata(metadata);
                
                // Add to the list
                processedImages.add(processedImage);
            }
            
            // Enable buttons
            convertButton.setDisable(false);
            applyFilterButton.setDisable(false);
            
            // Select first image
            if (!processedImages.isEmpty()) {
                imageListView.getSelectionModel().selectFirst();
            }
        } catch (IOException e) {
            showError("Processing Error", 
                    "Failed to process images: " + e.getMessage());
        }
    }
    
    /**
     * Displays the details of the selected image.
     * 
     * @param image The selected processed image
     */
    private void displayImageDetails(ProcessedImage image) {
        // Display thumbnail
        thumbnailView.setImage(image.getThumbnailImage());
        
        // Display image properties
        Map<String, String> metadata = image.getMetadata();
        imageProperties.clear();
        
        if (metadata != null) {
            // Add basic image dimensions first (if available)
            addPropertyIfExists(metadata, "Width");
            addPropertyIfExists(metadata, "Height");
            addPropertyIfExists(metadata, "File Size");
            addPropertyIfExists(metadata, "Color Type");
            
            // Add camera information
            addPropertyIfExists(metadata, "Exif SubIFD - Model");
            addPropertyIfExists(metadata, "Exif SubIFD - Make");
            addPropertyIfExists(metadata, "Exif SubIFD - Exposure Time");
            addPropertyIfExists(metadata, "Exif SubIFD - F-Number");
            addPropertyIfExists(metadata, "Exif SubIFD - ISO Speed Ratings");
            
            // Add location info if available
            addPropertyIfExists(metadata, "GPS - GPS Latitude");
            addPropertyIfExists(metadata, "GPS - GPS Longitude");
            addPropertyIfExists(metadata, "GPS - GPS Altitude");
            
            // Add date information
            addPropertyIfExists(metadata, "Exif SubIFD - Date/Time Original");
            
            // Add remaining properties that weren't specifically extracted above
            metadata.entrySet().stream()
                .filter(entry -> !imageProperties.contains(entry))
                .forEach(imageProperties::add);
        }
        
        // Enable zoom controls and view original button
        setZoomControlsDisabled(false);
    }
    
    /**
     * Adds a property to the property list if it exists in the metadata
     * 
     * @param metadata The metadata map
     * @param key The property key to look for
     */
    private void addPropertyIfExists(Map<String, String> metadata, String key) {
        metadata.entrySet().stream()
            .filter(entry -> entry.getKey().equals(key) || entry.getKey().endsWith(" - " + key))
            .findFirst()
            .ifPresent(imageProperties::add);
    }
    
    /**
     * Handles the convert button click.
     * Converts the selected image to the chosen format.
     */
    @FXML
    private void handleConvert() {
        ProcessedImage selectedImage = imageListView.getSelectionModel().getSelectedItem();
        if (selectedImage == null) {
            showError("Conversion Error", "No image selected");
            return;
        }
        
        String targetFormat = formatComboBox.getValue();
        if (targetFormat == null || targetFormat.isBlank()) {
            showError("Conversion Error", "No format selected");
            return;
        }
        
        try {
            File convertedFile = imageProcessor.convertImage(
                    selectedImage.getOriginalFile(), targetFormat);
            selectedImage.addConvertedFile(convertedFile);
            
            showSuccess("Conversion Complete", 
                    "Image converted to " + targetFormat + " format");
            downloadButton.setDisable(false);
        } catch (IOException e) {
            showError("Conversion Error", 
                    "Failed to convert image: " + e.getMessage());
        }
    }
    
    /**
     * Handles the apply filter button click.
     * Applies the chosen filter to the selected image.
     */
    @FXML
    private void handleApplyFilter() {
        ProcessedImage selectedImage = imageListView.getSelectionModel().getSelectedItem();
        if (selectedImage == null) {
            showError("Filter Error", "No image selected");
            return;
        }
        
        String filterType = filterComboBox.getValue();
        if (filterType == null || filterType.isBlank()) {
            showError("Filter Error", "No filter selected");
            return;
        }
        
        try {
            // For blur filter, add radius parameter
            Map<String, Object> params = new HashMap<>();
            if (filterType.equals("blur")) {
                params.put("radius", 5.0f);
            }
            
            File filteredFile = imageProcessor.applyFilter(
                    selectedImage.getOriginalFile(), filterType, params);
            selectedImage.addFilteredFile(filteredFile);
            
            showSuccess("Filter Applied", 
                    filterType + " filter applied to image");
            downloadButton.setDisable(false);
        } catch (IOException e) {
            showError("Filter Error", 
                    "Failed to apply filter: " + e.getMessage());
        }
    }
    
    /**
     * Handles the download button click.
     * Allows the user to download the processed images.
     */
    @FXML
    private void handleDownload() {
        ProcessedImage selectedImage = imageListView.getSelectionModel().getSelectedItem();
        if (selectedImage == null) {
            showError("Download Error", "No image selected");
            return;
        }
        
        List<File> allProcessedFiles = selectedImage.getConvertedFiles();
        List<File> filteredFiles = selectedImage.getFilteredFiles();
        allProcessedFiles.addAll(filteredFiles);
        
        if (allProcessedFiles.isEmpty()) {
            showError("Download Error", "No processed files to download");
            return;
        }
        
        // Get the most recent processed file (last in list)
        File sourceFile = allProcessedFiles.get(allProcessedFiles.size() - 1);
        
        // Create a meaningful filename based on operations performed
        String originalName = selectedImage.getFileName();
        String baseName = originalName.substring(0, originalName.lastIndexOf('.'));
        String extension = getFileExtension(sourceFile);
        
        // Determine operation type for the filename
        String operationType = "processed";
        if (!selectedImage.getConvertedFiles().isEmpty() && sourceFile.equals(selectedImage.getConvertedFiles().get(selectedImage.getConvertedFiles().size() - 1))) {
            operationType = "converted_to_" + extension;
        } else if (!filteredFiles.isEmpty()) {
            // Determine which filter was applied from the index
            int filterIndex = filteredFiles.indexOf(sourceFile);
            if (filterIndex >= 0) {
                String[] filterTypes = {"grayscale", "blur", "sharpen"};
                int selectedFilterIndex = filterComboBox.getSelectionModel().getSelectedIndex();
                if (selectedFilterIndex >= 0 && selectedFilterIndex < filterTypes.length) {
                    operationType = filterTypes[selectedFilterIndex] + "_filter";
                }
            }
        }
        
        // Add timestamp for uniqueness
        String timestamp = String.format("%tY%<tm%<td_%<tH%<tM%<tS", System.currentTimeMillis());
        
        // Build the new filename
        String newFileName = baseName + "_" + operationType + "_" + timestamp + "." + extension;
        
        // Create a directory chooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Processed Image");
        fileChooser.setInitialFileName(newFileName);
        
        Stage stage = (Stage) mainContainer.getScene().getWindow();
        File targetFile = fileChooser.showSaveDialog(stage);
        
        if (targetFile != null) {
            try {
                Files.copy(sourceFile.toPath(), targetFile.toPath());
                
                showSuccess("Download Complete", 
                        "Image saved to " + targetFile.getAbsolutePath());
            } catch (IOException e) {
                showError("Download Error", 
                        "Failed to save file: " + e.getMessage());
            }
        }
    }
    
    /**
     * Gets the file extension from a file.
     * 
     * @param file The file to get the extension from
     * @return The file extension without the dot
     */
    private String getFileExtension(File file) {
        String name = file.getName();
        int lastDotIndex = name.lastIndexOf('.');
        if (lastDotIndex > 0 && lastDotIndex < name.length() - 1) {
            return name.substring(lastDotIndex + 1).toLowerCase();
        }
        return "jpg"; // Default extension
    }
    
    /**
     * Shows an error alert dialog.
     * 
     * @param title The alert title
     * @param message The alert message
     */
    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    /**
     * Shows a success alert dialog.
     * 
     * @param title The alert title
     * @param message The alert message
     */
    private void showSuccess(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
} 