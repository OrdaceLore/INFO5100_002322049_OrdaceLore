package lead.cheese.model;

import javafx.scene.image.Image;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Model class representing a processed image with its properties and generated outputs.
 */
public class ProcessedImage {
    private final File originalFile;
    private File thumbnailFile;
    private Image thumbnailImage;
    private Map<String, String> metadata;
    private final List<File> convertedFiles;
    private final List<File> filteredFiles;
    
    /**
     * Constructor for ProcessedImage.
     * 
     * @param originalFile The original image file
     */
    public ProcessedImage(File originalFile) {
        this.originalFile = originalFile;
        this.convertedFiles = new ArrayList<>();
        this.filteredFiles = new ArrayList<>();
    }
    
    /**
     * Gets the original image file.
     * 
     * @return The original file
     */
    public File getOriginalFile() {
        return originalFile;
    }
    
    /**
     * Gets the thumbnail file.
     * 
     * @return The thumbnail file
     */
    public File getThumbnailFile() {
        return thumbnailFile;
    }
    
    /**
     * Sets the thumbnail file.
     * 
     * @param thumbnailFile The thumbnail file
     */
    public void setThumbnailFile(File thumbnailFile) {
        this.thumbnailFile = thumbnailFile;
    }
    
    /**
     * Gets the thumbnail image for display.
     * 
     * @return The thumbnail image
     */
    public Image getThumbnailImage() {
        return thumbnailImage;
    }
    
    /**
     * Sets the thumbnail image for display.
     * 
     * @param thumbnailImage The thumbnail image
     */
    public void setThumbnailImage(Image thumbnailImage) {
        this.thumbnailImage = thumbnailImage;
    }
    
    /**
     * Gets the image metadata.
     * 
     * @return The metadata as a map
     */
    public Map<String, String> getMetadata() {
        return metadata;
    }
    
    /**
     * Sets the image metadata.
     * 
     * @param metadata The metadata as a map
     */
    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }
    
    /**
     * Gets the list of converted files.
     * 
     * @return The list of converted files
     */
    public List<File> getConvertedFiles() {
        return convertedFiles;
    }
    
    /**
     * Adds a converted file to the list.
     * 
     * @param convertedFile The converted file to add
     */
    public void addConvertedFile(File convertedFile) {
        this.convertedFiles.add(convertedFile);
    }
    
    /**
     * Gets the list of filtered files.
     * 
     * @return The list of filtered files
     */
    public List<File> getFilteredFiles() {
        return filteredFiles;
    }
    
    /**
     * Adds a filtered file to the list.
     * 
     * @param filteredFile The filtered file to add
     */
    public void addFilteredFile(File filteredFile) {
        this.filteredFiles.add(filteredFile);
    }
    
    /**
     * Gets the name of the original file.
     * 
     * @return The original file name
     */
    public String getFileName() {
        return originalFile.getName();
    }
} 