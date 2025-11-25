package functions;

<<<<<<< HEAD
// Интерфейс для табулированных функций
public interface TabulatedFunction {
    // Методы доступа к точкам
    int getPointsCount();
    FunctionPoint getPoint(int index) throws FunctionPointIndexOutOfBoundsException;
    void setPoint(int index, FunctionPoint point) throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException;
    double getPointX(int index) throws FunctionPointIndexOutOfBoundsException;
    void setPointX(int index, double x) throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException;
    double getPointY(int index) throws FunctionPointIndexOutOfBoundsException;
    void setPointY(int index, double y) throws FunctionPointIndexOutOfBoundsException;

    // Методы модификации точек
    void deletePoint(int index) throws FunctionPointIndexOutOfBoundsException, IllegalStateException;
    void addPoint(FunctionPoint point) throws InappropriateFunctionPointException;

    // Методы работы с функцией
    double getLeftDomainBorder();
    double getRightDomainBorder();
    double getFunctionValue(double x);
=======
public interface TabulatedFunction {
    // Методы для работы с функцией
    double getLeftDomainBorder();
    double getRightDomainBorder();
    double getFunctionValue(double x);

    // Методы для работы с точками
    int getPointsCount();
    FunctionPoint getPoint(int index) throws FunctionPointIndexOutOfBoundsException;
    void setPoint(int index, FunctionPoint point)
            throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException;
    double getPointX(int index) throws FunctionPointIndexOutOfBoundsException;
    void setPointX(int index, double x)
            throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException;
    double getPointY(int index) throws FunctionPointIndexOutOfBoundsException;
    void setPointY(int index, double y) throws FunctionPointIndexOutOfBoundsException;

    // Методы изменения количества точек
    void deletePoint(int index) throws FunctionPointIndexOutOfBoundsException;
    void addPoint(FunctionPoint point) throws InappropriateFunctionPointException;

    // Вспомогательный метод для вывода
    void printFunction();
>>>>>>> origin
}