import java.io.*;

public class App3 {
    public static void main(String[] args) {
        Shape triangle = new Triangle(3, 4, 5);
        Shape rectangle = new Rectangle(5, 10);
        Shape circle = new Circle(7);
        Shape square = new Square(6);

        try {
            // Serialize objects
            FileOutputStream fileOut = new FileOutputStream("shapes.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(triangle);
            out.writeObject(rectangle);
            out.writeObject(circle);
            out.writeObject(square);
            out.close();
            fileOut.close();
            System.out.println("Serialized data is saved in shapes.ser");

            // Deserialize objects
            FileInputStream fileIn = new FileInputStream("shapes.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            Triangle deserializedTriangle = (Triangle) in.readObject();
            Rectangle deserializedRectangle = (Rectangle) in.readObject();
            Circle deserializedCircle = (Circle) in.readObject();
            Square deserializedSquare = (Square) in.readObject();
            in.close();
            fileIn.close();

            // Print deserialized objects' data
            System.out.println("Deserialized Triangle Area: " + deserializedTriangle.calculateArea());
            System.out.println("Deserialized Triangle Perimeter: " + deserializedTriangle.calculatePerimeter());

            System.out.println("Deserialized Rectangle Area: " + deserializedRectangle.calculateArea());
            System.out.println("Deserialized Rectangle Perimeter: " + deserializedRectangle.calculatePerimeter());

            System.out.println("Deserialized Circle Area: " + deserializedCircle.calculateArea());
            System.out.println("Deserialized Circle Perimeter: " + deserializedCircle.calculatePerimeter());

            System.out.println("Deserialized Square Area: " + deserializedSquare.calculateArea());
            System.out.println("Deserialized Square Perimeter: " + deserializedSquare.calculatePerimeter());

        } catch (IOException | ClassNotFoundException i) {
            i.printStackTrace();
        }

        System.out.println("Shape Color: " + Shape.color);
    }
}