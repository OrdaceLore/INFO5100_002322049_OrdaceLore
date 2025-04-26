# Image Processor Application

A JavaFX application for image processing that allows users to upload, convert, filter, and download images.

## Features

- Upload one or more image files
- View image thumbnails and metadata (height, width, camera, location, etc.)
- Convert images to various formats (JPG, PNG, BMP, GIF, TIFF)
- Apply filters to images (grayscale, sepia, blur, sharpen)
- Download processed images

## Requirements

- Java 21 or higher
- Maven
- ImageMagick (optional, for enhanced image processing capabilities)

## Installation

1. Clone this repository
2. Build with Maven:
   ```
   mvn clean package
   ```
3. Run the application:
   ```
   mvn javafx:run
   ```

## Usage Instructions

1. **Upload Images**: Click the "Upload Image(s)" button to select one or more image files from your computer.
2. **View Image Properties**: Select an image from the list to view its thumbnail and metadata properties.
3. **Convert Image Format**: 
   - Select a target format from the dropdown menu
   - Click the "Convert" button
4. **Apply Filters**:
   - Choose a filter from the dropdown menu
   - Click the "Apply Filter" button
5. **Download Processed Image**:
   - After converting or filtering an image, click the "Download Processed Image" button
   - Choose a location to save the file

## Implementation Details

The application is designed with object-oriented principles:

- **Inheritance**: AbstractImageProcessor base class with concrete implementations
- **Encapsulation**: Well-defined interfaces and data hiding
- **Interfaces**: ImageProcessor interface defining the processing operations
- **Design Pattern**: Factory pattern for creating image processor instances

## Error Handling

The application includes comprehensive error handling for:
- File operations
- Image processing failures
- Invalid format selections
- User interface interactions

## Dependencies

- JavaFX: UI framework
- metadata-extractor: For extracting image metadata
- im4java: For ImageMagick integration 