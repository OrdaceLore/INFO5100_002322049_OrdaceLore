# Image Management Tool - Class Diagram

## Core Model Components

### ImageProcessor (Interface)
```
interface ImageProcessor {
  + File convertImage(File inputFile, String outputFormat)
  + File applyFilter(File inputFile, String filterType, Map<String, Object> filterParams)
  + File createThumbnail(File inputFile, int width, int height) 
  + Map<String, String> extractMetadata(File inputFile)
}
```

### AbstractImageProcessor (Abstract Class)
```
abstract class AbstractImageProcessor implements ImageProcessor {
  # Path tempDir
  # AbstractImageProcessor()
  + File createThumbnail(File inputFile, int width, int height)
  # String getFileExtension(File file)
}
```

### JavaImageIOProcessor
```
class JavaImageIOProcessor extends AbstractImageProcessor {
  + JavaImageIOProcessor()
  + File convertImage(File inputFile, String outputFormat)
  + File applyFilter(File inputFile, String filterType, Map<String, Object> filterParams)
  + Map<String, String> extractMetadata(File inputFile)
  - BufferedImage applyGrayscale(BufferedImage image)
  - BufferedImage applySepia(BufferedImage image)
  - BufferedImage applyBlur(BufferedImage image, float radius)
  - BufferedImage applySharpen(BufferedImage image)
}
```

### ImageMagickProcessor
```
class ImageMagickProcessor extends AbstractImageProcessor {
  - ConvertCmd convertCmd
  - IdentifyCmd identifyCmd
  + ImageMagickProcessor()
  + File convertImage(File inputFile, String outputFormat)
  + File applyFilter(File inputFile, String filterType, Map<String, Object> filterParams)
  + Map<String, String> extractMetadata(File inputFile)
}
```

### ImageProcessorFactory
```
class ImageProcessorFactory {
  + enum ProcessorType {IMAGEMAGICK, JAVA_IMAGEIO}
  + static ImageProcessor createProcessor(ProcessorType type)
  + static ImageProcessor createDefaultProcessor()
}
```

### ProcessedImage
```
class ProcessedImage {
  - File originalFile
  - File thumbnailFile
  - Image thumbnailImage
  - Map<String, String> metadata
  - List<File> convertedFiles
  - List<File> filteredFiles
  + ProcessedImage(File originalFile)
  + File getOriginalFile()
  + File getThumbnailFile()
  + void setThumbnailFile(File thumbnailFile)
  + Image getThumbnailImage()
  + void setThumbnailImage(Image thumbnailImage)
  + Map<String, String> getMetadata()
  + void setMetadata(Map<String, String> metadata)
  + List<File> getConvertedFiles()
  + void addConvertedFile(File convertedFile)
  + List<File> getFilteredFiles()
  + void addFilteredFile(File filteredFile)
  + String getFileName()
}
```

## Controller

### MainController
```
class MainController {
  - VBox mainContainer
  - Button uploadButton
  - ComboBox<String> formatComboBox
  - ComboBox<String> filterComboBox
  - Button convertButton
  - Button applyFilterButton
  - Button downloadButton
  - ListView<ProcessedImage> imageListView
  - ImageView thumbnailView
  - Slider zoomSlider
  - Button zoomInButton
  - Button zoomOutButton
  - Button resetZoomButton
  - Button viewOriginalButton
  - StackPane imagePreviewContainer
  - TableView<Map.Entry<String, String>> propertiesTableView
  - TableColumn<Map.Entry<String, String>, String> propertyNameColumn
  - TableColumn<Map.Entry<String, String>, String> propertyValueColumn
  - ObservableList<ProcessedImage> processedImages
  - ObservableList<Map.Entry<String, String>> imageProperties
  - ImageProcessor imageProcessor
  - void initialize()
  - void configureZoomControls()
  - void setZoomControlsDisabled(boolean disabled)
  - void handleZoomIn()
  - void handleZoomOut()
  - void handleResetZoom()
  - void handleViewOriginal()
  - void handleUpload()
  - void processImages(List<File> files)
  - void displayImageDetails(ProcessedImage image)
  - void addPropertyIfExists(Map<String, String> metadata, String key)
  - void handleConvert()
  - void handleApplyFilter()
  - void handleDownload()
  - void showError(String title, String message)
  - void showSuccess(String title, String message)
}
```

## Class Relationships

1. **ImageProcessorFactory** creates **ImageProcessor** implementations
2. **MainController** uses **ImageProcessor** for image operations
3. **MainController** manages **ProcessedImage** objects
4. **MainController** uses **ImageProcessorFactory** to obtain processors
5. **JavaImageIOProcessor** and **ImageMagickProcessor** extend **AbstractImageProcessor**
6. **AbstractImageProcessor** implements **ImageProcessor** interface 