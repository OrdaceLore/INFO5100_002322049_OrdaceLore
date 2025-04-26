package lead.cheese.model;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IMOperation;
import org.im4java.core.IdentifyCmd;
import org.im4java.process.OutputConsumer;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of the ImageProcessor interface using ImageMagick.
 * Uses im4java library to interact with ImageMagick command-line tools.
 */
public class ImageMagickProcessor extends AbstractImageProcessor {
    
    private final ConvertCmd convertCmd;
    private final IdentifyCmd identifyCmd;
    
    /**
     * Constructor initializing ImageMagick commands.
     * 
     * @throws IOException If temporary directory creation fails
     */
    public ImageMagickProcessor() throws IOException {
        super();
        this.convertCmd = new ConvertCmd();
        this.identifyCmd = new IdentifyCmd();
    }
    
    @Override
    public File convertImage(File inputFile, String outputFormat) throws IOException {
        try {
            File outputFile = createOutputFile(inputFile.getName(), outputFormat);
            
            IMOperation op = new IMOperation();
            op.addImage(inputFile.getAbsolutePath());
            op.addImage(outputFile.getAbsolutePath());
            
            convertCmd.run(op);
            return outputFile;
        } catch (Exception e) {
            throw new IOException("Failed to convert image: " + e.getMessage(), e);
        }
    }
    
    @Override
    public File applyFilter(File inputFile, String filterType, Map<String, Object> filterParams) throws IOException {
        try {
            String extension = getFileExtension(inputFile);
            File outputFile = createOutputFile(inputFile.getName(), extension);
            
            IMOperation op = new IMOperation();
            op.addImage(inputFile.getAbsolutePath());
            
            // Apply the selected filter
            switch (filterType.toLowerCase()) {
                case "grayscale":
                    op.colorspace("Gray");
                    break;
                case "blur":
                    op.blur(filterParams.containsKey("radius") ? 
                            Double.parseDouble(filterParams.get("radius").toString()) : 0.0,
                            filterParams.containsKey("sigma") ? 
                            Double.parseDouble(filterParams.get("sigma").toString()) : 3.0);
                    break;
                case "sharpen":
                    op.sharpen(filterParams.containsKey("radius") ? 
                            Double.parseDouble(filterParams.get("radius").toString()) : 0.0,
                            filterParams.containsKey("sigma") ? 
                            Double.parseDouble(filterParams.get("sigma").toString()) : 1.0);
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported filter type: " + filterType);
            }
            
            op.addImage(outputFile.getAbsolutePath());
            
            convertCmd.run(op);
            return outputFile;
        } catch (Exception e) {
            throw new IOException("Failed to apply filter: " + e.getMessage(), e);
        }
    }
    
    @Override
    public Map<String, String> extractMetadata(File inputFile) throws IOException {
        Map<String, String> metadata = new HashMap<>();
        
        try {
            // Use Drew's metadata-extractor to get detailed metadata
            Metadata imageMetadata = ImageMetadataReader.readMetadata(inputFile);
            for (Directory directory : imageMetadata.getDirectories()) {
                for (Tag tag : directory.getTags()) {
                    // Skip non-essential tags to keep the output clean
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
} 