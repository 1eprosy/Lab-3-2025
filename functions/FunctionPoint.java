package functions;

public class FunctionPoint {
    private double x;
    private double y;
    private static final double EPSILON = 1e-10;

    // Конструкторы
    public FunctionPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public FunctionPoint(FunctionPoint point) {
        this.x = point.x;
        this.y = point.y;
    }

    public FunctionPoint() {
        this(0, 0);
    }

    // Геттеры и сеттеры
    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    // Методы сравнения с использованием эпсилона
    public static boolean equals(double a, double b) {
        return Math.abs(a - b) <= EPSILON;
    }

    public static boolean lessThan(double a, double b) {
        return a < b - EPSILON;
    }

    public static boolean greaterThan(double a, double b) {
        return a > b + EPSILON;
    }

    public static boolean lessOrEqual(double a, double b) {
        return a <= b + EPSILON;
    }

    public static boolean greaterOrEqual(double a, double b) {
        return a >= b - EPSILON;
    }
}