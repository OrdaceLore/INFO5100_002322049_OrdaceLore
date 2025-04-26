package lead.cheese.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

/**
 * Abstract base implementation of the ImageProcessor interface.
 * Provides common functionality for image processing operations.
 */
public abstract class AbstractImageProcessor implements ImageProcessor {
    
    // Common temporary directory for processed images
    protected final Path tempDir;
    
    /**
     * Constructor that initializes the temporary directory.
     * 
     * @throws IOException If temp directory creation fails
     */
    public AbstractImageProcessor() throws IOException {
        this.tempDir = Files.createTempDirectory("image_processor");
        // Register shutdown hook to clean up temp directory
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                Files.walk(tempDir)
                    .sorted((p1, p2) -> -p1.compareTo(p2))
                    .forEach(p -> {
                        try {
                            Files.deleteIfExists(p);
                        } catch (IOException e) {
                            System.err.println("Failed to delete: " + p);
                        }
                    });
            } catch (IOException e) {
                System.err.println("Error cleaning temp directory: " + e.getMessage());
            }
        }));
    }
    
    @Override
    public File createThumbnail(File inputFile, int width, int height) throws IOException {
        BufferedImage originalImage = ImageIO.read(inputFile);
        if (originalImage == null) {
            throw new IOException("Failed to read image: " + inputFile.getAbsolutePath());
        }
        
        BufferedImage thumbnail = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = thumbnail.createGraphics();
        try {
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.drawImage(originalImage, 0, 0, width, height, null);
        } finally {
            g2d.dispose();
        }
        
        String filename = "thumb_" + inputFile.getName();
        File outputFile = tempDir.resolve(filename).toFile();
        String extension = getFileExtension(inputFile);
        ImageIO.write(thumbnail, extension, outputFile);
        
        return outputFile;
    }
    
    /**
     * Extracts the file extension from a file.
     * 
     * @param file The file to get the extension from
     * @return The file extension without the dot
     */
    protected String getFileExtension(File file) {
        String name = file.getName();
        int lastDotIndex = name.lastIndexOf('.');
        if (lastDotIndex > 0 && lastDotIndex < name.length() - 1) {
            return name.substring(lastDotIndex + 1).toLowerCase();
        }
        return "jpg"; // Default extension
    }
    
    /**
     * Creates a unique output file in the temp directory.
     * 
     * @param originalFileName The original file name
     * @param newExtension The new file extension
     * @return A new File object for the output
     */
    protected File createOutputFile(String originalFileName, String newExtension) {
        String baseName = originalFileName.contains(".") ? 
                originalFileName.substring(0, originalFileName.lastIndexOf('.')) : 
                originalFileName;
        
        String newFileName = baseName + "_" + System.currentTimeMillis() + "." + newExtension;
        return tempDir.resolve(newFileName).toFile();
    }
} 