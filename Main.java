import functions.TabulatedFunction;
import functions.ArrayTabulatedFunction;
import functions.LinkedListTabulatedFunction;
import functions.FunctionPoint;
import functions.FunctionPointIndexOutOfBoundsException;
import functions.InappropriateFunctionPointException;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== ТЕСТИРОВАНИЕ ARRAY TABULATED FUNCTION ===");
        testArrayTabulatedFunction();

        System.out.println("\n=== ТЕСТИРОВАНИЕ LINKED LIST TABULATED FUNCTION ===");
        testLinkedListTabulatedFunction();

        System.out.println("\n=== ТЕСТИРОВАНИЕ ЧЕРЕЗ ИНТЕРФЕЙС ===");
        testThroughInterface();
    }

    public static void testArrayTabulatedFunction() {
        try {
            // Тест конструкторов с исключениями
            System.out.println("1. Тест конструкторов:");

            try {
                TabulatedFunction func = new ArrayTabulatedFunction(10, 5, 5); // leftX > rightX
                System.out.println("   ОШИБКА: Не выброшено исключение для leftX >= rightX");
            } catch (IllegalArgumentException e) {
                System.out.println("   ✓ IllegalArgumentException: " + e.getMessage());
            }

            try {
                TabulatedFunction func = new ArrayTabulatedFunction(0, 10, 1); // pointsCount < 2
                System.out.println("   ОШИБКА: Не выброшено исключение для pointsCount < 2");
            } catch (IllegalArgumentException e) {
                System.out.println("   ✓ IllegalArgumentException: " + e.getMessage());
            }

            // Создаем корректную функцию для дальнейших тестов
            TabulatedFunction func = new ArrayTabulatedFunction(0, 10, 5);
            System.out.println("   ✓ Корректная функция создана");

            // Тест методов доступа с исключениями
            System.out.println("2. Тест методов доступа:");

            try {
                func.getPoint(-1);
                System.out.println("   ОШИБКА: Не выброшено исключение для index < 0");
            } catch (FunctionPointIndexOutOfBoundsException e) {
                System.out.println("   ✓ FunctionPointIndexOutOfBoundsException: " + e.getMessage());
            }

            try {
                func.getPoint(10);
                System.out.println("   ОШИБКА: Не выброшено исключение для index >= size");
            } catch (FunctionPointIndexOutOfBoundsException e) {
                System.out.println("   ✓ FunctionPointIndexOutOfBoundsException: " + e.getMessage());
            }

            // Тест методов изменения с исключениями
            System.out.println("3. Тест методов изменения:");

            try {
                func.setPointX(0, -1); // нарушение упорядоченности
                System.out.println("   ОШИБКА: Не выброшено исключение для нарушения упорядоченности");
            } catch (InappropriateFunctionPointException e) {
                System.out.println("   ✓ InappropriateFunctionPointException: " + e.getMessage());
            }

            try {
                func.addPoint(new FunctionPoint(2.0, 5.0)); // точка с существующим X
                System.out.println("   ОШИБКА: Не выброшено исключение для дублирования X");
            } catch (InappropriateFunctionPointException e) {
                System.out.println("   ✓ InappropriateFunctionPointException: " + e.getMessage());
            }

            // Тест удаления точек
            System.out.println("4. Тест удаления точек:");

            // Создаем функцию с минимальным количеством точек
            TabulatedFunction smallFunc = new ArrayTabulatedFunction(0, 2, 2);

            try {
                smallFunc.deletePoint(0);
                System.out.println("   ОШИБКА: Не выброшено исключение при удалении до < 2 точек");
            } catch (IllegalStateException e) {
                System.out.println("   ✓ IllegalStateException: " + e.getMessage());
            }

        } catch (Exception e) {
            System.out.println("   НЕОЖИДАННАЯ ОШИБКА: " + e);
        }
    }

    public static void testLinkedListTabulatedFunction() {
        try {
            // Тест конструкторов с исключениями
            System.out.println("1. Тест конструкторов:");

            try {
                TabulatedFunction func = new LinkedListTabulatedFunction(10, 5, 5); // leftX > rightX
                System.out.println("   ОШИБКА: Не выброшено исключение для leftX >= rightX");
            } catch (IllegalArgumentException e) {
                System.out.println("   ✓ IllegalArgumentException: " + e.getMessage());
            }

            try {
                TabulatedFunction func = new LinkedListTabulatedFunction(0, 10, new double[]{1}); // pointsCount < 2
                System.out.println("   ОШИБКА: Не выброшено исключение для pointsCount < 2");
            } catch (IllegalArgumentException e) {
                System.out.println("   ✓ IllegalArgumentException: " + e.getMessage());
            }

            // Создаем корректную функцию для дальнейших тестов
            TabulatedFunction func = new LinkedListTabulatedFunction(0, 10, new double[]{1, 2, 3, 4, 5});
            System.out.println("   ✓ Корректная функция создана");

            // Тест методов доступа с исключениями
            System.out.println("2. Тест методов доступа:");

            try {
                func.getPointY(-1);
                System.out.println("   ОШИБКА: Не выброшено исключение для index < 0");
            } catch (FunctionPointIndexOutOfBoundsException e) {
                System.out.println("   ✓ FunctionPointIndexOutOfBoundsException: " + e.getMessage());
            }

            try {
                func.setPointY(10, 5.0);
                System.out.println("   ОШИБКА: Не выброшено исключение для index >= size");
            } catch (FunctionPointIndexOutOfBoundsException e) {
                System.out.println("   ✓ FunctionPointIndexOutOfBoundsException: " + e.getMessage());
            }

            // Тест методов изменения с исключениями
            System.out.println("3. Тест методов изменения:");

            try {
                func.setPoint(1, new FunctionPoint(0.5, 10)); // нарушение упорядоченности
                System.out.println("   ОШИБКА: Не выброшено исключение для нарушения упорядоченности");
            } catch (InappropriateFunctionPointException e) {
                System.out.println("   ✓ InappropriateFunctionPointException: " + e.getMessage());
            }

            // Тест удаления точек
            System.out.println("4. Тест удаления точек:");

            TabulatedFunction smallFunc = new LinkedListTabulatedFunction(0, 2, 2);

            try {
                smallFunc.deletePoint(0);
                System.out.println("   ОШИБКА: Не выброшено исключение при удалении до < 2 точек");
            } catch (IllegalStateException e) {
                System.out.println("   ✓ IllegalStateException: " + e.getMessage());
            }

            // Тест корректной работы
            System.out.println("5. Тест корректной работы:");
            TabulatedFunction workingFunc = new LinkedListTabulatedFunction(0, 4, new double[]{0, 1, 4, 9});

            // Добавление точки
            workingFunc.addPoint(new FunctionPoint(2.5, 6.25));
            System.out.println("   ✓ Точка добавлена корректно");

            // Изменение Y координаты
            workingFunc.setPointY(2, 6.0);
            System.out.println("   ✓ Y координата изменена корректно");

            // Получение значения функции
            double value = workingFunc.getFunctionValue(1.5);
            System.out.println("   ✓ Значение функции в x=1.5: " + value);

        } catch (Exception e) {
            System.out.println("   НЕОЖИДАННАЯ ОШИБКА: " + e);
        }
    }

    public static void testThroughInterface() {
        System.out.println("Тестирование через интерфейс TabulatedFunction:");

        // Тестирование ArrayTabulatedFunction через интерфейс
        TabulatedFunction arrayFunc = new ArrayTabulatedFunction(0, 5, 3);
        testCommonFunctionality(arrayFunc, "ArrayTabulatedFunction");

        // Тестирование LinkedListTabulatedFunction через интерфейс
        TabulatedFunction listFunc = new LinkedListTabulatedFunction(0, 5, new double[]{1, 2, 3});
        testCommonFunctionality(listFunc, "LinkedListTabulatedFunction");

        // Сравнение работы обеих реализаций
        System.out.println("Сравнение реализаций:");
        compareImplementations();
    }

    public static void testCommonFunctionality(TabulatedFunction func, String type) {
        System.out.println("\nТестирование " + type + ":");

        try {
            // Базовые операции
            System.out.println("  Количество точек: " + func.getPointsCount());
            System.out.println("  Левая граница: " + func.getLeftDomainBorder());
            System.out.println("  Правая граница: " + func.getRightDomainBorder());

            // Доступ к точкам
            for (int i = 0; i < func.getPointsCount(); i++) {
                FunctionPoint point = func.getPoint(i);
                System.out.printf("  Точка %d: (%.1f, %.1f)%n", i, point.getX(), point.getY());
            }

            // Вычисление значения функции
            double testX = 2.5;
            double value = func.getFunctionValue(testX);
            System.out.printf("  f(%.1f) = %.2f%n", testX, value);

            System.out.println("  ✓ Все операции выполнены успешно");

        } catch (Exception e) {
            System.out.println("  ОШИБКА: " + e);
        }
    }

    public static void compareImplementations() {
        try {
            // Создаем ОДИНАКОВЫЕ функции в обеих реализациях
            // Функция y = x^2 на интервале [0, 10] с 6 точками
            double leftX = 0;
            double rightX = 10;
            int pointsCount = 6;

            // Вычисляем значения Y для функции y = x^2
            double[] values = new double[pointsCount];
            double step = (rightX - leftX) / (pointsCount - 1);
            for (int i = 0; i < pointsCount; i++) {
                double x = leftX + i * step;
                values[i] = x * x; // y = x^2
            }

            // Создаем функции с одинаковыми параметрами
            TabulatedFunction arrayFunc = new ArrayTabulatedFunction(leftX, rightX, values);
            TabulatedFunction listFunc = new LinkedListTabulatedFunction(leftX, rightX, values);

            System.out.println("  Созданы функции y = x^2 на [0, 10] с 6 точками");

            // Проверяем, что точки одинаковые
            System.out.println("  Проверка точек:");
            boolean pointsMatch = true;
            for (int i = 0; i < pointsCount; i++) {
                FunctionPoint arrayPoint = arrayFunc.getPoint(i);
                FunctionPoint listPoint = listFunc.getPoint(i);

                boolean xMatch = Math.abs(arrayPoint.getX() - listPoint.getX()) < 1e-10;
                boolean yMatch = Math.abs(arrayPoint.getY() - listPoint.getY()) < 1e-10;

                System.out.printf("    Точка %d: массив(%.1f, %.1f) список(%.1f, %.1f) совпадают=%s%n",
                        i, arrayPoint.getX(), arrayPoint.getY(), listPoint.getX(), listPoint.getY(),
                        (xMatch && yMatch) ? "ДА" : "НЕТ");

                if (!xMatch || !yMatch) pointsMatch = false;
            }

            // Сравниваем значения функции в различных точках
            double[] testPoints = {0, 1.0, 2.5, 5.0, 7.5, 10.0, 12.0}; // включая точку вне домена
            boolean valuesMatch = true;

            System.out.println("  Сравнение значений функции:");
            for (double x : testPoints) {
                double y1 = arrayFunc.getFunctionValue(x);
                double y2 = listFunc.getFunctionValue(x);

                // Для точек вне домена оба должны вернуть NaN
                // Для точек внутри домена значения должны совпадать
                boolean match;
                if (x < leftX || x > rightX) {
                    match = Double.isNaN(y1) && Double.isNaN(y2);
                } else {
                    match = Math.abs(y1 - y2) < 1e-10;
                }

                System.out.printf("    f(%.1f): массив=%-10.6f список=%-10.6f совпадают=%-5s%n",
                        x, y1, y2, match ? "ДА" : "НЕТ");

                if (!match) {
                    valuesMatch = false;
                    if (!Double.isNaN(y1) && !Double.isNaN(y2)) {
                        System.out.printf("      Разница: %.10f%n", Math.abs(y1 - y2));
                    }
                }
            }

            // Проверяем границы домена
            boolean bordersMatch =
                    Math.abs(arrayFunc.getLeftDomainBorder() - listFunc.getLeftDomainBorder()) < 1e-10 &&
                            Math.abs(arrayFunc.getRightDomainBorder() - listFunc.getRightDomainBorder()) < 1e-10;

            System.out.printf("  Границы домена - массив: [%.1f, %.1f]%n",
                    arrayFunc.getLeftDomainBorder(), arrayFunc.getRightDomainBorder());
            System.out.printf("  Границы домена - список: [%.1f, %.1f]%n",
                    listFunc.getLeftDomainBorder(), listFunc.getRightDomainBorder());
            System.out.println("  Границы совпадают: " + (bordersMatch ? "ДА" : "НЕТ"));

            // Итоговый результат
            if (pointsMatch && valuesMatch && bordersMatch) {
                System.out.println("  ✓ Реализации работают идентично");
            } else {
                System.out.println("  ✗ Реализации работают по-разному");
                if (!pointsMatch) System.out.println("    - Точки не совпадают");
                if (!valuesMatch) System.out.println("    - Значения функции не совпадают");
                if (!bordersMatch) System.out.println("    - Границы домена не совпадают");
            }

        } catch (Exception e) {
            System.out.println("  ОШИБКА при сравнении: " + e);
            e.printStackTrace();
        }
    }

}