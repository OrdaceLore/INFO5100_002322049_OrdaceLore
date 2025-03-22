import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import org.json.*;// this only works all files is downloaded to local and run on local machine
import java.nio.file.*;

public class BookParser {
    public static void main(String[] args) throws Exception {
        // ========= XML Parsing =========
        File xmlFile = new File("books.xml");
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document xmlDoc = dBuilder.parse(xmlFile);
        xmlDoc.getDocumentElement().normalize();
        
        System.out.println("XML Parsing:");
        printXML(xmlDoc);

        // Programmatically add a new book entry in XML
        Element newBook = xmlDoc.createElement("Book");

        Element title = xmlDoc.createElement("title");
        title.appendChild(xmlDoc.createTextNode("Book Four"));
        newBook.appendChild(title);

        Element publishedYear = xmlDoc.createElement("publishedYear");
        publishedYear.appendChild(xmlDoc.createTextNode("2025"));
        newBook.appendChild(publishedYear);

        Element numberOfPages = xmlDoc.createElement("numberOfPages");
        numberOfPages.appendChild(xmlDoc.createTextNode("600"));
        newBook.appendChild(numberOfPages);

        Element authors = xmlDoc.createElement("authors");
        Element author = xmlDoc.createElement("author");
        author.appendChild(xmlDoc.createTextNode("Author Six"));
        authors.appendChild(author);
        newBook.appendChild(authors);

        xmlDoc.getDocumentElement().appendChild(newBook);

        System.out.println("\nXML After Adding New Book:");
        printXML(xmlDoc);

        // ========= JSON Parsing =========
        String jsonContent = new String(Files.readAllBytes(Paths.get("books.json")));
        JSONObject jsonDoc = new JSONObject(jsonContent);

        System.out.println("\nJSON Parsing:");
        printJSON(jsonDoc);

        // Programmatically add a new book entry in JSON
        JSONObject newBookJson = new JSONObject();
        newBookJson.put("title", "Book Four");
        newBookJson.put("publishedYear", 2025);
        newBookJson.put("numberOfPages", 600);
        newBookJson.put("authors", new JSONArray().put("Author Six"));

        jsonDoc.getJSONObject("BookShelf").getJSONArray("Books").put(newBookJson);

        System.out.println("\nJSON After Adding New Book:");
        printJSON(jsonDoc);
    }

    private static void printXML(Document doc) throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(System.out);
        transformer.transform(source, result);
    }

    private static void printJSON(JSONObject jsonObject) {
        System.out.println(jsonObject.toString(4));
    }
}