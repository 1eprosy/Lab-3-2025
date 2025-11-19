package functions;

public class ArrayTabulatedFunction implements TabulatedFunction {
    private FunctionPoint[] points;
    private int pointsCount;
    private static final double EPSILON = 1e-10;

    // КОНСТРУКТОРЫ
    public ArrayTabulatedFunction(double leftX, double rightX, int pointsCount) {
        if (leftX >= rightX || pointsCount < 2) {
            throw new IllegalArgumentException("Некорректные параметры функции");
        }

        this.pointsCount = pointsCount;
        this.points = new FunctionPoint[pointsCount + 10];

        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            points[i] = new FunctionPoint(x, 0);
        }
    }

    public ArrayTabulatedFunction(double leftX, double rightX, double[] values) {
        if (leftX >= rightX || values.length < 2) {
            throw new IllegalArgumentException("Некорректные параметры функции");
        }

        this.pointsCount = values.length;
        this.points = new FunctionPoint[pointsCount + 10];

        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            points[i] = new FunctionPoint(x, values[i]);
        }
    }

    // РЕАЛИЗАЦИЯ МЕТОДОВ ИНТЕРФЕЙСА
    @Override
    public double getLeftDomainBorder() {
        return points[0].getX();
    }

    @Override
    public double getRightDomainBorder() {
        return points[pointsCount - 1].getX();
    }

    @Override
    public double getFunctionValue(double x) {
        if (x < getLeftDomainBorder() || x > getRightDomainBorder()) {
            return Double.NaN;
        }

        for (int i = 0; i < pointsCount - 1; i++) {
            if (x >= points[i].getX() && x <= points[i + 1].getX()) {
                if (equals(x, points[i].getX())) {
                    return points[i].getY();
                }
                if (equals(x, points[i + 1].getX())) {
                    return points[i + 1].getY();
                }

                double x1 = points[i].getX();
                double y1 = points[i].getY();
                double x2 = points[i + 1].getX();
                double y2 = points[i + 1].getY();

                return y1 + (y2 - y1) * (x - x1) / (x2 - x1);
            }
        }

        return Double.NaN;
    }

    @Override
    public int getPointsCount() {
        return pointsCount;
    }

    @Override
    public FunctionPoint getPoint(int index) throws FunctionPointIndexOutOfBoundsException {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException(index);
        }
        return new FunctionPoint(points[index]);
    }

    @Override
    public void setPoint(int index, FunctionPoint point)
            throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException(index);
        }

        if (index > 0 && lessOrEqual(point.getX(), points[index - 1].getX())) {
            throw new InappropriateFunctionPointException(point);
        }
        if (index < pointsCount - 1 && greaterOrEqual(point.getX(), points[index + 1].getX())) {
            throw new InappropriateFunctionPointException(point);
        }

        points[index] = new FunctionPoint(point);
    }

    @Override
    public double getPointX(int index) throws FunctionPointIndexOutOfBoundsException {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException(index);
        }
        return points[index].getX();
    }

    @Override
    public void setPointX(int index, double x)
            throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException(index);
        }

        if (index > 0 && lessOrEqual(x, points[index - 1].getX())) {
            throw new InappropriateFunctionPointException("New X coordinate " + x +
                    " is inappropriate for point at index " + index);
        }
        if (index < pointsCount - 1 && greaterOrEqual(x, points[index + 1].getX())) {
            throw new InappropriateFunctionPointException("New X coordinate " + x +
                    " is inappropriate for point at index " + index);
        }

        points[index].setX(x);
    }

    @Override
    public double getPointY(int index) throws FunctionPointIndexOutOfBoundsException {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException(index);
        }
        return points[index].getY();
    }

    @Override
    public void setPointY(int index, double y) throws FunctionPointIndexOutOfBoundsException {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException(index);
        }
        points[index].setY(y);
    }

    @Override
    public void deletePoint(int index) throws FunctionPointIndexOutOfBoundsException {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException(index);
        }
        if (pointsCount <= 2) {
            throw new IllegalStateException("Нельзя удалить точку - минимальное количество точек: 2");
        }

        System.arraycopy(points, index + 1, points, index, pointsCount - index - 1);
        pointsCount--;
    }

    @Override
    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        int insertIndex = 0;
        while (insertIndex < pointsCount && lessThan(points[insertIndex].getX(), point.getX())) {
            insertIndex++;
        }

        if (insertIndex < pointsCount && equals(points[insertIndex].getX(), point.getX())) {
            throw new InappropriateFunctionPointException(point);
        }

        if (pointsCount == points.length) {
            FunctionPoint[] newPoints = new FunctionPoint[points.length * 2];
            System.arraycopy(points, 0, newPoints, 0, pointsCount);
            points = newPoints;
        }

        System.arraycopy(points, insertIndex, points, insertIndex + 1, pointsCount - insertIndex);
        points[insertIndex] = new FunctionPoint(point);
        pointsCount++;
    }

    @Override
    public void printFunction() {
        System.out.println("Табулированная функция (массив):");
        for (int i = 0; i < pointsCount; i++) {
            System.out.printf("(%.6f; %.6f) ", points[i].getX(), points[i].getY());
        }
        System.out.println();
    }

    // Вспомогательные методы для сравнения
    private boolean equals(double a, double b) {
        return Math.abs(a - b) <= EPSILON;
    }

    private boolean lessThan(double a, double b) {
        return a < b - EPSILON;
    }

    private boolean greaterThan(double a, double b) {
        return a > b + EPSILON;
    }

    private boolean lessOrEqual(double a, double b) {
        return a <= b + EPSILON;
    }

    private boolean greaterOrEqual(double a, double b) {
        return a >= b - EPSILON;
    }
}