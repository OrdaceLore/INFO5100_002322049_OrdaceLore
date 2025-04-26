package lead.cheese.model;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of the ImageProcessor interface using Java's ImageIO.
 * Provides a pure Java implementation for image processing operations.
 */
public class JavaImageIOProcessor extends AbstractImageProcessor {
    
    /**
     * Constructor.
     * 
     * @throws IOException If temp directory creation fails
     */
    public JavaImageIOProcessor() throws IOException {
        super();
    }
    
    @Override
    public File convertImage(File inputFile, String outputFormat) throws IOException {
        BufferedImage image = ImageIO.read(inputFile);
        if (image == null) {
            throw new IOException("Failed to read image: " + inputFile.getAbsolutePath());
        }
        
        // Create output file
        File outputFile = createOutputFile(inputFile.getName(), outputFormat);
        
        // Write in the new format
        if (!ImageIO.write(image, outputFormat, outputFile)) {
            throw new IOException("Failed to write image in format: " + outputFormat);
        }
        
        return outputFile;
    }
    
    @Override
    public File applyFilter(File inputFile, String filterType, Map<String, Object> filterParams) throws IOException {
        BufferedImage image = ImageIO.read(inputFile);
        if (image == null) {
            throw new IOException("Failed to read image: " + inputFile.getAbsolutePath());
        }
        
        BufferedImage filteredImage;
        
        // Apply the filter
        switch (filterType.toLowerCase()) {
            case "grayscale":
                filteredImage = applyGrayscale(image);
                break;
            case "blur":
                float radius = filterParams.containsKey("radius") ? 
                        Float.parseFloat(filterParams.get("radius").toString()) : 5.0f;
                filteredImage = applyBlur(image, radius);
                break;
            case "sharpen":
                filteredImage = applySharpen(image);
                break;
            default:
                throw new IllegalArgumentException("Unsupported filter type: " + filterType);
        }
        
        String extension = getFileExtension(inputFile);
        File outputFile = createOutputFile(inputFile.getName(), extension);
        
        if (!ImageIO.write(filteredImage, extension, outputFile)) {
            throw new IOException("Failed to write filtered image");
        }
        
        return outputFile;
    }
    
    @Override
    public Map<String, String> extractMetadata(File inputFile) throws IOException {
        Map<String, String> metadata = new HashMap<>();
        
        try {
            // Get image dimensions
            BufferedImage image = ImageIO.read(inputFile);
            if (image != null) {
                metadata.put("Width", String.valueOf(image.getWidth()));
                metadata.put("Height", String.valueOf(image.getHeight()));
                metadata.put("Color Type", getColorTypeName(image.getType()));
            }
            
            // Use Drew's metadata-extractor to get detailed metadata
            Metadata imageMetadata = ImageMetadataReader.readMetadata(inputFile);
            for (Directory directory : imageMetadata.getDirectories()) {
                for (Tag tag : directory.getTags()) {
                    if (!tag.getTagName().contains("Unknown")) {
                        metadata.put(directory.getName() + " - " + tag.getTagName(), tag.getDescription());
                    }
                }
            }
            
            // Add basic file info
            metadata.put("File Name", inputFile.getName());
            metadata.put("File Size", String.format("%.2f KB", inputFile.length() / 1024.0));
            
            return metadata;
        } catch (Exception e) {
            throw new IOException("Failed to extract metadata: " + e.getMessage(), e);
        }
    }
    
    /**
     * Gets a human-readable color type name from BufferedImage type constant.
     * 
     * @param imageType The BufferedImage type
     * @return Human-readable type name
     */
    private String getColorTypeName(int imageType) {
        return switch (imageType) {
            case BufferedImage.TYPE_INT_RGB -> "RGB";
            case BufferedImage.TYPE_INT_ARGB -> "ARGB";
            case BufferedImage.TYPE_INT_ARGB_PRE -> "ARGB Pre-Multiplied";
            case BufferedImage.TYPE_INT_BGR -> "BGR";
            case BufferedImage.TYPE_3BYTE_BGR -> "3-Byte BGR";
            case BufferedImage.TYPE_4BYTE_ABGR -> "4-Byte ABGR";
            case BufferedImage.TYPE_4BYTE_ABGR_PRE -> "4-Byte ABGR Pre-Multiplied";
            case BufferedImage.TYPE_BYTE_GRAY -> "Grayscale";
            case BufferedImage.TYPE_BYTE_BINARY -> "Binary";
            case BufferedImage.TYPE_BYTE_INDEXED -> "Indexed";
            case BufferedImage.TYPE_USHORT_GRAY -> "Grayscale (16-bit)";
            case BufferedImage.TYPE_USHORT_565_RGB -> "RGB 565";
            case BufferedImage.TYPE_USHORT_555_RGB -> "RGB 555";
            default -> "Unknown";
        };
    }
    
    /**
     * Applies a grayscale filter to an image.
     * 
     * @param image The input image
     * @return The grayscale image
     */
    private BufferedImage applyGrayscale(BufferedImage image) {
        BufferedImage result = new BufferedImage(
                image.getWidth(), 
                image.getHeight(), 
                BufferedImage.TYPE_BYTE_GRAY);
        
        Graphics g = result.getGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        
        return result;
    }
    
    /**
     * Applies a blur filter to an image.
     * 
     * @param image The input image
     * @param radius The blur radius
     * @return The blurred image
     */
    private BufferedImage applyBlur(BufferedImage image, float radius) {
        int size = Math.max(1, Math.round(radius));
        float weight = 1.0f / (size * size);
        float[] data = new float[size * size];
        
        for (int i = 0; i < data.length; i++) {
            data[i] = weight;
        }
        
        Kernel kernel = new Kernel(size, size, data);
        ConvolveOp op = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
        
        return op.filter(image, null);
    }
    
    /**
     * Applies a sharpen filter to an image.
     * 
     * @param image The input image
     * @return The sharpened image
     */
    private BufferedImage applySharpen(BufferedImage image) {
        float[] sharpenMatrix = {
            0.0f, -0.2f, 0.0f,
            -0.2f, 1.8f, -0.2f,
            0.0f, -0.2f, 0.0f
        };
        
        Kernel kernel = new Kernel(3, 3, sharpenMatrix);
        ConvolveOp op = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
        
        return op.filter(image, null);
    }
} 