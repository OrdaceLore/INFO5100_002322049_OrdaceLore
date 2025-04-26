package lead.cheese.model;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Interface defining operations for image processing.
 * Provides methods for converting images and applying filters.
 */
public interface ImageProcessor {
    
    /**
     * Converts an image to a specified format.
     * 
     * @param inputFile The input image file
     * @param outputFormat The target format (e.g., "png", "jpg", "bmp")
     * @return The converted image file
     * @throws IOException If an I/O error occurs during conversion
     */
    File convertImage(File inputFile, String outputFormat) throws IOException;
    
    /**
     * Applies a filter to an image.
     * 
     * @param inputFile The input image file
     * @param filterType The type of filter to apply
     * @param filterParams Additional parameters for the filter
     * @return The filtered image file
     * @throws IOException If an I/O error occurs during filtering
     */
    File applyFilter(File inputFile, String filterType, Map<String, Object> filterParams) throws IOException;
    
    /**
     * Creates a thumbnail of the image.
     * 
     * @param inputFile The input image file
     * @param width Thumbnail width
     * @param height Thumbnail height
     * @return The thumbnail image file
     * @throws IOException If an I/O error occurs during thumbnail creation
     */
    File createThumbnail(File inputFile, int width, int height) throws IOException;
    
    /**
     * Extracts metadata from an image.
     * 
     * @param inputFile The input image file
     * @return A map of metadata properties
     * @throws IOException If an I/O error occurs during metadata extraction
     */
    Map<String, String> extractMetadata(File inputFile) throws IOException;
} 