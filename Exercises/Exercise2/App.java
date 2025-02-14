public class App {
  public static void main(String[] args) {
      Shape triangle = new Triangle(3, 4, 5);
      Shape rectangle = new Rectangle(5, 10);
      Shape circle = new Circle(7);
      Shape square = new Square(6);

      System.out.println("Triangle Area: " + triangle.calculateArea());
      System.out.println("Triangle Perimeter: " + triangle.calculatePerimeter());

      System.out.println("Rectangle Area: " + rectangle.calculateArea());
      System.out.println("Rectangle Perimeter: " + rectangle.calculatePerimeter());

      System.out.println("Circle Area: " + circle.calculateArea());
      System.out.println("Circle Perimeter: " + circle.calculatePerimeter());

      System.out.println("Square Area: " + square.calculateArea());
      System.out.println("Square Perimeter: " + square.calculatePerimeter());

      System.out.println("Shape Color: " + Shape.color);
  }
}
