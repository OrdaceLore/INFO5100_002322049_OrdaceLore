package lead.cheese.model;

import java.io.IOException;

/**
 * Factory class for creating ImageProcessor instances.
 * Implements the Factory design pattern(Creational Pattern).
 */
public class ImageProcessorFactory {
    
    /**
     * Processor type enumeration.
     */
    public enum ProcessorType {
        IMAGEMAGICK,
        JAVA_IMAGEIO
    }
    
    /**
     * Creates an ImageProcessor instance based on the specified type.
     * 
     * @param type The type of processor to create
     * @return An ImageProcessor instance
     * @throws IOException If processor creation fails
     */
    public static ImageProcessor createProcessor(ProcessorType type) throws IOException {
        return switch (type) {
            case IMAGEMAGICK -> new ImageMagickProcessor();
            case JAVA_IMAGEIO -> new JavaImageIOProcessor();
        };
    }
    
    /**
     * Creates a default ImageProcessor instance.
     * 
     * @return An ImageProcessor instance
     * @throws IOException If processor creation fails
     */
    public static ImageProcessor createDefaultProcessor() throws IOException {
        try {
            return new ImageMagickProcessor();
        } catch (Exception e) {
            // Fallback to Java ImageIO if ImageMagick is not available
            return new JavaImageIOProcessor();
        }
    }
} 