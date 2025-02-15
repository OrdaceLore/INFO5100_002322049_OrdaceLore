import java.io.Serializable;

class Square extends Shape implements Serializable {
    private static final long serialVersionUID = 1L;
    private double side;

    public Square(double side) {
        this.side = side;
    }

    @Override
    public double calculateArea() {
        return side * side;
    }

    @Override
    public double calculatePerimeter() {
        return 4 * side;
    }
}